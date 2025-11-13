package com.springboot.safescan.repository;

import com.springboot.safescan.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SearchRepository {
    //피싱 URL이 DB에 존재하는지 확인
    Optional<Search> findByPhishing_url(String phishing_url);

}
