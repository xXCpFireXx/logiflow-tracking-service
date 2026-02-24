package co.com.bancolombia.mongo;

import co.com.bancolombia.model.tracking.*;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
import co.com.bancolombia.mongo.document.LiveTrackingDocument;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.document.TrackingDocument;
import co.com.bancolombia.mongo.mapper.TrackingMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<TrackingEvent, TrackingDocument, String, MongoDBRepository>
        implements TrackingRepository {

    private final LiveMongoDBRepository liveRepository;

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper, LiveMongoDBRepository liveRepository) {
        super(repository, mapper, d -> TrackingMapper.toEntity((TrackingDocument) d));
        this.liveRepository = liveRepository;
    }

    @Override
    public Mono<TrackingEvent> saveEvent(TrackingEvent event) {
        return repository.save(TrackingMapper.toDocument(event))
                .map(TrackingMapper::toEntity);
    }

    @Override
    public Flux<TrackingEvent> findEventsByShipmentId(String shipmentId) {
        return repository.findByShipmentId(shipmentId)
                .map(TrackingMapper::toEntity);
    }

    @Override
    public Flux<Tracking> findAllTrackings() {
        return liveRepository.findAll()
                .map(doc -> {
                    var blue = new Coordinate(doc.getBlueLatitude(), doc.getBlueLongitude());
                    var orange = new Coordinate(doc.getOrangeLatitude(), doc.getOrangeLongitude());

                    return Tracking.builder()
                            .shipmentId(doc.getShipmentId())
                            .status(TrackingStatus.valueOf(doc.getStatus()))
                            .truckPositions(new TruckPositions(blue, orange))
                            .build();
                });
    }

    @Override
    public Mono<Tracking> findByShipmentId(String shipmentId) {
        return liveRepository.findById(shipmentId)
                .map(doc -> {
                    double bLat = doc.getBlueLatitude() != null ? doc.getBlueLatitude() : 0.0;
                    double bLon = doc.getBlueLongitude() != null ? doc.getBlueLongitude() : 0.0;
                    double oLat = doc.getOrangeLatitude() != null ? doc.getOrangeLatitude() : 0.0;
                    double oLon = doc.getOrangeLongitude() != null ? doc.getOrangeLongitude() : 0.0;

                    Coordinate blue = new Coordinate(bLat, bLon);
                    Coordinate orange = new Coordinate(oLat, oLon);

                    return Tracking.builder()
                            .shipmentId(doc.getShipmentId())
                            .trackingId(doc.getTrackingId())
                            .status(doc.getStatus() != null ? TrackingStatus.valueOf(doc.getStatus()) : null)
                            .truckPositions(new TruckPositions(blue, orange))
                            .history(new java.util.ArrayList<>())
                            .cargoDetails(new java.util.ArrayList<>())
                            .documents(new java.util.ArrayList<>())
                            .build();
                });
    }

    @Override
    public Mono<Tracking> save(Tracking tracking) {
        var pos = tracking.getTruckPositions();

        LiveTrackingDocument liveDoc = LiveTrackingDocument.builder()
                .shipmentId(tracking.getShipmentId())
                .trackingId(tracking.getTrackingId())
                .status(tracking.getStatus() != null ? tracking.getStatus().name() : null)

                .blueLatitude(pos.getBlue().getLatitude())
                .blueLongitude(pos.getBlue().getLongitude())
                .orangeLatitude(pos.getOrange().getLatitude())
                .orangeLongitude(pos.getOrange().getLongitude())
                .build();

        return liveRepository.save(liveDoc)
                .thenReturn(tracking);
    }

}