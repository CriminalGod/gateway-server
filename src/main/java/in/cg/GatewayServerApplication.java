package in.cg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator cgRoutesConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r.path("/cg/micros-1/**")
                        .filters(f -> f.rewritePath("/cg/micros-1/(?<segment>.*)", "/${segment}"))
                        .uri("lb://MICROS-1")
                )
                .route(r -> r.path("/cg/micros-2/**")
                        .filters(f -> f.rewritePath("/cg/micros-2/(?<segment>.*)", "/${segment}"))
                        .uri("lb://MICROS-2")
                )
                .route(r -> r.path("/cg/micros-3/**")
                        .filters(f -> f.rewritePath("/cg/micros-3/(?<segment>.*)", "/${segment}"))
                        .uri("lb://MICROS-3")
                )
                .build();
    }
}
