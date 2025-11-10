package com.springboot.safescan.repository;

import com.springboot.safescan.domain.Category;
import com.springboot.safescan.domain.CommunityPost;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<CommunityPost, Long> {

    @Query("""
        select p from CommunityPost p
        where (:category is null or p.category = :category)
          and (:q is null or lower(p.title) like lower(concat('%', :q, '%'))
                      or lower(p.content) like lower(concat('%', :q, '%')))
        """)
    Page<CommunityPost> search(@Param("category") Category category,
                               @Param("q") String q,
                               Pageable pageable);
}
