package co.com.bancolombia.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Este agrupa los 4 cuadritos
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UiDetailsResponse {
    private CardDetailResponse origin;
    private CardDetailResponse destination;
    private CardDetailResponse carrier;
    private CardDetailResponse weight;
}