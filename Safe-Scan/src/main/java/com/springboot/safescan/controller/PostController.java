package com.springboot.safescan.controller;


import com.springboot.safescan.dto.*;
import com.springboot.safescan.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록 (이미지 포함)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long create(@Valid @ModelAttribute PostCreateRequest req) {
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

    // 댓글 등록
    @PostMapping("/{postId}/comments")
    public Long addComment(@PathVariable Long postId, @Valid @RequestBody CommentCreateRequest req) {
        // path의 postId를 본문과 맞춰 안전하게 사용하려면 다음 라인 추가 가능:
        if (!postId.equals(req.getPostId())) throw new IllegalArgumentException("postId 불일치");
        return postService.addComment(req);
    }

    // 댓글 목록
    @GetMapping("/{postId}/comments")
    public PageResponse<CommentResponse> listComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return postService.listComments(postId, PageRequest.of(page, Math.min(size, 50)));
    }
}