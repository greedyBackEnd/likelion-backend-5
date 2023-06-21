package com.example.article;

import com.example.article.dto.ArticleDto;
import com.example.article.entity.ArticleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;

    public ArticleDto createArticle(ArticleDto dto) {
        ArticleEntity entity = new ArticleEntity();
        entity.setWriter(dto.getWriter());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());

        return ArticleDto.fromEntity(repository.save(entity));
    }

    public ArticleDto readArticle(Long id) {
        Optional<ArticleEntity> entity = repository.findById(id);
        if (entity.isPresent())
            return ArticleDto.fromEntity(entity.get());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<ArticleDto> readArticleAll() {
        List<ArticleDto> dtoList = new ArrayList<>();
        List<ArticleEntity> entityList = repository.findAll();
        for (ArticleEntity entity : entityList) {
            dtoList.add(ArticleDto.fromEntity(entity));
        }
        return dtoList;
    }

    public ArticleDto updateArticle(Long id, ArticleDto dto) {
        Optional<ArticleEntity> entityOptional = repository.findById(id);
        if (entityOptional.isPresent()) {
            ArticleEntity entity = entityOptional.get();
            entity.setWriter(dto.getWriter());
            entity.setTitle(dto.getTitle());
            entity.setContent(dto.getContent());
            repository.save(entity);
            return ArticleDto.fromEntity(entity);
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void deleteArticle(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
