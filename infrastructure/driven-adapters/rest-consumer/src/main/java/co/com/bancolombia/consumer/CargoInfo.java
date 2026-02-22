package co.com.bancolombia.consumer;

import lombok.Data;

@Data
public class CargoInfo {
    private String packageType;
    private String quantity;
    private String dimensions;
    private String volume;
    private String commodity;
    private boolean stackable;
    private String hsCode;
    private double weight;
}