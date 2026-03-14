package dev.cyberjar.aicodingbattle.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SymptomAnalysisServiceIntegrationTest {

    private final SymptomAnalysisResponseParser responseParser = new SymptomAnalysisResponseParser(new ObjectMapper());

    @Test
    void parseIllnessPredictionsFromJsonResponse() {
        String jsonResponse = """
                [
                  {"name": "Common Cold", "certainty": 85, "specialty": "General Medicine"},
                  {"name": "Influenza", "certainty": 65, "specialty": "General Medicine"}
                ]
                """;

        List<IllnessPredictionDto> predictions = responseParser.parseIllnessPredictions(jsonResponse);

        assertThat(predictions).hasSize(2);
        assertThat(predictions.get(0).name()).isEqualTo("Common Cold");
        assertThat(predictions.get(0).certainty()).isEqualTo(85.0D);
        assertThat(predictions.get(1).name()).isEqualTo("Influenza");
    }

    @Test 
    void boundsCertaintyToValidRange() {
        String jsonResponse = """
                [
                  {"name": "Illness A", "certainty": 150, "specialty": "Spec A"},
                  {"name": "Illness B", "certainty": -10, "specialty": "Spec B"}
                ]
                """;

        List<IllnessPredictionDto> predictions = responseParser.parseIllnessPredictions(jsonResponse);

        assertThat(predictions).isEmpty();
    }

    @Test
    void capsAtFivePredictions() {
        String jsonResponse = """
                [
                  {"name": "A", "certainty": 10, "specialty": "S1"},
                  {"name": "B", "certainty": 20, "specialty": "S2"},
                  {"name": "C", "certainty": 30, "specialty": "S3"},
                  {"name": "D", "certainty": 40, "specialty": "S4"},
                  {"name": "E", "certainty": 50, "specialty": "S5"},
                  {"name": "F", "certainty": 60, "specialty": "S6"}
                ]
                """;

        List<IllnessPredictionDto> predictions = responseParser.parseIllnessPredictions(jsonResponse);

        assertThat(predictions).hasSize(5);
        assertThat(predictions).extracting(IllnessPredictionDto::name).containsExactly("A", "B", "C", "D", "E");
    }

    @Test
    void handlesInvalidJsonResponse() {
        String invalidJson = "not-json";
        List<IllnessPredictionDto> predictions = responseParser.parseIllnessPredictions(invalidJson);
        assertThat(predictions).isEmpty();
    }
}
