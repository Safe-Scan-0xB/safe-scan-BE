package com.springboot.safescan.repository;

import com.springboot.safescan.domain.Category;
import com.springboot.safescan.domain.CommunityPost;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<CommunityPost, Long> {

    @Query(
            value = """
        SELECT *
        FROM community_posts p
        WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
          AND (
            :pattern IS NULL
            OR p.title   ILIKE :pattern
            OR p.content ILIKE :pattern
          )
        ORDER BY p.created_at DESC
      """,
            countQuery = """
        SELECT count(*)
        FROM community_posts p
        WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
          AND (
            :pattern IS NULL
            OR p.title   ILIKE :pattern
            OR p.content ILIKE :pattern
          )
      """,
            nativeQuery = true
    )
    Page<CommunityPost> searchNative(@Param("categoryId") Long categoryId,
                                     @Param("pattern") String pattern,
                                     Pageable pageable);
}
