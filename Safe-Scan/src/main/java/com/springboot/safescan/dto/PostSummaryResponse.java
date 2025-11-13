package com.springboot.safescan.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostSummaryResponse {
    private Long id;
    private String title;
    private String excerpt;
    private String createdAt;
    private int viewCount;
    private int commentCount;
    private String categoryName;
    private List<String> imageUrls;
}
