package co.com.bancolombia.consumer;

import lombok.Data;

@Data
public class CargoInfo {
    private String packageType;
    private String commodity;
    private String quantity;
    private double weight;
}