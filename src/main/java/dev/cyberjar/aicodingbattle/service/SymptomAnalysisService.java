package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.model.Illness;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SymptomAnalysisService {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public SymptomAnalysisService(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.objectMapper = new ObjectMapper();
    }

    public List<Illness> analyzeSymptoms(String symptoms) {
        String prompt = String.format(
                "Analyze the following symptoms and provide a JSON list of up to 5 possible illnesses with certainty percentages (0-100) and the medical specialty that should be consulted.\n\n" +
                "Symptoms: %s\n\n" +
                "Return ONLY a valid JSON array with no additional text, in this exact format:\n" +
                "[\n" +
                "  {\"name\": \"illness name\", \"certainty\": 85, \"specialty\": \"medical specialty\"},\n" +
                "  {\"name\": \"illness name\", \"certainty\": 70, \"specialty\": \"medical specialty\"}\n" +
                "]\n\n" +
                "Ensure the JSON is valid and properly formatted.",
                symptoms
        );

        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        Prompt p = promptTemplate.create();
        String response = chatModel.call(p).getResult().getOutput().getText();

        try {
            // Remove markdown code blocks if present
            String cleanResponse = response.replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();
            
            Illness[] illnesses = objectMapper.readValue(cleanResponse, Illness[].class);
            return Arrays.stream(illnesses)
                    .limit(5)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response: " + e.getMessage(), e);
        }
    }
}
