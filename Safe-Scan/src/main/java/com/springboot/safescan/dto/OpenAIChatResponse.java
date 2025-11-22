package com.springboot.safescan.dto;

import java.util.List;

public record OpenAIChatResponse(
        List<Choice> choices
) {
    public record Choice(OpenAIMessage message) {}
}