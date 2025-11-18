package com.springboot.safescan.dto;


import java.util.List;

public record ChatHistoryResponse(String userId, List<ChatResponse> content) {
}
