package dev.cyberjar.aicodingbattle.ui;

import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.service.AdviceService;
import dev.cyberjar.aicodingbattle.service.DoctorLocatorService;
import dev.cyberjar.aicodingbattle.service.SymptomAnalysisService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemedUiService {

    private final SymptomAnalysisService symptomAnalysisService;
    private final AdviceService adviceService;
    private final DoctorLocatorService doctorLocatorService;

    public TelemedUiService(SymptomAnalysisService symptomAnalysisService,
                            AdviceService adviceService,
                            DoctorLocatorService doctorLocatorService) {
        this.symptomAnalysisService = symptomAnalysisService;
        this.adviceService = adviceService;
        this.doctorLocatorService = doctorLocatorService;
    }

    public List<IllnessPredictionDto> analyzeSymptoms(String symptoms) {
        return this.symptomAnalysisService.analyzeSymptoms(symptoms);
    }

    public String generateAdvice(String illnessName) {
        return this.adviceService.generateAdvice(illnessName);
    }

    public List<NearbyDoctorDto> findNearbyDoctors(double latitude, double longitude, String specialty) {
        return this.doctorLocatorService.findNearbyDoctors(latitude, longitude, specialty);
    }

    public String buildShareText(IllnessPredictionDto selectedPrediction, String advice) {
        if (selectedPrediction == null || advice == null || advice.isBlank()) {
            return "No shareable recommendation available yet.";
        }

        return "Potential illness: " + selectedPrediction.name() + System.lineSeparator()
                + "Suggested specialty: " + selectedPrediction.specialty() + System.lineSeparator()
                + "Certainty: " + Math.round(selectedPrediction.certainty()) + "%" + System.lineSeparator()
                + System.lineSeparator()
                + "Advice:" + System.lineSeparator()
                + advice;
    }
}
