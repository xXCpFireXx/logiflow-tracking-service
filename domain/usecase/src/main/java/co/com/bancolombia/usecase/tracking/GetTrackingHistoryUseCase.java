// Este devuelve la lista de todos los eventos que le han pasado a un paquete (el Timeline)
package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.HistoryStep;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class GetTrackingHistoryUseCase {
    private final TrackingRepository repository;

    public Mono<List<HistoryStep>> history(String shipmentId) {
        return repository.findEventsByShipmentId(shipmentId)
                .collectList()
                .map(events -> {
                    if (events.isEmpty()) return new ArrayList<>();

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es", "CO"));
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    ZoneId bogotaZone = ZoneId.of("America/Bogota");

                    var lastEvent = events.get(events.size() - 1);

                    return events.stream()
                            .map(event -> {
                                var dateTime = event.getOccurredAt().atZone(bogotaZone);
                                return new HistoryStep(
                                        dateTime.format(dateFormatter),
                                        dateTime.format(timeFormatter),
                                        event.getStatus().toString(),
                                        event.getCity() + ", " + event.getCountryCode(),
                                        event.getDescription(),
                                        event == lastEvent, // active si es el último
                                        true              // completed siempre en historial
                                );
                            }).toList();
                });
    }
}