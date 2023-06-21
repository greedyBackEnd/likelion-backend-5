package com.example.article.controller;

import com.example.article.dto.CommentDto;
import com.example.article.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController // @ResponseBody 생략 가능
@RequestMapping("/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    // POST /articles/{articleId}/comments
    @PostMapping
    public CommentDto create(
            @PathVariable("articleId") Long articleId,
            @RequestBody CommentDto dto
    ) {
        return service.createComment(articleId, dto);
    }
}
