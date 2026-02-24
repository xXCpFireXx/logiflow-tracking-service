package co.com.bancolombia.api.truck;

import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.gateways.TrackingEventTruck;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class TrackingEventTruckImpl implements TrackingEventTruck {

    // Multicast: varios pueden escuchar al mismo tiempo
    private final Sinks.Many<Tracking> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public void emit(Tracking tracking) {
        sink.tryEmitNext(tracking);
    }

    @Override
    public Flux<Tracking> getEvents(String shipmentId) {
        return sink.asFlux()
                .filter(t -> t.getShipmentId().equals(shipmentId)); // Solo escucha los suyos
    }
}