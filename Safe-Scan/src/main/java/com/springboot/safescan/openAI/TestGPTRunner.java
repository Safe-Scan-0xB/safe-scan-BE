package com.springboot.safescan.openAI;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

//@Component
@RequiredArgsConstructor
public class TestGPTRunner implements CommandLineRunner {
    private final WebClient openAiwebClient;

    @Override
    public void run(String... args) throws Exception {
        String reqJson = """
                {
                   "model" : "gpt-4o-mini",
                   "messages" : [
                      {"role" : "user", "content" : "안녕?" }
                   ]
                }""";

        String response = openAiwebClient.post()
                .uri("/chat/completions")
                .bodyValue(reqJson)
                .retrieve()
                .bodyToMono(String.class)
                .block();

                System.out.println("GPT 응답 = " + response);
    }
}
