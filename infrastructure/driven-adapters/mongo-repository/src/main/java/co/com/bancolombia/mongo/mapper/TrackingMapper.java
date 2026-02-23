package co.com.bancolombia.mongo.mapper;

import co.com.bancolombia.model.tracking.*;
import co.com.bancolombia.mongo.document.LiveTrackingDocument;
import co.com.bancolombia.mongo.document.TrackingDocument;

public class TrackingMapper {

    public static TrackingDocument toDocument(TrackingEvent event) {
        return TrackingDocument.builder()
                .shipmentId(event.getShipmentId())
                .status(event.getStatus().name())
                .description(event.getDescription())
                .occurredAt(event.getOccurredAt())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .city(event.getCity())
                .countryCode(event.getCountryCode())
                .build();
    }

    public static TrackingEvent toEntity(TrackingDocument doc) {
        return new TrackingEvent(
                doc.getShipmentId(),
                TrackingStatus.valueOf(doc.getStatus()),
                doc.getDescription(),
                doc.getOccurredAt(),
                doc.getLatitude(),
                doc.getLongitude(),
                doc.getCity(),
                doc.getCountryCode()
        );
    }

    public static LiveTrackingDocument toLiveDocument(Tracking tracking) {
        return LiveTrackingDocument.builder()
                .shipmentId(tracking.getShipmentId())
                .status(tracking.getStatus().name())
                .blueLatitude(tracking.getTruckPositions().getBlue().getLatitude())
                .blueLongitude(tracking.getTruckPositions().getBlue().getLongitude())
                .orangeLatitude(tracking.getTruckPositions().getOrange().getLatitude())
                .orangeLongitude(tracking.getTruckPositions().getOrange().getLongitude())
                .build();
    }

    public static Tracking toEntity(LiveTrackingDocument doc) {
        Coordinate blue = new Coordinate(doc.getBlueLatitude(), doc.getBlueLongitude());
        Coordinate orange = new Coordinate(doc.getOrangeLatitude(), doc.getOrangeLongitude());

        return Tracking.builder()
                .shipmentId(doc.getShipmentId())
                .status(TrackingStatus.valueOf(doc.getStatus()))
                .truckPositions(new TruckPositions(blue, orange))
                .build();
    }
}