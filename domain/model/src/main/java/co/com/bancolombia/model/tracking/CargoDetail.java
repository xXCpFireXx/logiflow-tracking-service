package co.com.bancolombia.model.tracking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CargoDetail {
    private final String label;
    private final String value;

}