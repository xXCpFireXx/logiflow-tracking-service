package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.dto.ShipmentResponse;
import co.com.bancolombia.model.tracking.CargoDetail;
import co.com.bancolombia.model.tracking.Document;
import co.com.bancolombia.model.tracking.gateways.ShipmentGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ShipmentGateway {
    private final WebClient client;


    // These methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
    @CircuitBreaker(name = "testGet" /*, fallbackMethod = "testGetOk"*/) // This name should match with settings name in application.yaml
    public Mono<ObjectResponse> testGet() {
        return client
                .get()
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get() // TODO: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    @CircuitBreaker(name = "testPost") // This name should match with settings name in application.yaml
    public Mono<ObjectResponse> testPost() {
        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), ObjectRequest.class)
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

    @Override
    public Mono<List<CargoDetail>> getCargoDetails(String shipmentId) {
        return client.get()
                .uri("/shipments/" + shipmentId)
                .retrieve()
                .bodyToMono(ShipmentResponse.class)
                .map(response -> response.getDetails() != null ? response.getDetails() : new java.util.ArrayList<CargoDetail>())
                .onErrorResume(e -> Mono.just(new java.util.ArrayList<CargoDetail>()));
    }

    @Override
    public Mono<List<Document>> getShipmentDocuments(String shipmentId) {
        return client.get()
                .uri("/shipments/" + shipmentId)
                .retrieve()
                .bodyToMono(ShipmentResponse.class)
                .map(response -> response.getDocuments() != null ? response.getDocuments() : new java.util.ArrayList<Document>())
                .onErrorResume(e -> Mono.just(new java.util.ArrayList<Document>()));
    }
}
