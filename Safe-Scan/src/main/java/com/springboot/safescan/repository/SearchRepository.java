package com.springboot.safescan.repository;

import com.springboot.safescan.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query("SELECT s FROM Search s WHERE s.phishing_url = :phishing_url")
    Optional<Search> findByPhishing_url(@Param("phishing_url") String phishing_url);
}


