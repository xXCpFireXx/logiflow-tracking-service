package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.Tracking;
import reactor.core.publisher.Mono;

public interface TrackingRepository {
    Mono<Tracking> findByShipmentId(String shipmentId);
    Mono<Tracking> save(Tracking tracking);
}