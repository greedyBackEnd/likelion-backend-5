package com.example.article.service;

import com.example.article.dto.CommentDto;
import com.example.article.entity.CommentEntity;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        entity.setArticleId(articleId);
        entity = commentRepository.save(entity);

        return CommentDto.fromEntity(entity);
    }

    // TODO 게시글 댓글 전체 조회
    // 반환 타입 이름 인자
    public List<CommentDto> readCommentAll(Long articleId) {
        List<CommentEntity> commentEntities = commentRepository.findAllByArticleId(articleId);
        List<CommentDto> commentList = new ArrayList<>();
        for (CommentEntity entity : commentEntities) {
            commentList.add(CommentDto.fromEntity(entity));
        }
        return commentList;
    }

    // 게시글 댓글 수정
    public CommentDto updateComment(
            Long articleId,
            Long commentId,
            CommentDto dto
    ) {
        // 요청한 댓글이 존재하는지 확인
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        // 존재하지 않으면 예외 발생
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 존재하면 로직 진행
        CommentEntity comment = optionalComment.get();

        // 대상 댓글이 대상 게시글의 댓글이 맞는지
        if (!articleId.equals(comment.getArticleId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        comment.setContent(dto.getContent());
        comment.setWriter(dto.getWriter());
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    // TODO 게시글 댓글 삭제
    public void deleteComment(Long articleId, Long commentId) {
        // 요청한 댓글이 존재하는지 확인
        if (!commentRepository.existsById(commentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        CommentEntity comment = commentRepository.findById(commentId).get();

        // 대상 댓글이 대상 게시글의 댓글이 맞는지
        if (!comment.getArticleId().equals(articleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        commentRepository.deleteById(commentId);
    }
}
