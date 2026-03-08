package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.dto.ShipmentResponse;
import co.com.bancolombia.consumer.dto.CardDetailResponse; // Import necesario para el helper
import co.com.bancolombia.model.tracking.*; // Para Shipment, Document, ShipmentDetails, DetailItem
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ShipmentGateway {
    private final WebClient client;

    @CircuitBreaker(name = "testGet")
    public Mono<ObjectResponse> testGet() {
        return client.get().retrieve().bodyToMono(ObjectResponse.class);
    }

    @CircuitBreaker(name = "testPost")
    public Mono<ObjectResponse> testPost() {
        ObjectRequest request = ObjectRequest.builder().val1("val1").val2("val2").build();
        return client.post().body(Mono.just(request), ObjectRequest.class).retrieve().bodyToMono(ObjectResponse.class);
    }

    @Override
    public Mono<Shipment> getDetails(String shipmentId) {
        return client.get()
                .uri("/shipments/" + shipmentId)
                .retrieve()
                .bodyToMono(ShipmentResponse.class)
                .map(response -> {
                    // Armamos los detalles de los cuadritos aquí mismo
                    ShipmentDetails domainDetails = null;
                    if (response.getDetails() != null) {
                        domainDetails = ShipmentDetails.builder()
                                .origin(mapItem(response.getDetails().getOrigin()))
                                .destination(mapItem(response.getDetails().getDestination()))
                                .carrier(mapItem(response.getDetails().getCarrier()))
                                .weight(mapItem(response.getDetails().getWeight()))
                                .build();
                    }

                    // Seguimos con tu lógica del builder
                    return Shipment.builder()
                            .id(response.getId())
                            .trackingNumber(response.getTrackingNumber())
                            .status(response.getStatus())
                            .cargo(response.getCargo())
                            .documents(response.getDocuments() != null ?
                                    response.getDocuments().stream()
                                            .map(doc -> new Document(doc.getName(), doc.getFormat(), doc.getSize()))
                                            .toList()
                                    : new ArrayList<>())
                            // --- NUEVO CAMPO AGREGADO ---
                            .details(domainDetails)
                            .build();
                })
                .onErrorResume(e -> {
                    System.out.println("Error conectando a Shipment: " + e.getMessage());
                    return Mono.just(Shipment.builder().id(shipmentId).build());
                });
    }

    @Override
    public Mono<Void> updateShipmentStatus(String shipmentId, UpdateShipmentStatusRequest request) {
        return client.patch()
                .uri("/shipments/{id}/status", shipmentId)
                .bodyValue(request) // Spring Boot convertirá automáticamente tu DTO a {"status": "OUT_FOR_DELIVERY"}
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    // Si falla Shipment (está caído o el estado es inválido), lo registramos pero no rompemos el proceso de Tracking
                    System.err.println("Fallo al actualizar el estado en Shipment: " + e.getMessage());
                    return Mono.empty();
                });
    }

    // Helper privado dentro de la misma clase para no repetir código del "new DetailItem"
    private DetailItem mapItem(CardDetailResponse item) {
        if (item == null) return null;
        return DetailItem.builder()
                .label(item.getLabel())
                .value(item.getValue())
                .subtext(item.getSubtext()) // Aquí viaja el "Warehouse H-22"
                .build();
    }
}