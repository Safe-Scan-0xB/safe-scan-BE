package com.springboot.safescan.repository;

import com.springboot.safescan.domain.CommunityComment;
import com.springboot.safescan.domain.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommunityComment, Long> {
    long countByPost(CommunityPost post);
    Page<CommunityComment> findByPost(CommunityPost post, Pageable pageable);
    List<CommunityComment> findTop20ByPostOrderByIdDesc(CommunityPost post);
}