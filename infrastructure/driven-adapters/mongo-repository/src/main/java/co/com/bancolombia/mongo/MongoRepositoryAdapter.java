package co.com.bancolombia.mongo;

import co.com.bancolombia.model.tracking.Tracking;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.model.tracking.gateways.TrackingRepository;
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

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> TrackingMapper.toEntity((TrackingDocument) d));
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
        // Por ahora lo dejamos en vacío hasta que definamos
        return Flux.empty();
    }

    @Override
    public Mono<Tracking> findByShipmentId(String shipmentId) {
        return Mono.empty();
    }

    @Override
    public Mono<Tracking> save(Tracking tracking) {
        return Mono.empty();
    }

}