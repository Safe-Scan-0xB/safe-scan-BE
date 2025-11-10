package com.springboot.safescan.repository;

import com.springboot.safescan.domain.CommunityPhoto;
import com.springboot.safescan.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<CommunityPhoto, Long> {
    List<CommunityPhoto> findAllByPostOrderBySortOrderAsc(CommunityPost post);
    List<CommunityPhoto> findTop1ByPostOrderBySortOrderAsc(CommunityPost post);
}
