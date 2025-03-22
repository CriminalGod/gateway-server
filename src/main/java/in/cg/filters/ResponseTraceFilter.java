package in.cg.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    private final FilterUtility filterUtility;

    @Autowired
    public ResponseTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
                    String correlationId = filterUtility.getCorrelationIdHeader(httpHeaders);
                    logger.debug("Updated {} to the outbound response headers: {}", FilterUtility.CORRELATION_HEADER,
                            correlationId);
                    exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_HEADER, correlationId);
                }));
    }

}
