package co.com.bancolombia.usecase.tracking;

import co.com.bancolombia.model.tracking.Coordinate;
import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.TruckPositions;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateTrackingUseCase {
    private final TrackingRepository repository;

    public Mono<TrackingEvent> register(TrackingEvent event) {
        return repository.saveEvent(event) // 1. Guarda siempre el historial
                .flatMap(savedEvent ->
                        repository.findByShipmentId(event.getShipmentId()) // 2. Busca el estado vivo
                                .switchIfEmpty(Mono.defer(() -> createInitialTracking(event))) // 3. ¡LA CLAVE! Si no existe, lo crea
                                .flatMap(currentTracking -> {
                                    // 4. Actualiza el azul (movimiento) y mantiene el naranja
                                    currentTracking.updateLiveStatus(
                                            new Coordinate(event.getLatitude(), event.getLongitude()),
                                            event.getStatus(),
                                            event.getCity()
                                    );
                                    return repository.save(currentTracking);
                                })
                                .thenReturn(savedEvent)
                );
    }

    // Método auxiliar para crear el primer registro de tracking vivo
    private Mono<Tracking> createInitialTracking(TrackingEvent event) {
        Coordinate initialPos = new Coordinate(event.getLatitude(), event.getLongitude());
        return Mono.just(Tracking.builder()
                .shipmentId(event.getShipmentId())
                .trackingId("TRK-" + event.getShipmentId().substring(0, 8).toUpperCase())
                .status(event.getStatus())
                .currentLocation(event.getCity())
                // Al principio, el camión azul y el punto naranja están en el mismo sitio
                .truckPositions(new TruckPositions(initialPos, initialPos))
                .history(new java.util.ArrayList<>())
                .cargoDetails(new java.util.ArrayList<>())
                .documents(new java.util.ArrayList<>())
                .build());
    }
}
