package dev.cyberjar.aicodingbattle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdviceService.class);
    private static final int MAX_BULLETS = 5;
    private static final String DEFAULT_ADVICE = "- Please consult a medical professional for personalized advice.";

    private final ChatClient chatClient;

    public AdviceService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateAdvice(String illnessName) {
        if (illnessName == null || illnessName.isBlank()) {
            return DEFAULT_ADVICE;
        }

        String userPrompt = """
            Provide short and general self-care guidance for %s.
            Return only a plain bullet list with 3 to 5 bullets.
            Every line must start with "- " and be concise.
            Do not add a title, disclaimer block, or prose paragraph.
            """.formatted(illnessName.trim());

        try {
            String aiResponse = this.chatClient
                    .prompt()
                    .user(userPrompt)
                    .call()
                    .content();

            return normalizeBulletList(aiResponse);
        } catch (RuntimeException exception) {
            LOGGER.warn("Advice generation failed", exception);
            return DEFAULT_ADVICE;
        }
    }

    private String normalizeBulletList(String aiResponse) {
        if (aiResponse == null || aiResponse.isBlank()) {
            return DEFAULT_ADVICE;
        }

        String[] rawLines = aiResponse.split("\\R");
        List<String> bulletLines = new ArrayList<>();
        for (String rawLine : rawLines) {
            if (bulletLines.size() >= MAX_BULLETS) {
                break;
            }

            String line = rawLine == null ? "" : rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("- ")) {
                bulletLines.add(line);
                continue;
            }

            if (line.startsWith("* ")) {
                bulletLines.add("- " + line.substring(2).trim());
                continue;
            }

            bulletLines.add("- " + line);
        }

        if (bulletLines.isEmpty()) {
            return DEFAULT_ADVICE;
        }

        return String.join(System.lineSeparator(), bulletLines);
    }
}
