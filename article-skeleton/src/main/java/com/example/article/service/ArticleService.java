package com.example.article.service;

import com.example.article.dto.ArticleDto;
import com.example.article.entity.ArticleEntity;
import com.example.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void deleteArticle(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Page<ArticleDto> readArticlePaged(Integer pageNumber, Integer pageSize) {
        // PagingAndSortingRepository 메서드에 전달하는 용도
        // 조회하고 싶은 페이지의 정보를 담는 객체
        // {pageSize}개씩 데이터를 나눌때 {pageNumber}번 페이지를 달라고 요청하는 Pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<ArticleEntity> articleEntityPage = repository.findAll(pageable);

        // map: 전달받은 함수를 각 원소에 인자로 전달한 결과를 다시 모아서 Stream으로
        // Page.map: 전달받은 함수를 각 원소에 인자로 전달한 결과를 다시 모아서 Page로
        Page<ArticleDto> articleDtoPage = articleEntityPage.map(ArticleDto::fromEntity);

        return articleDtoPage;
    }

    public Page<ArticleDto> search(String query, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("id").descending());
        return repository.findAllByTitleContains(query, pageable).map(ArticleDto::fromEntity);

    }
}
