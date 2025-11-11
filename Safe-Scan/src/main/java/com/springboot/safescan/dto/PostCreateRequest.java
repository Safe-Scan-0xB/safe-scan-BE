package com.springboot.safescan.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class PostCreateRequest {
    @NotBlank private String title;
    @NotBlank private String content;
    @NotNull  private Long categoryId;           // ERD: FK (category.id)
    @NotBlank private String userId;      // 로그인 아이디 (users.userId)

    @Size(max = 9) private List<MultipartFile> images;

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }

    public @NotNull Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull Long categoryId) {
        this.categoryId = categoryId;
    }

    public @NotBlank String getUserId() {
        return userId;
    }

    public void setUserId(@NotBlank String userId) {
        this.userId = userId;
    }

    public @Size(max = 9) List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(@Size(max = 9) List<MultipartFile> images) {
        this.images = images;
    }
}
