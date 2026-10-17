package com.projects.API.Gateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;

@Configuration
public class BookingServiceRoutes {
    @Bean
    public RouterFunction<ServerResponse> bookingRoutes() {
        return GatewayRouterFunctions.route("booking-service")
                // if you got on the gateway:port/api/v1/booking
                .route(RequestPredicates.POST("/api/v1/booking"), HandlerFunctions.http())
                // then act as 8081/api/v1/booking (the original service working port)
                .before(uri("http://localhost:8081"))
                // Track booking failures and forward to our local fallback when a call fails or the circuit is open.
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("bookingServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return GatewayRouterFunctions.route("fallbackRoute")
                // The internal forward preserves the original booking request's POST method.
                .POST("/fallbackRoute",
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Booking service is down"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceApiDocs() {
        return GatewayRouterFunctions.route("booking-service-api-docs")
                .route(RequestPredicates.path("/docs/bookingservice/v3/api-docs"), HandlerFunctions.http())
                .before(uri("http://localhost:8081"))
                // The booking service exposes its generated documentation at this path.
                .before(setPath("/v3/api-docs"))
                .build();
    }
}
