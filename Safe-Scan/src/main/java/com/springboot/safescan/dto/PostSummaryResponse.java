package com.springboot.safescan.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PostSummaryResponse {
    private Long id;
    private String title;
    private String excerpt;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;
    private String categoryName;
    private List<String> imageUrls;

}
