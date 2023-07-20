package com.example.article.controller;

import com.example.article.dto.CommentDto;
import com.example.article.repository.CommentRepository;
import com.example.article.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // @ResponseBody 생략 가능
@RequestMapping("/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentService service;

    // 게시글 작성
    // POST /articles/{articleId}/comments
    @PostMapping
    public CommentDto create(
            @PathVariable("articleId") Long articleId,
            @RequestBody CommentDto dto
    ) {
        return service.createComment(articleId, dto);
    }

    // 게시글, 댓글 전체 조회
    // GET /articles/{articleId}/comments
    @GetMapping
    public List<CommentDto> readAll(@PathVariable("articleId") Long articleId) {
        return service.readCommentAll(articleId);
    }

    // 게시글 댓글 수정
    // PUT /articles/{articleId}/comments/{commentId}
    @PutMapping("/{commentId}")
    public CommentDto updateComment(
            @PathVariable("articleId") Long articleId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        return service.updateComment(articleId, commentId, dto);
    }

    // 게시글 댓글 삭제
    // DELETE /articles/{articlesId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable("articleId") Long articleId,
            @PathVariable("commentId") Long commentId
    ) {
        service.deleteComment(articleId, commentId);
    }


}
