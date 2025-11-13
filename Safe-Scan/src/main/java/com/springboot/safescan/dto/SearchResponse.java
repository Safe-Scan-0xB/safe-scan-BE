package com.springboot.safescan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SearchResponse {
    private String phishing_url;
    private Long url_count;
    private String state;
}
