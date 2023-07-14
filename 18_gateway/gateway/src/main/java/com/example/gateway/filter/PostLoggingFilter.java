package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@Slf4j
public class PostLoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 사용자에게 응답이 들어가고 난 뒤에 실행하는 필터
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpRequest httpRequest = exchange.getRequest();
                    String requestId = httpRequest.getHeaders().getFirst("x-gateway-request-id");
                    String requestTimeString = httpRequest.getHeaders().getFirst("x-gateway-request-time");
                    // 현재 시각
                    long timeEnd = Instant.now().toEpochMilli();
                    // PreLoggingFilter 에서 기록한 시각
                    long timeStart = Long.parseLong(requestTimeString);
                    // 기록
                    log.info("Execution Time id: {}, timeDiff(ms): {}", requestId, timeEnd - timeStart);
                }));
    }
}
