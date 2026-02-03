package co.com.bancolombia.usecase.tracking;

// Llamar al microservicio de Shipment (usando el Gateway) para traer los detalles de carga y documentos y Unimos all en el objeto Tracking
import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import co.com.bancolombia.model.tracking.TruckPositions;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class GetCurrentTrackingUseCase {
    private final TrackingRepository repository;
    private final ShipmentGateway shipmentGateway;

    public Mono<Tracking> current(String shipmentId) {
        // Combinamos la búsqueda en Mongo con la llamada al otro Microservicio
        return Mono.zip(
                repository.findEventsByShipmentId(shipmentId).collectList(), // Eventos de este micro
                shipmentGateway.getCargoDetails(shipmentId),                // Datos de Shipment Service
                shipmentGateway.getShipmentDocuments(shipmentId)            // Documentos de Shipment Service
        ).map(tuple -> {
            var events = tuple.getT1();
            var cargo = tuple.getT2();
            var docs = tuple.getT3();

            // Usamos el último evento para saber la posición actual y el estado
            TrackingEvent lastEvent = events.get(events.size() - 1);

            // Creamos el objeto Tracking completo para el Frontend
            return new Tracking(
                    "ID-TEMP", // ID técnico
                    shipmentId,
                    "TRK-" + shipmentId,
                    lastEvent.getStatus(),
                    lastEvent.getDescription(),
                    new TruckPositions(lastEvent.getLocation(), null), // Posición actual
                    new java.util.ArrayList<>(), // Aquí se podriamapear los HistorySteps
                    cargo,
                    docs
            );
        });
    }
}