package com.example.article.controller;

import com.example.article.dto.ArticleDto;
import com.example.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QueryController {

    private final ArticleService service;

    //GET /path?query=keyword&limit=20
    @GetMapping("/path")
    public Map<String, Object> queryParams(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        log.info("query = " + query);
        log.info("limit = " + limit);

        Map<String, Object> response = new HashMap<>();
        response.put("query", query);
        response.put("limit", limit);
        return response;
    }

    //GET /search?query=en
    @GetMapping("/search")
    public Page<ArticleDto> search(
            @RequestParam("query") String query, // 검색어 필수
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber
    ) {
        return service.search(query, pageNumber);
    }
}
