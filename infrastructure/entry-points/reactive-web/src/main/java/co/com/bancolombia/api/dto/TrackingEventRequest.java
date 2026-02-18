package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.TrackingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrackingEventRequest {
    private String shipmentId;
    private String status;
    private String description;
    private double latitude;
    private double longitude;
    private String city;
    private String countryCode;

    public TrackingEvent toDomain() {
        return new TrackingEvent(
                this.shipmentId,
                TrackingStatus.valueOf(this.status),
                this.description,
                null, // El dominio se encarga del tiempo
                this.latitude,
                this.longitude,
                this.city,
                this.countryCode
        );
    }
}