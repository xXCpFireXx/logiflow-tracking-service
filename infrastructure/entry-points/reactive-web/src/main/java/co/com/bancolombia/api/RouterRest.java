package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(TrackingHandler handler) {
        return route(POST("/tracking/events"), handler::registerEvent)
                .andRoute(GET("/tracking/shipments/{shipmentId}/history"), handler::getHistory)
                .andRoute(GET("/tracking/shipments/{shipmentId}/current"), handler::getCurrentStatus)
                .andRoute(GET("/tracking/stream/{shipmentId}"), handler::streamTracking);
    }
}