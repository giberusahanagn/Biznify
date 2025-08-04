// GeminiAiService.java
package com.aroha.callingAiAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aroha.callingAiAPI.dto.GeminiRequest;
import com.aroha.callingAiAPI.dto.GeminiResponse;

@Service("gemini")
public class GeminiAiService implements AiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Override
    public String askQuestion(String prompt) {
        GeminiRequest.Part part = new GeminiRequest.Part();
        part.setText(prompt);

        GeminiRequest.Content content = new GeminiRequest.Content();
        content.setParts(List.of(part));

        GeminiRequest request = new GeminiRequest();
        request.setContents(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, entity, GeminiResponse.class);

        return response.getBody()
                .getCandidates()
                .stream()
                .findFirst()
                .map(c -> c.getContent().getParts().stream()
                        .findFirst()
                        .map(GeminiRequest.Part::getText)
                        .orElse("No part"))
                .orElse("No candidate");
    }
}