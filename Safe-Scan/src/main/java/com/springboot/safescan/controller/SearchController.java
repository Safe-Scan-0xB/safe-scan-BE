package com.springboot.safescan.controller;

import com.springboot.safescan.dto.SearchResponse;
import com.springboot.safescan.service.port.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/scan")
    public ResponseEntity<?> scanPhishing(@RequestParam(value = "url", required = false) String url) {

        // 1) URL 값이 아예 없거나 공백일 때
        if (url == null || url.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("msg", "url 입력 형식이 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // 2) URL이지만 형식이 이상한 경우 검증 (간단 버전)
        // "http://", "https://" 제외한 문자열에서도 최소 도메인 형태인지 검사
        if (!isValidUrl(url)) {
            Map<String, String> error = new HashMap<>();
            error.put("msg", "url 입력 형식이 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // 3) 정상 URL이면 서비스 호출
        SearchResponse result = searchService.searchUrl(url);
        return ResponseEntity.ok(result);
    }

    // URL 최소 형식 검증용 내부 함수
    private boolean isValidUrl(String url) {
        String normalized = url.trim().toLowerCase();

        // 프로토콜 제거
        normalized = normalized
                .replace("https://", "")
                .replace("http://", "")
                .trim();

        // 최소한 “aaa.bbb” 형태인지 검사
        return normalized.contains(".");
    }
}
