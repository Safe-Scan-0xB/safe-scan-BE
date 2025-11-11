package com.springboot.safescan.service;


import com.springboot.safescan.dto.*;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Long createPost(PostCreateRequest req);
    PostDetailResponse getPost(Long id);
    PageResponse<PostSummaryResponse> listPosts(Long categoryId, String q, Pageable pageable);
    Long addComment(CommentCreateRequest req);
    PageResponse<CommentResponse> listComments(Long postId, Pageable pageable);
}
