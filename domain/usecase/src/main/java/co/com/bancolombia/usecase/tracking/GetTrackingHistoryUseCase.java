// Este devuelve la lista de todos los eventos que le han pasado a un paquete (el Timeline)
package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

public class GetTrackingHistoryUseCase {
    private final TrackingRepository repository;

    public GetTrackingHistoryUseCase(TrackingRepository repository) {
        this.repository = repository;
    }

    public Flux<TrackingEvent> history(String shipmentId) {
        return repository.findEventsByShipmentId(shipmentId);
    }
}
