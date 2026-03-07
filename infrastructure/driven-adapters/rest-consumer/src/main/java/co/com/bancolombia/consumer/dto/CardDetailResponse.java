package co.com.bancolombia.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponse {
    private String label;
    private String value;
    private String subtext; // AQUÍ VA EL "Warehouse H-22"
}