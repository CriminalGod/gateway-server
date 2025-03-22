package in.cg.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    private final FilterUtility filterUtility;

    @Autowired
    public RequestTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(httpHeaders)) {
            logger.debug("{} found in RequestTraceFilter {}", FilterUtility.CORRELATION_HEADER,
                    filterUtility.getCorrelationIdHeader(httpHeaders));
        } else {
            String correlationId = generateCorrelationId();
            exchange = filterUtility.setCorrelationIdHeader(exchange, correlationId);
            logger.debug("{} added in RequestTraceFilter {}", FilterUtility.CORRELATION_HEADER, correlationId);
        }
        return chain.filter(exchange);
    }

    private boolean isCorrelationIdPresent(HttpHeaders httpHeaders) {
        return filterUtility.getCorrelationIdHeader(httpHeaders) != null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
