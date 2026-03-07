package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.Coordinate;
import co.com.bancolombia.model.tracking.HistoryStep;
import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TruckPositions;
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

@RequiredArgsConstructor
public class GetCurrentTrackingUseCase {
    private final TrackingRepository repository;
    private final ShipmentGateway shipmentGateway;

    public Mono<Tracking> current(String shipmentId) {
        return Mono.zip(
                repository.findEventsByShipmentId(shipmentId).collectList(),
                shipmentGateway.getDetails(shipmentId)
        ).flatMap(tuple -> {
            var events = tuple.getT1();
            var shipmentInfo = tuple.getT2();

            if (events.isEmpty()) return Mono.empty();

            var lastEvent = events.get(events.size() - 1);

            return repository.findByShipmentId(shipmentId)
                    .map(live -> live.toBuilder()
                            .trackingId(shipmentInfo.getTrackingNumber()) // #SHP-C8F3 real
                            .status(lastEvent.getStatus())
                            .currentLocation(lastEvent.getCity() + ", " + lastEvent.getCountryCode())
                            .cargo(shipmentInfo.getCargo())
                            .documents(shipmentInfo.getDocuments())
                            .details(shipmentInfo.getDetails())
                            .build());
        });
    }

}