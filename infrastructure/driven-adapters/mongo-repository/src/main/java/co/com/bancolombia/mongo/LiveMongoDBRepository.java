package co.com.bancolombia.mongo;

import co.com.bancolombia.mongo.document.LiveTrackingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveMongoDBRepository extends ReactiveMongoRepository<LiveTrackingDocument, String> {
}