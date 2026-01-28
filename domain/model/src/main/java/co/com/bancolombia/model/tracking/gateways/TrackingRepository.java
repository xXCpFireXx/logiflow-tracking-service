package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.Tracking;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface TrackingRepository {
    // Para buscar el historial completo de un envio
    Mono<Tracking> findByShipmentId(String shipmentId);

    // Para guardar o actualizar el estado y posiciones
    Mono<Tracking> save(Tracking tracking);

    // todos los trackings activos (para dashboard)
    Flux<Tracking> findAll();

    // Para buscar por el código que el cliente escribe en el buscador
    Mono<Tracking> findByTrackingId(String trackingId);
}