package co.com.bancolombia.mongo;

import co.com.bancolombia.mongo.document.TrackingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Flux;

public interface MongoDBRepository extends ReactiveMongoRepository<TrackingDocument, String>,
        ReactiveQueryByExampleExecutor<TrackingDocument> {

    // Este metodo buscara todos los eventos asociados a un envío
    Flux<TrackingDocument> findByShipmentId(String shipmentId);
}