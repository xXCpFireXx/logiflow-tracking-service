package co.com.bancolombia.model.tracking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Document {
    private final String name;
    private final String format;
    private final String size;
}