package com.springboot.safescan.dto;

import java.util.List;

public record OpenAIChatRequest(
        String model,
        List<OpenAIMessage> messages
) {}
