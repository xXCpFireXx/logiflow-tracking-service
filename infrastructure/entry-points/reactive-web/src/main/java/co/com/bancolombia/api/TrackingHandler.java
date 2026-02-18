package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.TrackingEventRequest;
import co.com.bancolombia.model.tracking.TrackingEvent;
import co.com.bancolombia.usecase.tracking.GetCurrentTrackingUseCase;
import co.com.bancolombia.usecase.tracking.GetTrackingHistoryUseCase;
import co.com.bancolombia.usecase.tracking.CreateTrackingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrackingHandler {
    // Inyectamos los cerebros del microservicio
    private final CreateTrackingUseCase registerUseCase;
    private final GetTrackingHistoryUseCase historyUseCase;
    private final GetCurrentTrackingUseCase currentUseCase;

    public Mono<ServerResponse> registerEvent(ServerRequest request) {
        return request.bodyToMono(TrackingEventRequest.class)
                .map(TrackingEventRequest::toDomain)
                .flatMap(registerUseCase::register)
                .flatMap(event -> ServerResponse.status(HttpStatus.CREATED).build());
    }

    public Mono<ServerResponse> getHistory(ServerRequest request) {
        String shipmentId = request.pathVariable("shipmentId");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(historyUseCase.history(shipmentId), TrackingEvent.class);
    }

    public Mono<ServerResponse> getCurrentStatus(ServerRequest request) {
        String shipmentId = request.pathVariable("shipmentId");
        return currentUseCase.current(shipmentId)
                .flatMap(tracking -> ServerResponse.ok().bodyValue(tracking));
    }

    public Mono<ServerResponse> streamTracking(ServerRequest request) {
        String shipmentId = request.pathVariable("shipmentId");

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM) // Esto le dice al navegador: "Mantén la conexión abierta"
                .body(historyUseCase.history(shipmentId), TrackingEvent.class);
    }
}