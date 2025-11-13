package com.springboot.safescan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequest {

    private String title;
    private String content;
    private Long categoryId;

    private List<Long> keepPhotoIds;
    private List<MultipartFile> newImages;
}