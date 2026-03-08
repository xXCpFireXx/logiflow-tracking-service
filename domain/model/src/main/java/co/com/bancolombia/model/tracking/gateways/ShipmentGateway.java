package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.Shipment;
import co.com.bancolombia.model.tracking.UpdateShipmentStatusRequest;
import reactor.core.publisher.Mono;

public interface ShipmentGateway {
    Mono<Shipment> getDetails(String shipmentId);
    Mono<Void> updateShipmentStatus(String shipmentId, UpdateShipmentStatusRequest request);

}