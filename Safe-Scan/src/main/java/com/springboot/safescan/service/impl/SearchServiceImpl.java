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

    @Override
    public SearchResponse searchUrl(String targetUrl) {

        String requestUrl = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("returnType", "JSON")
                .queryParam("numOfRows", 9999)
                .toUriString();

        // 공공데이터포털 API JSON 구조에 맞는 record 정의
        record Item(@JsonProperty("홈페이지주소") String homepage) {}
        record Items(@JsonProperty("item") List<Item> item) {}
        record Body(@JsonProperty("items") Items items) {}
        record Response(@JsonProperty("body") Body body) {}
        record ApiResponse(@JsonProperty("response") Response response) {}

        ApiResponse response = restTemplate.getForObject(requestUrl, ApiResponse.class);

        if (response == null ||
                response.response() == null ||
                response.response().body() == null ||
                response.response().body().items() == null ||
                response.response().body().items().item() == null) {

            return SearchResponse.builder()
                    .phishing_url(targetUrl)
                    .url_count(0L)
                    .state("안전")
                    .build();
        }

        List<Item> items = response.response().body().items().item();

        long count = items.stream()
                .filter(item -> {
                    if (item.homepage() == null) return false;

                    String homepage = item.homepage()
                            .replaceAll("\\s+", "")
                            .replace("https://", "")
                            .replace("http://", "")
                            .trim()
                            .toLowerCase();

                    String normalizedTarget = targetUrl
                            .replaceAll("\\s+", "")
                            .replace("https://", "")
                            .replace("http://", "")
                            .trim()
                            .toLowerCase();

                    return homepage.contains(normalizedTarget);
                })
                .count();

        String state = count > 0 ? "위험" : "안전";

        return SearchResponse.builder()
                .phishing_url(targetUrl)
                .url_count(count)
                .state(state)
                .build();
    }
}
