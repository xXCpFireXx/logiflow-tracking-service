package co.com.bancolombia.model.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Tracking {
    private final String id;         // ID técnico de MongoDB
    private final String shipmentId; // Relación técnica con Shipment
    private final String trackingId; // ID de negocio (#SHP-2540)
    private TrackingStatus status;
    private String currentLocation;
    private TruckPositions truckPositions;
    private final List<HistoryStep> history;
    private List<CargoDetail> cargoDetails;
    private List<Document> documents;


    // constructor (2) para crear trackings nuevos y para los Test
    public Tracking(String id, String shipmentId, String trackingId) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.trackingId = trackingId;
        this.status = TrackingStatus.CREATED; // Estado inicial por defecto
        this.history = new ArrayList<>();
        this.cargoDetails = new ArrayList<>();
        this.documents = new ArrayList<>();
    }

    public Tracking updateLiveStatus(Coordinate newBluePosition, TrackingStatus newStatus, String city) {
        if (this.status == TrackingStatus.DELIVERED) {
            throw new IllegalStateException("Shipment already delivered");
        }

        this.status = newStatus;
        this.currentLocation = city;

        // Aquí está el secreto:
        // Creamos nuevas posiciones con el AZUL nuevo,
        // pero mantenemos el NARANJA que ya estaba en el objeto.
        this.truckPositions = new TruckPositions(
                newBluePosition,
                this.truckPositions.getOrange()
        );

        return this;
    }
}