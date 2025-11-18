package com.springboot.safescan.controller;

import com.springboot.safescan.dto.ChatRequest;
import com.springboot.safescan.dto.ChatResponse;
import com.springboot.safescan.dto.ChatHistoryResponse;
import com.springboot.safescan.service.impl.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 1) 지난 24시간 대화 조회
    @GetMapping
    public ChatHistoryResponse getHistory(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return chatService.getHistory(userId);
    }

    // 2) 챗봇에게 질문 → 답변 생성
    @PostMapping
    public ChatResponse ask(
            @RequestBody ChatRequest req,
            Authentication auth
    ) {
        String userId = (String) auth.getPrincipal();
        return chatService.ask(userId, req);
    }
}
