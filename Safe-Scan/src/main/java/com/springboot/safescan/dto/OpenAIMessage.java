package com.springboot.safescan.dto;

public record OpenAIMessage(
        String role,
        String content
) {}
