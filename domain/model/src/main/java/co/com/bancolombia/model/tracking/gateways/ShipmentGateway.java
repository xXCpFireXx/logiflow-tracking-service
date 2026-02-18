package co.com.bancolombia.model.tracking.gateways;

import co.com.bancolombia.model.tracking.CargoDetail;
import co.com.bancolombia.model.tracking.Document;
import co.com.bancolombia.model.tracking.Shipment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ShipmentGateway {
    // Para validar si el envío existe y traer sus detalles de carga
//    Mono<List<CargoDetail>> getCargoDetails(String shipmentId); -> no lo utilizo

    Mono<Shipment> getDetails(String shipmentId);

    // Para traer los documentos del envío
//    Mono<List<Document>> getShipmentDocuments(String shipmentId); -> no lo utilizo
}