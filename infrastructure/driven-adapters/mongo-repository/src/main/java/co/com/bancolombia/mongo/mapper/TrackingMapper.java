package co.com.bancolombia.mongo.mapper;

import co.com.bancolombia.model.tracking.Coordinate;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.TrackingStatus;
import co.com.bancolombia.mongo.document.TrackingDocument;

public class TrackingMapper {

    public static TrackingDocument toDocument(TrackingEvent event) {
        return TrackingDocument.builder()
                .shipmentId(event.getShipmentId())
                .status(event.getStatus().name())
                .description(event.getDescription())
                .occurredAt(event.getOccurredAt())
                .latitude(event.getLocation().getX())
                .longitude(event.getLocation().getY())
                .build();
    }

    public static TrackingEvent toEntity(TrackingDocument doc) {
        return new TrackingEvent(
                doc.getShipmentId(),
                TrackingStatus.valueOf(doc.getStatus()),
                doc.getDescription(),
                doc.getOccurredAt(),
                new Coordinate(doc.getLatitude(), doc.getLongitude())
        );
    }
}