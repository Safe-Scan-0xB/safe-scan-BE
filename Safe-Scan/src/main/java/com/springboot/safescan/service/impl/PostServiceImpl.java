package com.springboot.safescan.service.impl;

import com.springboot.safescan.domain.*;
import com.springboot.safescan.dto.*;
import com.springboot.safescan.mapper.PostMapper;
import com.springboot.safescan.repository.*;
import com.springboot.safescan.service.PostService;
import com.springboot.safescan.service.port.ImageStoragePort;
import com.springboot.safescan.util.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.safescan.service.impl.PostServiceImpl.SecurityUtil.getCurrentUserId;

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
        String loginUserId = getCurrentUserId();
        var category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        var user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("작성 유저가 존재하지 않습니다."));

        var post = new CommunityPost();
        post.setCategory(category);
        post.setUser(user);
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post = postRepository.save(post);

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

        var photos = photoRepository.findAllByPostOrderBySortOrderAsc(post);
        var imageDtos = photos.stream().map(photo -> {
            var dto = new ImageInfoResponse();
            dto.setId(photo.getId());
            dto.setUrl(photo.getUrl());
            return dto;
        }).collect(Collectors.toList());

        var comments = commentRepository.findTop20ByPostOrderByIdDesc(post)
                .stream().map(PostMapper::toCommentDto)
                .collect(Collectors.toList());

        var res = new PostDetailResponse();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setCategoryName(post.getCategory().getCategoryName());
        res.setCreatedAt(DateUtils.format(post.getCreatedAt()));
        res.setViewCount(post.getViewCount());
        res.setCommentCount((int) commentRepository.countByPost(post));
        res.setUserId(post.getUser().getUserId());
        res.setImages(imageDtos);
        res.setComments(comments);

        return res;
    }

    @Override
    public PageResponse<PostSummaryResponse> listPosts(Long categoryId, String q, Pageable pageable) {
        String pattern = null;
        if (q != null) {
            q = q.trim();
            if (!q.isEmpty()) {
                pattern = "%" + q + "%";
            }
        }

        Page<CommunityPost> page = postRepository.searchNative(categoryId, pattern, pageable);

        var content = page.getContent().stream().map(p -> {
            int commentCount = (int) commentRepository.countByPost(p);
            var firstImage = photoRepository.findTop1ByPostOrderBySortOrderAsc(p)
                    .stream().map(CommunityPhoto::getUrl).toList();
            return PostMapper.toSummary(p, p.getCategory().getCategoryName(), commentCount, firstImage);
        }).toList();

        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(),
                page.getNumber(), page.getSize(), page.hasNext());
    }

    public void deletePost(Long postId) {
        String loginUserId = getCurrentUserId();
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getUserId().equals(loginUserId)) {
            throw new AccessDeniedException("본인 게시글만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    @Override
    public Long addComment(Long postId, String content) {
        String loginUserId = getCurrentUserId();
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        var user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        var comment = new CommunityComment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);

        commentRepository.save(comment);

        return comment.getId();
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

    @Override
    public void updatePost(Long postId, PostUpdateRequest req) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        String loginUserId = getCurrentUserId();
        if (!post.getUser().getUserId().equals(loginUserId)) {
            throw new RuntimeException("본인 게시글만 수정할 수 있습니다.");
        }

        // ----- 1) 텍스트 업데이트 -----
        if (req.getTitle() != null) post.setTitle(req.getTitle());
        if (req.getContent() != null) post.setContent(req.getContent());
        if (req.getCategoryId() != null) {
            var category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
            post.setCategory(category);
        }

        // ----- 2) 이미지 처리 -----
        if (req.getKeepPhotoIds() != null || req.getNewImages() != null) {
            handleImagesUpdate(post, req);
        }
    }

    private void handleImagesUpdate(CommunityPost post, PostUpdateRequest req) {
        var existingPhotos = photoRepository.findAllByPostOrderBySortOrderAsc(post);

        var keepIds = req.getKeepPhotoIds();
        boolean hasKeepRule = (keepIds != null);

        List<CommunityPhoto> photosToKeep;
        if (hasKeepRule) {
            var keepIdSet = new java.util.HashSet<Long>(keepIds);
            photosToKeep = existingPhotos.stream()
                    .filter(p -> keepIdSet.contains(p.getId()))
                    .toList();
        } else {
            photosToKeep = existingPhotos;
        }

        if (hasKeepRule) {
            var toDelete = existingPhotos.stream()
                    .filter(p -> !photosToKeep.contains(p))
                    .toList();
            photoRepository.deleteAllInBatch(toDelete);
        }

        int order = 0;
        for (var photo : photosToKeep) {
            photo.setSortOrder(order++);
        }

        if (req.getNewImages() != null) {
            for (var file : req.getNewImages()) {
                if (file == null || file.isEmpty()) continue;

                var url = imageStoragePort.store(file);

                var photo = new CommunityPhoto();
                photo.setPost(post);
                photo.setUrl(url);
                photo.setSortOrder(order++);
                photoRepository.save(photo);
            }
        }
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        String loginUserId = getCurrentUserId();
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("게시글/댓글 정보가 일치하지 않습니다.");
        }

        if (!comment.getUser().getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<PostSummaryResponse> listHotPosts(int limit) {
        // 최소 1개는 나오도록 방어코드
        int size = Math.max(1, limit);

        Pageable pageable = PageRequest.of(0, size);
        var posts = postRepository.findHotPosts(pageable);

        // 기존 listPosts에서 쓰던 방식 그대로 재사용
        return posts.stream().map(p -> {
            int commentCount = (int) commentRepository.countByPost(p);
            var firstImage = photoRepository.findTop1ByPostOrderBySortOrderAsc(p)
                    .stream().map(CommunityPhoto::getUrl).toList();
            return PostMapper.toSummary(
                    p,
                    p.getCategory().getCategoryName(),
                    commentCount,
                    firstImage
            );
        }).toList();
    }

    class SecurityUtil {

        static String getCurrentUserId() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getPrincipal() == null) {
                throw new RuntimeException("인증되지 않은 사용자입니다.");
            }
            return auth.getPrincipal().toString();
        }
    }
}
