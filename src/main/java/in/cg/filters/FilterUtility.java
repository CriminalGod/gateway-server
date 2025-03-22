package in.cg.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class FilterUtility {

    public static final String CORRELATION_HEADER = "cs-correlation-id";

    public String getCorrelationIdHeader(HttpHeaders httpHeaders) {
        List<String> correlationIdList = httpHeaders.get(CORRELATION_HEADER);
        return correlationIdList == null || correlationIdList.isEmpty() ? null
                : correlationIdList.stream().findFirst().orElse(null);
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setCorrelationIdHeader(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_HEADER, correlationId);
    }
}
