package com.springboot.safescan.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private String createdAt;
    private int viewCount;
    private int commentCount;
    private String userId;
    private List<String> imageUrls;
    private List<CommentResponse> comments;
}
