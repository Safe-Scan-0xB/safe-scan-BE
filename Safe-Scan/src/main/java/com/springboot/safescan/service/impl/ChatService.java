package com.springboot.safescan.service.impl;

import com.springboot.safescan.domain.Chat;
import com.springboot.safescan.dto.*;
import com.springboot.safescan.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.springboot.safescan.prompt.SystemPromptProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient openAIwebClient;
    private final ChatRepository chatRepo;
    private final SystemPromptProvider systemPromptProvider;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");



    @Transactional
    public ChatResponse ask(String userId, ChatRequest req) {

        // 1) 24시간 지난 기록 삭제
        cleanUpOldChats();

        // 2) 현재 유저 메시지 DB 저장 (user)
        LocalDateTime now = LocalDateTime.now();
        Chat userMsg = Chat.builder()
                .userId(userId)
                .role("user")
                .content(req.content())
                .timestamp(now)
                .build();
        chatRepo.save(userMsg);

        // 3) 지난 24시간 히스토리 불러오기
        LocalDateTime from = now.minusHours(24);
        List<Chat> history = chatRepo.findByUserIdAndTimestampAfterOrderByTimestampAsc(userId, from);

        // 4) OpenAI messages 구성
        List<OpenAIMessage> messages = new ArrayList<>();

        // (1) 기본 시스템 프롬프트
        messages.add(new OpenAIMessage("system", systemPromptProvider.getSystemPrompt()));

        // (2) 기능 프롬프트 트리거
        String content = req.content();

        // 2-1) 위험도 평가 기능
        if (containsAny(content, "점수로", "몇 점인지", "위험도 평가")) {
            messages.add(new OpenAIMessage(
                    "system",
                    """
                    [위험도 평가 기능 지시문]
                    - 위험도를 0~10점 사이 숫자로 표현하고, 0은 전혀 위험하지 않음, 10은 매우 위험함으로 정의합니다.
                    - 점수와 함께 그 이유를 3가지 이상 bullet로 설명해주세요.
                    """
            ));
        }

        // 2-2) 요약 기능
        if (containsAny(content, "요약해줘", "짧게 정리")) {
            messages.add(new OpenAIMessage(
                    "system",
                    """
                    [요약 기능 지시문]
                    - 사용자가 붙여넣은 내용을 3줄 이내로 요약해주세요.
                    - 핵심 위험 포인트만 남기고, 불필요한 문장은 제거합니다.
                    """
            ));
        }

        // 2-3) 부모님용 쉬운 설명
        if (containsAny(content, "엄마한테 설명하듯", "비전문가에게 쉽게")) {
            messages.add(new OpenAIMessage(
                    "system",
                    """
                    [부모님용 쉬운 설명 지시문]
                    - 디지털/보안 지식이 거의 없는 60대도 이해할 수 있게 매우 쉽게 설명합니다.
                    - 전문 용어를 쓰면 바로 쉬운 말로 풀어서 말해주세요.
                    """
            ));
        }

        // (3) 지난 24시간 히스토리를 role 그대로 전달
        for (Chat chat : history) {
            messages.add(new OpenAIMessage(chat.getRole(), chat.getContent()));
        }

        // (4) 이번 유저 메시지 추가
        messages.add(new OpenAIMessage("user", req.content()));

        // 5) OpenAI 요청
        OpenAIChatRequest openAIRequest = new OpenAIChatRequest(
                "gpt-4o-mini",
                messages
        );

        OpenAIChatResponse openAIResponse = openAIwebClient.post()
                .uri("/chat/completions")
                .bodyValue(openAIRequest)
                .retrieve()
                .bodyToMono(OpenAIChatResponse.class)
                .block();

        String assistantContent = openAIResponse.choices()
                .get(0)
                .message()
                .content();

        LocalDateTime botTime = LocalDateTime.now();

        // 6) assistant 메시지도 DB에 저장
        Chat botMsg = Chat.builder()
                .userId(userId)
                .role("assistant")
                .content(assistantContent)
                .timestamp(botTime)
                .build();
        chatRepo.save(botMsg);

        // 7) 응답 DTO
        return new ChatResponse(
                "assistant",
                assistantContent,
                botTime.format(formatter)
        );
    }

    // 지난 24시간 대화 조회 (커서 기반)
    public ChatHistoryResponse getHistory(String userId, Long cursor, int size) {

        // 1) 24시간 지난 기록 삭제
        cleanUpOldChats();

        LocalDateTime from = LocalDateTime.now().minusHours(24);
        Pageable pageable = PageRequest.of(0, size);

        List<Chat> chats;

        // 첫 로드: 최신 size개
        if (cursor == null) {
            chats = chatRepo.findTop20ByUserIdOrderByIdDesc(userId, pageable);
        } else {
            // 이전 메시지 더 불러오기
            chats = chatRepo.findByUserIdAndIdLessThanOrderByIdDesc(
                    userId, cursor, pageable
            );
        }

        // 24시간 이내만 필터링 + 정방향 정렬
        List<Chat> filtered = chats.stream()
                .filter(c -> c.getTimestamp().isAfter(from))
                .sorted(Comparator.comparing(Chat::getId))
                .toList();

        // 다음 커서 (더 불러올 것이 있는지 판단에 사용)
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

    // 24시간 지난 모든 대화 삭제
    private void cleanUpOldChats() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        chatRepo.deleteByTimestampBefore(cutoff);
    }

    private boolean containsAny(String text, String... keywords) {
        if (text == null) return false;
        for (String k : keywords) {
            if (text.contains(k)) return true;
        }
        return false;
    }
}
