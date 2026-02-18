package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.Coordinate;
import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TruckPositions;
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetCurrentTrackingUseCase {
    private final TrackingRepository repository;
    private final ShipmentGateway shipmentGateway;

    public Mono<Tracking> current(String shipmentId) {
        return repository.findEventsByShipmentId(shipmentId)
                .collectList()
                .zipWith(shipmentGateway.getDetails(shipmentId))
                .map(tuple -> {
                    var events = tuple.getT1();       // List<TrackingEvent>
                    var shipmentInfo = tuple.getT2();

                    if (events.isEmpty()) {
                        return Tracking.builder().shipmentId(shipmentId).build();
                    }

                    // Lógica de carritos (Azul y Naranja)
                    var firstEvent = events.get(0);
                    var lastEvent = events.get(events.size() - 1);

                    return Tracking.builder()
                            .id(lastEvent.getShipmentId())
                            .shipmentId(shipmentId)
                            .trackingId("TRK-" + shipmentId)
                            .status(lastEvent.getStatus())
                            .currentLocation(lastEvent.getCity() + ", " + lastEvent.getCountryCode())
                            .truckPositions(new TruckPositions(
                                    new Coordinate(lastEvent.getLatitude(), lastEvent.getLongitude()),
                                    new Coordinate(firstEvent.getLatitude(), firstEvent.getLongitude())
                            ))
                            .history(events)
                            .cargoDetails(shipmentInfo.getCargo() != null ?
                                    java.util.Collections.singletonList(shipmentInfo.getCargo()) :
                                    new java.util.ArrayList<>())
                            .build();
                });
    }
}