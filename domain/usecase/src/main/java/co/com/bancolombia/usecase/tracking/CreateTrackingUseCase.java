package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateTrackingUseCase {
    private final TrackingRepository repository;

    public Mono<TrackingEvent> register(TrackingEvent event) {
        // Aquí podrías añadir lógica extra, como validar que el evento no sea duplicado
        return repository.saveEvent(event);
    }
}
