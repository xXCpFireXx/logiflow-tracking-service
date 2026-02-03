// (El Hecho): Es lo que "llega" del exterior (el GPS mandó una señal). Es un dato puntual.
package co.com.bancolombia.model.tracking;

import java.time.Instant;

public class TrackingEvent {
    private final String shipmentId;
    private final TrackingStatus status;
    private final String description;
    private final Instant occurredAt;
    private final Coordinate location;

    public TrackingEvent(String shipmentId, TrackingStatus status, String description,
                         Instant occurredAt, Coordinate location) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException("ShipmentId is required");
        }
        this.shipmentId = shipmentId;
        this.status = status;
        this.description = description;
        this.occurredAt = occurredAt != null ? occurredAt : Instant.now();
        this.location = location;
    }

    public String getShipmentId() { return shipmentId; }
    public TrackingStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getOccurredAt() { return occurredAt; }
    public Coordinate getLocation() { return location; }
}