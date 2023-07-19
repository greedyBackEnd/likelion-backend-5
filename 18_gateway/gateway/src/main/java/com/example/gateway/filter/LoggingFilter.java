package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

<<<<<<< HEAD
@Component
@Slf4j
public class LoggingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(
            // HttpServletRequest & Response 대신 exchange
            ServerWebExchange exchange,
            // FilterChain
            GatewayFilterChain chain) {
        // 사용자가 보낸 HTTP 요청을 받는다
        ServerHttpRequest httpRequest = exchange.getRequest();
        // PreLoggingFilter 에서 요청을 식별할 수 있는 HTTP Header 를 작성
        // 이후 PostLoggingFilter 에서 해당 Header 를 바탕으로 실행에 걸린 시간 측정

        // 실행에 걸린 시간 측정
        String requestId = UUID.randomUUID().toString();

        // 사용자가 보낸 요청을 조작(변형)
        httpRequest.mutate()
                .headers(httpHeaders -> {
                    httpHeaders.add(
                            "x-gateway-request-id",
                            UUID.randomUUID().toString()
                    );
                    httpHeaders.add(
                            "x-gateway-request-time",
                            String.valueOf(Instant.now().toEpochMilli())
                    );
                });
        log.info("start transaction: {}", requestId);

        // filterChain.goFilter() 대신
        return chain.filter(exchange);
=======
@Slf4j
@Component
// Header 조작 없이 바로 기록하는 필터
public class LoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {
        String path = exchange.getRequest().getPath().toString();
        log.trace("Executed filter in PreLoggingFilter");
        long timeStart = Instant.now().toEpochMilli();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            log.info("Execution Time Path: {}, timediff(ms): {}",
                    path, Instant.now().toEpochMilli() - timeStart);
        }));
>>>>>>> origin/canary
    }
}
