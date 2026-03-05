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
    private final String shipmentId; // Relación técnica con Shipment
    private final String trackingId; // ID de negocio (#SHP-2540)
    private TrackingStatus status;
    private String currentLocation;
    private TruckPositions truckPositions;
    private Object cargo;
    private List<Document> documents;


    // constructor (2) para crear trackings nuevos y para los Test
    public Tracking(String shipmentId, String trackingId) {
        this.shipmentId = shipmentId;
        this.trackingId = trackingId;
        this.status = TrackingStatus.CREATED; // Estado inicial por defecto
        this.cargo = new ArrayList<>();
        this.documents = new ArrayList<>();
    }

    public Tracking updateLiveStatus(Coordinate newBluePosition, TrackingStatus newStatus, String city) {
        if (this.status == TrackingStatus.DELIVERED) {
            throw new IllegalStateException("Shipment already delivered");
        }

        this.status = newStatus;
        this.currentLocation = city;

        // Creamos nuevas posiciones con el AZUL nuevo,
        // pero mantenemos el NARANJA que ya estaba en el objeto.
        this.truckPositions = new TruckPositions(
                newBluePosition,
                this.truckPositions.getOrange()
        );

        return this;
    }
}