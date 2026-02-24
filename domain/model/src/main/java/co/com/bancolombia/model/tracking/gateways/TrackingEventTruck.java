package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.Tracking;
import reactor.core.publisher.Flux;

public interface TrackingEventTruck {
    void emit(Tracking tracking); // Para enviar la actualización
    Flux<Tracking> getEvents(String shipmentId); // Para que el Front se suscriba
}