package co.com.bancolombia.consumer.dto;

import co.com.bancolombia.consumer.CargoInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {
    @JsonProperty("_id")
    private String id;

    private String trackingNumber;
    private String status;

    @JsonProperty("cargo") // cargo info
    private CargoInfo cargo;

    private List<DocumentResponse> documents;

    private UiDetailsResponse details;
}