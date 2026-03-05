package dev.cyberjar.aicodingbattle.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdviceService {

    private final ChatModel chatModel;

    public AdviceService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public List<String> generateAdvice(String illness) {
        String prompt = String.format(
                "Provide short, practical medical advice for someone who may have %s. " +
                "Return as a bullet list (5-7 items), each item starting with '- '. " +
                "Be concise and clear.\n\n" +
                "Illness: %s",
                illness, illness
        );

        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        Prompt p = promptTemplate.create();
        String response = chatModel.call(p).getResult().getOutput().getText();

        return Arrays.stream(response.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> line.startsWith("- ") || line.startsWith("* "))
                .map(line -> line.replaceFirst("^[\\-\\*]\\s*", ""))
                .toList();
    }
}
