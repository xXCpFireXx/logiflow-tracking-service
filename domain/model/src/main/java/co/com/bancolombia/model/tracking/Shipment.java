package co.com.bancolombia.model.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {
    private String id;
    private CargoDetail cargo;
    private List<Document> documents;
}