package com.springboot.safescan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhishingApiResponse {
    private String phishing_url;
    private Long url_count;
}
