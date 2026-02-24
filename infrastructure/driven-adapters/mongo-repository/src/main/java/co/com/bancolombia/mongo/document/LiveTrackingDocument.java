package co.com.bancolombia.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tracking")
public class LiveTrackingDocument {
    @Id
    private String shipmentId;
    private String trackingId;
    private String status;

    private Double blueLatitude;
    private Double blueLongitude;

    // Punto Naranja (El origen fijo)
    private Double orangeLatitude;
    private Double orangeLongitude;
}