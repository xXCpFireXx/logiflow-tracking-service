// (El Hecho): Es lo que "llega" del exterior (el GPS mandó una señal). Es un dato puntual.
package co.com.bancolombia.model.tracking;

import java.time.Instant;

public class TrackingEvent {
    private final String shipmentId;
    private final TrackingStatus status;
    private final String description;
    private final Instant occurredAt;
    private final double latitude;
    private final double longitude;
    private final String city;
    private final String countryCode;

    public TrackingEvent(String shipmentId, TrackingStatus status, String description, Instant occurredAt, double latitude, double longitude, String city, String countryCode) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException("ShipmentId is required");
        }
        this.shipmentId = shipmentId;
        this.status = status;
        this.description = description;
        this.occurredAt = occurredAt != null ? occurredAt : Instant.now();
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.countryCode = countryCode;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public TrackingStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }
}