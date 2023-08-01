package om.polarbookshop.gateway.fallback.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class RouterConfiguration {

	@Bean
	public RouterFunction<ServerResponse> catalogRouter() {
		return route(GET("/catalog-fallback"), request -> ok().body(Mono.just(""), String.class))
				.andRoute(POST("/catalog-fallback"), request-> status(HttpStatus.SERVICE_UNAVAILABLE).build());
	}

}
