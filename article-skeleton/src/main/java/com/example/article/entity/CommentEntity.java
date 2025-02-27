package com.example.article.entity;

import jakarta.persistence.*;
import lombok.Data;

/*
id integer primary key autoincrement
writer text
content text
 */

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long articleId;
    private String writer;
    private String content;
}
