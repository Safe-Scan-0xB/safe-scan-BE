package com.springboot.safescan.service.impl;

import com.springboot.safescan.domain.Chat;
import com.springboot.safescan.dto.ChatHistoryResponse;
import com.springboot.safescan.dto.ChatRequest;
import com.springboot.safescan.dto.ChatResponse;
import com.springboot.safescan.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ChatHistoryResponse getHistory(String userId) {

        LocalDateTime from = LocalDateTime.now().minusHours(24);

        List<Chat> chats = chatRepo.findByUserIdAndTimestampAfterOrderByTimestampAsc(
                userId, from
        );

        List<ChatResponse> responses = chats.stream()
                .map(c -> new ChatResponse(
                        c.getRole(),
                        c.getContent(),
                        c.getTimestamp().format(DateTimeFormatter.ofPattern("a h:mm"))
                ))
                .toList();

        return new ChatHistoryResponse(userId, responses);
    }
}
