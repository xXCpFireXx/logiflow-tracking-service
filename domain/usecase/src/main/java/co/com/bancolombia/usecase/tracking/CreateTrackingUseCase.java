package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.Coordinate;
import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.TruckPositions;
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import co.com.bancolombia.model.tracking.gateways.TrackingEventTruck;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateTrackingUseCase {
    private final TrackingRepository repository;
    private final TrackingEventTruck eventTruck;
    private final ShipmentGateway shipmentGateway;

    public Mono<TrackingEvent> register(TrackingEvent event) {
        return repository.saveEvent(event)
                .flatMap(savedEvent ->
                        repository.findByShipmentId(event.getShipmentId())
                                .switchIfEmpty(Mono.defer(() -> createInitialTracking(event))) // Ahora es reactivo
                                .flatMap(current -> {
                                    current.updateLiveStatus(
                                            new Coordinate(event.getLatitude(), event.getLongitude()),
                                            event.getStatus(),
                                            event.getCity()
                                    );
                                    return repository.save(current)
                                            .doOnNext(eventTruck::emit); // se emite el sse
                                })
                                .thenReturn(savedEvent)
                );
    }

    // Metodo auxiliar para crear el primer registro de tracking vivo
    private Mono<Tracking> createInitialTracking(TrackingEvent event) {
        // 1. Cambiamos el nombre del método a getDetails
        return shipmentGateway.getDetails(event.getShipmentId())
                .map(shipment -> {
                    Coordinate initialPos = new Coordinate(event.getLatitude(), event.getLongitude());
                    return Tracking.builder()
                            // El ID técnico lo dejamos nulo para que no se repita con shipmentId en el JSON
                            .shipmentId(event.getShipmentId())
                            // 2. Usamos el campo correcto que viene del microservicio de Shipment
                            .trackingId(shipment.getTrackingNumber())
                            .status(event.getStatus())
                            .currentLocation(event.getCity())
                            .truckPositions(new TruckPositions(initialPos, initialPos))
                            .cargoDetails(new java.util.ArrayList<>())
                            .documents(new java.util.ArrayList<>())
                            .build();
                });
    }
}
