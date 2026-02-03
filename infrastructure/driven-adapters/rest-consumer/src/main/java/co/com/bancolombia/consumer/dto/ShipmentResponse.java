package co.com.bancolombia.consumer.dto;

import co.com.bancolombia.model.tracking.CargoDetail;
import co.com.bancolombia.model.tracking.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {
    private String id;
    private String trackingId;
    private String status;
    private List<CargoDetail> details;  // Esto mapea los "Cargo Details"
    private List<Document> documents;    // Esto mapea los "Documents"
}