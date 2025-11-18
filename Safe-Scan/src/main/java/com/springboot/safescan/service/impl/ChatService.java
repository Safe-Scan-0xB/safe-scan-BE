package com.springboot.safescan.service.impl;

import com.springboot.safescan.domain.Chat;
import com.springboot.safescan.dto.ChatHistoryResponse;
import com.springboot.safescan.dto.ChatRequest;
import com.springboot.safescan.dto.ChatResponse;
import com.springboot.safescan.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient openAIwebClient;
    private final ChatRepository chatRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");

    public ChatResponse ask(String userId, ChatRequest req) {

        System.out.println("ENV KEY = [" + System.getenv("OPENAI_API_KEY") + "]");

        Chat userMsg = Chat.builder()
                .userId(userId)
                .role("user")
                .content(req.content())
                .timestamp(LocalDateTime.now())
                .build();
        chatRepo.save(userMsg);

        String reqJson = """
                {
                  "model": "gpt-4o-mini",
                  "messages": [
                    { "role": "user", "content": "%s" }
                  ]
                }
                """.formatted(req.content());


        String openAIResponse = openAIwebClient.post()
                .uri("/chat/completions")
                .bodyValue(reqJson)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        String assistantContent = extractContent(openAIResponse);
        LocalDateTime now = LocalDateTime.now();

        Chat botMsg = Chat.builder()
                .userId(userId)
                .role("assistant")
                .content(assistantContent)
                .timestamp(now)
                .build();
        chatRepo.save(botMsg);

        return new ChatResponse(
                "assistant",
                assistantContent,
                now.format(formatter)
        );
    }

    private String extractContent(String json) {
        int idx = json.indexOf("\"content\"");
        int first = json.indexOf("\"", idx + 10);
        int second = json.indexOf("\"", first + 1);
        return json.substring(first + 1, second);
    }

<<<<<<< HEAD
    public ChatHistoryResponse getHistory(String userId, Long cursor, int size) {

        LocalDateTime from = LocalDateTime.now().minusHours(24);
        Pageable pageable = PageRequest.of(0, size);

        List<Chat> chats;

        // 첫 로드: 최신 size개
        if (cursor == null) {
            chats = chatRepo.findTop20ByUserIdOrderByIdDesc(userId);
        }
        else {
            chats = chatRepo.findByUserIdAndIdLessThanOrderByIdDesc(
                    userId, cursor, pageable
            );
        }

        // 24시간 필터 적용
        List<Chat> filtered = chats.stream()
                .filter(c -> c.getTimestamp().isAfter(from))
                .sorted(Comparator.comparing(Chat::getId))
                .toList();

        // 다음 커서 계산
        Long nextCursor = chats.isEmpty()
                ? null
                : chats.get(chats.size() - 1).getId();

        List<ChatResponse> responses = filtered.stream()
                .map(c -> new ChatResponse(
                        c.getRole(),
                        c.getContent(),
                        c.getTimestamp().format(formatter)
                ))
                .toList();

        return new ChatHistoryResponse(
                userId,
                responses,
                nextCursor
        );
    }
}
