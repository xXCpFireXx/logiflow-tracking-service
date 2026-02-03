package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.tracking.Coordinate;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.TrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEventRequest {
    private String shipmentId;
    private String status;
    private String description;
    private Instant occurredAt;
    private CoordinateRequest location;

    public TrackingEvent toDomain() {
        return new TrackingEvent(
                this.shipmentId,
                TrackingStatus.valueOf(this.status),
                this.description,
                this.occurredAt,
                new Coordinate(this.location.getX(), this.location.getY())
        );
    }
}