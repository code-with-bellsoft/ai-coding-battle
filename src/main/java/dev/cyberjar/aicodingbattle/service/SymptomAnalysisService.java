package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SymptomAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SymptomAnalysisService.class);

    private final ChatClient chatClient;
    private final SymptomAnalysisResponseParser responseParser;

    public SymptomAnalysisService(ChatClient.Builder chatClientBuilder,
                                  SymptomAnalysisResponseParser responseParser) {
        this.chatClient = chatClientBuilder.build();
        this.responseParser = responseParser;
    }

    public List<IllnessPredictionDto> analyzeSymptoms(String symptoms) {
        if (symptoms == null || symptoms.isBlank()) {
            return List.of();
        }

        String userPrompt = """
            You are a medical triage assistant.
            Classify the symptoms into at most 5 likely illnesses.
            Return strictly valid JSON only as an array with this format:
            [
              {
                "name": "Illness",
                "certainty": 65,
                "specialty": "medical specialty"
              }
            ]
            Use certainty as a number from 0 to 100.
            Do not include markdown, comments, or additional text.

            Symptoms:
            %s
            """.formatted(symptoms.trim());

        try {
            String aiResponse = this.chatClient
                    .prompt()
                    .user(userPrompt)
                    .call()
                    .content();

            return this.responseParser.parseIllnessPredictions(aiResponse);
        } catch (RuntimeException exception) {
            LOGGER.warn("Symptom analysis failed", exception);
            return List.of();
        }
    }
}
