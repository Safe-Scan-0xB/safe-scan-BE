package com.springboot.safescan.service.port;
import com.springboot.safescan.dto.SearchResponse;

public interface SearchService {
    SearchResponse searchUrl(String keyword);
}
