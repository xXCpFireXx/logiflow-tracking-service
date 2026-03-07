package co.com.bancolombia.model.tracking;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDetails {
    private DetailItem origin;
    private DetailItem destination;
    private DetailItem carrier;
    private DetailItem weight;
}