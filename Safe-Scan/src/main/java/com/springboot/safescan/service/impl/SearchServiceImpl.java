package com.springboot.safescan.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.safescan.dto.SearchResponse;
import com.springboot.safescan.service.port.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RestTemplate restTemplate;

    @Value("${phishing.api.url}")
    private String apiUrl;

    @Value("${phishing.api.key}")
    private String apiKey;

    // ===== JSON 구조 =====
    record Item(
            @JsonProperty("날짜") String date,
            @JsonProperty("홈페이지주소") String homepage
    ) {}

    record ApiResponse(
            @JsonProperty("data") List<Item> data
    ) {}

    @Override
    public SearchResponse searchUrl(String targetUrl) {

        // -------- 요청 URL 생성 --------
        String requestUrl = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("page", 1)
                .queryParam("perPage", 10000)
                .toUriString();

        // -------- 디버그용 출력 --------
        System.out.println("=== KISA API Request URL ===");
        System.out.println(requestUrl);

        // -------- API 호출 --------
        ApiResponse response = restTemplate.getForObject(requestUrl, ApiResponse.class);

        if (response == null || response.data() == null) {
            return SearchResponse.builder()
                    .phishing_url(targetUrl)
                    .url_count(0L)
                    .state("안전")
                    .build();
        }

        List<Item> items = response.data();

        // 타겟 URL 정규화
        String normalizedTarget = normalize(targetUrl);

        long count = items.stream()
                .filter(item -> item.homepage() != null &&
                        normalize(item.homepage()).contains(normalizedTarget))
                .count();

        String state = count > 0 ? "위험" : "안전";

        return SearchResponse.builder()
                .phishing_url(targetUrl)
                .url_count(count)
                .state(state)
                .build();
    }

    private String normalize(String url) {
        return url.replaceAll("\\s+", "")
                .replace("https://", "")
                .replace("http://", "")
                .trim()
                .toLowerCase();
    }
}
