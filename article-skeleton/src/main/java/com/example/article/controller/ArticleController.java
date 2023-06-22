package com.example.article.controller;

import com.example.article.dto.ArticleDto;
import com.example.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService service;

    // POST /articles
    @PostMapping
    // RESTful한 API는 행동의 결과로 반영된 자원의 상태를 반환함이 옳음
    public ArticleDto create(@RequestBody ArticleDto dto) {
        return service.createArticle(dto);
    }

    // GET /articles
    @GetMapping
    public List<ArticleDto> readAll() {
        return service.readArticleAll();
    }

    // GET /paged (Pagination)
    @GetMapping("/paged")
    public Page<ArticleDto> readAllPaged(
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return service.readArticlePaged(pageNumber, pageSize);
    }

    // GET /articles/{id}
    @GetMapping("/{id}")
    public ArticleDto read(@PathVariable("id") Long id) {
        return service.readArticle(id);
    }

    // PUT /articles/{id}
    @PutMapping("/{id}")
    public ArticleDto update(
            @PathVariable("id") Long id,
            @RequestBody ArticleDto dto) {
        return service.updateArticle(id, dto);
    }

    // DELETE /articles/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.deleteArticle(id);
    }
}
