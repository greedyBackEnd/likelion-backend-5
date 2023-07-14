package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    // 라우팅
    // 어떤식으로 요청을 사용자에게서 다른 서버로 보낼 것인지
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // http://localhost:8080/articles/**
                // (/articles, /articles/{id}, /articles/{id}/{comments}
                // -> http://localhost:8082/articles/**
                .route("articles", predicate -> predicate
                        .path("/articles/**")
                        .uri("http://localhost:8082")
                )
                .route("auth", predicate -> predicate
                        // http://localhost:8080/auth/**
                        .path("/auth/**")
                        .filters(filter -> filter
                                // 정규 표현식을 활용하여 경로의 일부분 추출
                                .rewritePath("/auth/(?<path>.*)", "/${path}")
                        )
                        // -> http://localhost:8081/**
                        .uri("http://localhost:8081")
                )
                .build();
    }
}
