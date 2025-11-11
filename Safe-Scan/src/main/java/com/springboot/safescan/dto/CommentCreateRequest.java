package com.springboot.safescan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentCreateRequest {
    @NotNull  private Long postId;
    @NotBlank private String userId; // users.userId
    @NotBlank private String content;

    public @NotNull Long getPostId() {
        return postId;
    }

    public void setPostId(@NotNull Long postId) {
        this.postId = postId;
    }

    public @NotBlank String getUserId() {
        return userId;
    }

    public void setUserId(@NotBlank String userId) {
        this.userId = userId;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }
}
