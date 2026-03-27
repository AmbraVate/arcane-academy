package com.arcane.academy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class AiMentorService {

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${anthropic.api-url}")
    private String apiUrl;

    @Value("${anthropic.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getFeedback(String questTitle, String topic, String problem,
                               String code, String failedTests) {
        if (apiKey == null || apiKey.isBlank()) {
            return "Master Velan seems distracted... (AI feedback unavailable — set ANTHROPIC_API_KEY)";
        }

        String prompt = buildPrompt(questTitle, topic, problem, code, failedTests);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", model);
            body.put("max_tokens", 300);
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<?> content = (List<?>) response.getBody().get("content");
                if (content != null && !content.isEmpty()) {
                    Map<?, ?> first = (Map<?, ?>) content.get(0);
                    return (String) first.get("text");
                }
            }
        } catch (Exception e) {
            log.error("Error calling Anthropic API", e);
        }

        return "Master Velan strokes his beard. \"Examine your logic closely, apprentice. The answer lies within your spell.\"";
    }

    private String buildPrompt(String questTitle, String topic, String problem,
                                String code, String failedTests) {
        return """
            You are Master Velan, a wise and encouraging wizard mentor in a Java learning RPG called Arcane Academy.
            
            Quest: "%s" (topic: %s)
            Problem: %s
            
            Student's code:
            ```java
            %s
            ```
            
            Failed test cases: %s
            
            Give short, helpful feedback (3-4 sentences) in character as Master Velan. \
            Use wizard/magic metaphors naturally. Identify the specific mistake or gap. \
            Do NOT give the full answer — guide with a question or pointed hint. Be warm and encouraging.
            """.formatted(questTitle, topic, problem, code, failedTests);
    }
}
