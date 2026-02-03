package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TrackingEvent;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface TrackingRepository {
    // Para el estado actual (Dashboard)
    Mono<Tracking> findByShipmentId(String shipmentId);
    Mono<Tracking> save(Tracking tracking);

    // Para la trazabilidad (Timeline/History)
    Flux<TrackingEvent> findEventsByShipmentId(String shipmentId);
    Mono<TrackingEvent> saveEvent(TrackingEvent event);

    Flux<Tracking> findAllTrackings();
}