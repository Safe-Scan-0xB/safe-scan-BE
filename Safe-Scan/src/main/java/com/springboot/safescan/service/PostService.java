package com.springboot.safescan.service;


import com.springboot.safescan.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Long createPost(PostCreateRequest req);
    PostDetailResponse getPost(Long id);
    PageResponse<PostSummaryResponse> listPosts(Long categoryId, String q, Pageable pageable);
    Long addComment(Long postId, String comment);

    PageResponse<CommentResponse> listComments(Long postId, Pageable pageable);
    void updatePost(Long postId, PostUpdateRequest req);
    void deletePost(Long postId);
    void deleteComment(Long postId, Long commentId);

    List<PostSummaryResponse> listHotPosts(int limit);
}
