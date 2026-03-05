package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.Shipment;
import reactor.core.publisher.Mono;

public interface ShipmentGateway {
    Mono<Shipment> getDetails(String shipmentId);
}