package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.dto.ShipmentResponse;
import co.com.bancolombia.model.tracking.CargoDetail;
import co.com.bancolombia.model.tracking.Shipment;
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ShipmentGateway {
    private final WebClient client;


    // These methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
    @CircuitBreaker(name = "testGet" /*, fallbackMethod = "testGetOk"*/) // This name should match with settings name in application.yaml
    public Mono<ObjectResponse> testGet() {
        return client
                .get()
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get() // TODO: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    @CircuitBreaker(name = "testPost") // This name should match with settings name in application.yaml
    public Mono<ObjectResponse> testPost() {
        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), ObjectRequest.class)
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

    @Override
    public Mono<Shipment> getDetails(String shipmentId) {
        return client.get()
                .uri("/api/shipments/" + shipmentId)
                .retrieve()
                .bodyToMono(ShipmentResponse.class)
                .map(response -> {
                    List<CargoDetail> detailsList = new ArrayList<>();

                    if (response.getCustomer() != null)
                        detailsList.add(new CargoDetail("Cliente", response.getCustomer()));

                    if (response.getCarrierName() != null)
                        detailsList.add(new CargoDetail("Transportadora", response.getCarrierName()));

                    if (response.getCargo() != null) {
                        CargoInfo info = response.getCargo();
                        if (info.getCommodity() != null)
                            detailsList.add(new CargoDetail("Mercancía", info.getCommodity()));
                        if (info.getPackageType() != null)
                            detailsList.add(new CargoDetail("Empaque", info.getPackageType()));
                        if (info.getWeight() > 0)
                            detailsList.add(new CargoDetail("Peso", info.getWeight() + " kg"));
                    }

                    return Shipment.builder()
                            .id(response.getId())
                            .cargoDetails(detailsList)
                            .documents(response.getDocuments() != null ? response.getDocuments() : new ArrayList<>())
                            .build();
                })
                .onErrorResume(e -> {
                    System.out.println("Error conectando a Shipment: " + e.getMessage());
                    return Mono.just(Shipment.builder()
                            .id(shipmentId)
                            .cargoDetails(new ArrayList<CargoDetail>())
                            .build());
                });
    }
}
