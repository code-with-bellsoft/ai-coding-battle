package dev.cyberjar.aicodingbattle.api;

import dev.cyberjar.aicodingbattle.model.Illness;
import dev.cyberjar.aicodingbattle.service.SymptomAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SymptomAnalysisController {

    private final SymptomAnalysisService symptomAnalysisService;

    public SymptomAnalysisController(SymptomAnalysisService symptomAnalysisService) {
        this.symptomAnalysisService = symptomAnalysisService;
    }

    @PostMapping("/analyze-symptoms")
    public ResponseEntity<List<Illness>> analyzeSymptoms(@RequestParam String symptoms) {
        List<Illness> illnesses = symptomAnalysisService.analyzeSymptoms(symptoms);
        return ResponseEntity.ok(illnesses);
    }
}
