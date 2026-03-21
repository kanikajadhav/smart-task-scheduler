package com.taskscheduler.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GroqService {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_API_KEY = System.getenv("GROQ_API_KEY");

    public String predictPriority(String title, String description) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(GROQ_API_URL);
            request.setHeader("Authorization", "Bearer " + GROQ_API_KEY);
            request.setHeader("Content-Type", "application/json");

            String prompt = "You are a task priority classifier. Classify the priority of this task as HIGH, MEDIUM, or LOW.\n\n" +
                "Rules:\n" +
                "- HIGH: urgent, critical, production issues, deadlines today, server down, emergencies, fix bugs\n" +
                "- MEDIUM: important but not urgent, due in a few days, regular work tasks, meetings, reports\n" +
                "- LOW: personal tasks, leisure, eating, sleeping, hobbies, no deadline, minor improvements\n\n" +
                "Examples:\n" +
                "- 'Fix server crash' -> HIGH\n" +
                "- 'Submit report by Friday' -> MEDIUM\n" +
                "- 'Go for a walk' -> LOW\n" +
                "- 'Eat lunch' -> LOW\n" +
                "- 'Sleep' -> LOW\n\n" +
                "Reply with ONLY one word: HIGH, MEDIUM, or LOW.\n\n" +
                "Title: " + title + "\n" +
                "Description: " + description;

            JSONObject body = new JSONObject();
            body.put("model", "llama3-8b-8192");
            body.put("max_tokens", 10);

            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            body.put("messages", messages);

            request.setEntity(new StringEntity(body.toString()));

            StringBuilder response = new StringBuilder();
            try (var httpResponse = client.execute(request)) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            String priority = jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()
                    .toUpperCase();

            if (priority.contains("HIGH")) return "HIGH";
            if (priority.contains("LOW")) return "LOW";
            return "MEDIUM";

        } catch (Exception e) {
            System.out.println("Groq API error: " + e.getMessage());
            return "MEDIUM";
        }
    }
}