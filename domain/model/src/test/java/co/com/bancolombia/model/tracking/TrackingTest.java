package co.com.bancolombia.model.tracking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class TrackingTest {

    @Test
    void should_update_location_when_status_is_not_delivered() {
        Tracking tracking = new Tracking("65b2f1", "shp-001", "#SHP-2540");

        Coordinate blue = new Coordinate(10, 20);
        Coordinate orange = new Coordinate(11, 21);
        TruckPositions newPositions = new TruckPositions(blue, orange);

        // Act
        tracking.updateLiveStatus("New York Hub", newPositions);

        // Assert
        assertEquals("New York Hub", tracking.getCurrentLocation());
        assertEquals(10, tracking.getTruckPositions().getBlue().getX());
    }

    @Test
    void should_throw_exception_when_updating_delivered_shipment() {
        // Se usa el constructor 1 para forzar el estado DELIVERED
        Tracking tracking = new Tracking(
                "65b2f1",           // id
                "shp-001",          // shipmentId
                "#SHP-2540",        // trackingId
                TrackingStatus.DELIVERED, // STATUS
                "Paris",            // currentLocation
                null,               // truckPositions
                new ArrayList<>(),  // history
                new ArrayList<>(),  // cargoDetails
                new ArrayList<>()   // documents
        );

        TruckPositions newPositions = new TruckPositions(new Coordinate(1, 1), new Coordinate(2, 2));

        // Act & Assert: Verificamos
        assertThrows(IllegalStateException.class, () -> {
            tracking.updateLiveStatus("London", newPositions);
        });
    }
}