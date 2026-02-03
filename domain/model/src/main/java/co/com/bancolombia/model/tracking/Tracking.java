package co.com.bancolombia.model.tracking;

import java.util.List;
import java.util.ArrayList;

public class Tracking {
    private final String id;         // ID técnico de MongoDB
    private final String shipmentId; // Relación técnica con Shipment
    private final String trackingId; // ID de negocio (#SHP-2540)
    private TrackingStatus status;
    private String currentLocation;
    private TruckPositions truckPositions;
    private List<HistoryStep> history;
    private List<CargoDetail> cargoDetails;
    private List<Document> documents;

    // constructor (1) para el Mapper de Infraestructura (Carga todos los datos)
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

    // Regla de Negocio -> Proteger el estado
    public void updateLiveStatus(String location, TruckPositions positions) {
        if (this.status == TrackingStatus.DELIVERED) {
            throw new IllegalStateException("Shipment already delivered");
        }
        this.currentLocation = location;
        this.truckPositions = positions;
    }

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