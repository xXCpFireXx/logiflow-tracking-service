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

            if (events.isEmpty()) {
                return Mono.just(Tracking.builder()
                        .shipmentId(shipmentId)
                        .trackingId(shipmentInfo.getTrackingNumber())
                        .build());
            }

            var firstEvent = events.get(0);
            var lastEvent = events.get(events.size() - 1);

            // Formateo de historia (Tu lógica está perfecta)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es", "CO"));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            ZoneId bogotaZone = ZoneId.of("America/Bogota");

            var historySteps = events.stream()
                    .map(event -> {
                        var dateTime = event.getOccurredAt().atZone(bogotaZone);
                        return new HistoryStep(
                                dateTime.format(dateFormatter),
                                dateTime.format(timeFormatter),
                                event.getStatus().toString(),
                                event.getCity() + ", " + event.getCountryCode(),
                                event.getDescription(),
                                event == lastEvent,
                                true
                        );
                    }).toList();

            return repository.findByShipmentId(shipmentId)
                    .defaultIfEmpty(Tracking.builder()
                            .shipmentId(shipmentId)
                            .truckPositions(new TruckPositions(
                                    new Coordinate(lastEvent.getLatitude(), lastEvent.getLongitude()),
                                    new Coordinate(firstEvent.getLatitude(), firstEvent.getLongitude())
                            ))
                            .build())
                    .map(live -> live.toBuilder()
                            .shipmentId(shipmentId)
                            .trackingId(shipmentInfo.getTrackingNumber())
                            .status(lastEvent.getStatus())
                            .currentLocation(lastEvent.getCity() + ", " + lastEvent.getCountryCode())
                            .history(historySteps)
                            .cargoDetails(shipmentInfo.getCargoDetails() != null ? shipmentInfo.getCargoDetails() : new ArrayList<>())
                            .documents(shipmentInfo.getDocuments() != null ? shipmentInfo.getDocuments() : new ArrayList<>())
                            .build());
        });
    }
}