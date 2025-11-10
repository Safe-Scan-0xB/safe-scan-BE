package com.springboot.safescan.service.impl;


import com.springboot.safescan.domain.CommunityPost;
import com.springboot.safescan.dto.CommentResponse;
import com.springboot.safescan.dto.PostCreateRequest;
import com.springboot.safescan.dto.PostDetailResponse;
import com.springboot.safescan.repository.*;
import com.springboot.safescan.service.PostService;
import com.springboot.safescan.service.port.ImageStoragePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageStoragePort imageStoragePort;

    @Override
    public Long createPost(PostCreateRequest req) {
        var category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        var user = userRepository.findByUserId(req.getWriterUserId())
                .orElseThrow(() -> new IllegalArgumentException("작성 유저가 존재하지 않습니다."));

        var post = new CommunityPost();
        post.setCategory(category);
        post.setUser(user);
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post = postRepository.save(post);

        // 이미지 저장(최대 9장)
        if (req.getImages() != null) {
            int order = 0;
            for (var file : req.getImages()) {
                if (file.isEmpty()) continue;
                var url = imageStoragePort.store(file);
                var photo = new CommunityPhoto();
                photo.setPost(post);
                photo.setUrl(url);
                photo.setSortOrder(order++);
                photoRepository.save(photo);
            }
        }
        return post.getId();
    }

    @Override
    public PostDetailResponse getPost(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        post.increaseView();

        var imageUrls = photoRepository.findAllByPostOrderBySortOrderAsc(post)
                .stream().map(CommunityPhoto::getUrl).toList();

        var comments = commentRepository.findTop20ByPostOrderByIdDesc(post)
                .stream().map(PostMapper::toCommentDto).toList();

        var res = new PostDetailResponse();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setCategoryName(post.getCategory().getCategoryName());
        res.setCreatedAt(post.getCreatedAt());
        res.setViewCount(post.getViewCount());
        res.setCommentCount((int) commentRepository.countByPost(post));
        res.setWriterUserId(post.getUser().getUserId());
        res.setImageUrls(imageUrls);
        res.setComments(comments);
        return res;
    }

    @Override
    public PageResponse<PostSummaryResponse> listPosts(Long categoryId, String q, Pageable pageable) {
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        }
        String query = StringUtils.hasText(q) ? q.trim() : null;

        Page<CommunityPost> page = postRepository.search(category, query, pageable);

        var content = page.getContent().stream().map(p -> {
            int commentCount = (int) commentRepository.countByPost(p);
            var firstImage = photoRepository.findTop1ByPostOrderBySortOrderAsc(p)
                    .stream().map(CommunityPhoto::getUrl).toList();
            return PostMapper.toSummary(p, p.getCategory().getCategoryName(), commentCount, firstImage);
        }).toList();

        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(),
                page.getNumber(), page.getSize(), page.hasNext());
    }

    @Override
    public Long addComment(CommentCreateRequest req) {
        var post = postRepository.findById(req.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        var user = userRepository.findByUserId(req.getWriterUserId())
                .orElseThrow(() -> new IllegalArgumentException("댓글 유저가 존재하지 않습니다."));

        var c = new CommunityComment();
        c.setPost(post);
        c.setUser(user);
        c.setContent(req.getContent());
        c = commentRepository.save(c);
        return c.getId();
    }

    @Override
    public PageResponse<CommentResponse> listComments(Long postId, Pageable pageable) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        var page = commentRepository.findByPost(post, pageable);
        var content = page.getContent().stream().map(PostMapper::toCommentDto).toList();
        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(),
                page.getNumber(), page.getSize(), page.hasNext());
    }
}
