package com.springboot.safescan.controller;


import com.springboot.safescan.dto.*;
import com.springboot.safescan.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록 (이미지 포함)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long create(@Valid @ModelAttribute PostCreateRequest req, Authentication auth) {
        String loginUserId = (String) auth.getPrincipal(); // JWT에서 세팅했던 subject
        return postService.createPost(req);
    }

    // 게시글 상세 (조회수 증가, 최근 댓글 20 포함)
    @GetMapping("/{id}")
    public PostDetailResponse detail(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 목록 (카테고리/검색/페이징)
    @GetMapping
    public PageResponse<PostSummaryResponse> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.listPosts(categoryId, q, PageRequest.of(page, Math.min(size, 50)));
    }

    // 게시글 수정
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updatePost(@PathVariable Long postId,
                           @ModelAttribute PostUpdateRequest req) {
        postService.updatePost(postId, req);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id,
                           @AuthenticationPrincipal String loginUserId) {
        postService.deletePost(id);
    }

    // 댓글 등록
    @PostMapping("/{postId}/comments")
    public Long addComment(@PathVariable Long postId,
                           @Valid @RequestBody CommentCreateRequest req) {

        return postService.addComment(postId, req.getContent());
    }
    // 댓글 목록
    @GetMapping("/{postId}/comments")
    public PageResponse<CommentResponse> listComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return postService.listComments(postId, PageRequest.of(page, Math.min(size, 50)));
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long postId,
                              @PathVariable Long commentId) {
        postService.deleteComment(postId, commentId);
    }
}