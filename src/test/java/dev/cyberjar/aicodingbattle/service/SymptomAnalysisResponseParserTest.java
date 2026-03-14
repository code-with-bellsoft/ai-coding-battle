package dev.cyberjar.aicodingbattle.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SymptomAnalysisResponseParserTest {

    private SymptomAnalysisResponseParser parser;

    @BeforeEach
    void setUp() {
        parser = new SymptomAnalysisResponseParser(new ObjectMapper());
    }

    @Test
    void parsesValidJsonArray() {
        String json = """
                [
                  {"name":"Flu", "certainty":72, "specialty":"General Medicine"},
                  {"name":"Common Cold", "certainty":"51%", "specialty":"General Medicine"}
                ]
                """;

        List<IllnessPredictionDto> predictions = parser.parseIllnessPredictions(json);

        assertThat(predictions).hasSize(2);
        assertThat(predictions.get(0).name()).isEqualTo("Flu");
        assertThat(predictions.get(0).certainty()).isEqualTo(72.0D);
        assertThat(predictions.get(1).certainty()).isEqualTo(51.0D);
    }

    @Test
    void returnsAtMostFivePredictions() {
        String json = """
                [
                  {"name":"A", "certainty":10, "specialty":"S1"},
                  {"name":"B", "certainty":20, "specialty":"S2"},
                  {"name":"C", "certainty":30, "specialty":"S3"},
                  {"name":"D", "certainty":40, "specialty":"S4"},
                  {"name":"E", "certainty":50, "specialty":"S5"},
                  {"name":"F", "certainty":60, "specialty":"S6"}
                ]
                """;

        List<IllnessPredictionDto> predictions = parser.parseIllnessPredictions(json);

        assertThat(predictions).hasSize(5);
        assertThat(predictions.get(4).name()).isEqualTo("E");
    }

    @Test
    void skipsInvalidPredictionItems() {
        String json = """
                [
                  {"name":"Flu", "certainty":72, "specialty":"General Medicine"},
                  {"name":"", "certainty":50, "specialty":"General Medicine"},
                  {"name":"Migraine", "certainty":120, "specialty":"Neurology"},
                  {"name":"Allergy", "certainty":40, "specialty":"Immunology"}
                ]
                """;

        List<IllnessPredictionDto> predictions = parser.parseIllnessPredictions(json);

        assertThat(predictions).hasSize(2);
        assertThat(predictions).extracting(IllnessPredictionDto::name).containsExactly("Flu", "Allergy");
    }

    @Test
    void returnsEmptyListForInvalidJson() {
        List<IllnessPredictionDto> predictions = parser.parseIllnessPredictions("not-json");

        assertThat(predictions).isEmpty();
    }
}
