package dev.cyberjar.aicodingbattle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SymptomAnalysisResponseParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SymptomAnalysisResponseParser.class);
    private static final int MAX_RESULTS = 5;

    private final ObjectMapper objectMapper;

    public SymptomAnalysisResponseParser() {
        this(new ObjectMapper());
    }

    SymptomAnalysisResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<IllnessPredictionDto> parseIllnessPredictions(String rawResponse) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return List.of();
        }

        try {
            JsonNode rootNode = this.objectMapper.readTree(rawResponse);
            if (!rootNode.isArray()) {
                return List.of();
            }

            List<IllnessPredictionDto> parsedPredictions = new ArrayList<>();
            for (JsonNode predictionNode : rootNode) {
                if (parsedPredictions.size() >= MAX_RESULTS) {
                    break;
                }

                IllnessPredictionDto prediction = parsePrediction(predictionNode);
                if (prediction != null) {
                    parsedPredictions.add(prediction);
                }
            }

            return List.copyOf(parsedPredictions);
        } catch (JsonProcessingException exception) {
            LOGGER.warn("Could not parse symptom analysis response as JSON: {}", exception.getOriginalMessage());
            return List.of();
        }
    }

    private IllnessPredictionDto parsePrediction(JsonNode predictionNode) {
        if (predictionNode == null || !predictionNode.isObject()) {
            return null;
        }

        String name = getTrimmedText(predictionNode.get("name"));
        String specialty = getTrimmedText(predictionNode.get("specialty"));
        Double certainty = parsePercentage(predictionNode.get("certainty"));

        if (name == null || specialty == null || certainty == null) {
            return null;
        }

        if (certainty < 0.0D || certainty > 100.0D) {
            return null;
        }

        return new IllnessPredictionDto(name, certainty, specialty);
    }

    private String getTrimmedText(JsonNode node) {
        if (node == null || !node.isValueNode()) {
            return null;
        }

        String value = node.asText();
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            return null;
        }

        return trimmedValue;
    }

    private Double parsePercentage(JsonNode node) {
        if (node == null) {
            return null;
        }

        if (node.isNumber()) {
            return node.asDouble();
        }

        if (!node.isTextual()) {
            return null;
        }

        String value = node.asText().trim().replace("%", "");
        if (value.isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
