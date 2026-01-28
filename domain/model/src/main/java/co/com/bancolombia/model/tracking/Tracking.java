package co.com.bancolombia.model.tracking;

import java.util.List;
import java.util.ArrayList;

public class Tracking {
    private final String id;
    private final String shipmentId;
    private final String trackingId;
    private TrackingStatus status;
    private String currentLocation;
    private TruckPositions truckPositions;
    private List<HistoryStep> history;
    private List<CargoDetail> cargoDetails;
    private List<Document> documents;

    // Constructor completo para el Mapper (Infraestructura)
    public Tracking(String id, String shipmentId, String trackingId, TrackingStatus status,
                    String currentLocation, TruckPositions truckPositions,
                    List<HistoryStep> history, List<CargoDetail> cargoDetails,
                    List<Document> documents) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.trackingId = trackingId;
        this.status = status;
        this.currentLocation = currentLocation;
        this.truckPositions = truckPositions;
        this.history = history != null ? history : new ArrayList<>();
        this.cargoDetails = cargoDetails != null ? cargoDetails : new ArrayList<>();
        this.documents = documents != null ? documents : new ArrayList<>();
    }

    // Regla de Negocio: Solo actualizamos si no ha terminado
    public void updateLiveStatus(String location, TruckPositions positions) {
        if (this.status == TrackingStatus.DELIVERED) {
            throw new IllegalStateException("Shipment already delivered");
        }
        this.currentLocation = location;
        this.truckPositions = positions;
    }

    // getter
    public String getId() { return id; }
    public String getShipmentId() { return shipmentId; }
    public String getTrackingId() { return trackingId; }
    public TrackingStatus getStatus() { return status; }
    public String getCurrentLocation() { return currentLocation; }
    public TruckPositions getTruckPositions() { return truckPositions; }
    public List<HistoryStep> getHistory() { return history; }
    public List<CargoDetail> getCargoDetails() { return cargoDetails; }
    public List<Document> getDocuments() { return documents; }
}