package co.com.bancolombia.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tracking_events")
public class TrackingDocument {
    @Id
    private String id;
    private String shipmentId;
    private String status;
    private String description;
    private java.time.Instant occurredAt;
    private double latitude;
    private double longitude;
    private String city;
    private String countryCode;
}