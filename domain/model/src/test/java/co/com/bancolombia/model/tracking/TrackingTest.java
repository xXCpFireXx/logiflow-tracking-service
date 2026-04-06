package co.com.bancolombia.model.tracking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class TrackingTest {

    @Test
    void should_update_location_when_status_is_not_delivered() {
        // 1. Creamos los detalles del envío que ahora pide el constructor
        ShipmentDetails details = new ShipmentDetails();

        // 2. Usamos el constructor de 3 parámetros: (id, trackingId, shipmentDetails)
        Tracking tracking = new Tracking("65b2f1", "#SHP-2540", details);

        Coordinate position = new Coordinate(10.0, 20.0);

        // 3. El método ahora recibe: (Coordinate, TrackingStatus, String)
        tracking.updateLiveStatus(position, TrackingStatus.IN_TRANSIT, "New York Hub");

        // Assert
        assertEquals("New York Hub", tracking.getCurrentLocation());
        assertEquals(10.0, tracking.getTruckPositions().getBlue().getLatitude());
    }

    @Test
    void should_throw_exception_when_updating_delivered_shipment() {
        ShipmentDetails details = new ShipmentDetails();

        // Usamos el constructor completo (8 parámetros) para forzar el estado DELIVERED
        Tracking tracking = new Tracking(
                "65b2f1",           // id
                "#SHP-2540",        // trackingId
                TrackingStatus.DELIVERED, // status
                "Paris",            // currentLocation
                new TruckPositions(new Coordinate(0.0,0.0), new Coordinate(0.0,0.0)), // truckPositions
                new ArrayList<>(),  // history
                new ArrayList<>(),  // documents
                details             // shipmentDetails
        );

        Coordinate newPos = new Coordinate(1.0, 1.0);

        // Verificamos que lance error si intentamos mover algo ya entregado
        assertThrows(IllegalStateException.class, () -> {
            tracking.updateLiveStatus(newPos, TrackingStatus.DELIVERED, "London");
        });
    }
}