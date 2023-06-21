package com.example.article.service;

import com.example.article.dto.CommentDto;
import com.example.article.entity.CommentEntity;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentDto createComment(Long articleId, CommentDto dto) {

        // articleId를 Id로 가진 articleEntity가 존재 여부 확인
        if (!articleRepository.existsById(articleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        CommentEntity entity = new CommentEntity();
        entity.setWriter(dto.getWriter());
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId());
        entity = commentRepository.save(entity);

        return CommentDto.fromEntity(entity);
    }
}
