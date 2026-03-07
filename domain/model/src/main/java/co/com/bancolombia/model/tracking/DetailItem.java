package co.com.bancolombia.model.tracking;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DetailItem {
    private String label;
    private String value;
    private String subtext;
}