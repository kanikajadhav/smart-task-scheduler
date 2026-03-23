package com.taskscheduler.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GroqService {

    @Value("${ai.priority.service.url}")
    private String aiPriorityServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String predictPriority(String title, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = Map.of(
                "title", title,
                "description", description
            );

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                aiPriorityServiceUrl,
                request,
                String.class
            );

            String priority = response.getBody();
            if (priority != null) priority = priority.trim().toUpperCase();

            if ("HIGH".equals(priority) || "MEDIUM".equals(priority) || "LOW".equals(priority)) {
                return priority;
            }
            return "MEDIUM";

        } catch (Exception e) {
            System.out.println("AI Priority Service error: " + e.getMessage());
            return "MEDIUM";
        }
    }
}