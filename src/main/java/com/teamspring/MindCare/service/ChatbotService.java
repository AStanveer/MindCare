package com.teamspring.MindCare.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.teamspring.MindCare.dto.gemini.GeminiRequest;
import com.teamspring.MindCare.dto.gemini.GeminiResponse;

import jakarta.servlet.http.HttpSession;

@Service
public class ChatbotService {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getResponse(String userMessage, HttpSession session) {
        if (isCrisis(userMessage)) {
            return 
                "I'm hearing that you're in a lot of pain. Please, your life matters. " +
                "Contact these emergency resources immediately:\n" +
                "- Befrienders KL: 03-7627 2929\n" +
                "- Talian Kasih: 15999\n" +
                "(This is an automated safety response)";
        }

        List<GeminiRequest.Content> history = (List<GeminiRequest.Content>) session.getAttribute("chatHistory");
        if(history == null) {
            history = new ArrayList<>();
            String systemPrompt = "You are MindCare, a compassionate mental health support assistant. " +
                                "Keep answers brief, supportive, and safe. If the user mentions harm, provide emergency contacts.";
            history.add(new GeminiRequest.Content("user", systemPrompt)); 
            history.add(new GeminiRequest.Content("assistant", "Understood. I am ready to help."));
            session.setAttribute("chatHistory", history);
            }

        history.add(new GeminiRequest.Content("user", userMessage));

        if (history.size() > 20) { 
            history.subList(2, history.size() - 10).clear(); 
        }

        String botReply = callGemini(history);

        history.add(new GeminiRequest.Content("assistant", botReply));

        return botReply;
    }

    private boolean isCrisis(String message) {
        String lower = message.toLowerCase();
        return lower.contains("suicide") || 
               lower.contains("kill") || 
               lower.contains("hurt") || 
               lower.contains("die");
    }

    private String callGemini(List<GeminiRequest.Content> history) {
        try {
            String finalUrl = apiUrl + "?key=" + apiKey;

            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("You are a compassionate mental health support assistant named MindCare. ")
                         .append("Keep answers brief (under 50 words) and supportive. ");

            for (GeminiRequest.Content content : history) {
                promptBuilder.append(content.getRole()).append(" says: ")
                             .append(content.getParts().get(0).getText()).append("\n");
            }

            String prompt = promptBuilder.toString();

            System.out.println("Calling Gemini URL: " + apiUrl + "?key=" + (apiKey != null ? "FOUND" : "NULL"));

            GeminiRequest request = new GeminiRequest(prompt);

            GeminiResponse response = restTemplate.postForObject(finalUrl, request, GeminiResponse.class);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }

            return "I'm having trouble connecting to my thoughts right now. Please try again.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I am currently offline. Please try again later.";
        }
    }    
}


