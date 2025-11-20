package com.springboot.safescan.prompt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Getter
public class SystemPromptProvider {

    @Value("classpath:prompts/chatbot_system_prompt.md")
    private Resource promptResource;

    private String systemPrompt;

    @PostConstruct
    public void loadPrompt() throws IOException {
        byte[] bytes = promptResource.getInputStream().readAllBytes();
        this.systemPrompt = new String(bytes, StandardCharsets.UTF_8);
    }
}
