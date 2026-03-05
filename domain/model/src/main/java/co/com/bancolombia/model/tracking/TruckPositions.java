package co.com.bancolombia.model.tracking;

public class TruckPositions {
    private final Coordinate blue;
    private final Coordinate orange;

    public TruckPositions(Coordinate blue, Coordinate orange) {
        this.blue = blue;
        this.orange = orange;
    }

    public Coordinate getBlue() { return blue; }
    public Coordinate getOrange() { return orange; }
}