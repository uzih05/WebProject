package com.example.myweb.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;
    private String createdAt;
}
