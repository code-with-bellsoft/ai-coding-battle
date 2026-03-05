package dev.cyberjar.aicodingbattle.controller;

import dev.cyberjar.aicodingbattle.dto.AnalyzeSymptomsRequestDto;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorsQueryDto;
import dev.cyberjar.aicodingbattle.service.AdviceService;
import dev.cyberjar.aicodingbattle.service.DoctorLocatorService;
import dev.cyberjar.aicodingbattle.service.SymptomAnalysisService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api")
public class TelemedApiController {

    private final SymptomAnalysisService symptomAnalysisService;
    private final AdviceService adviceService;
    private final DoctorLocatorService doctorLocatorService;

    public TelemedApiController(SymptomAnalysisService symptomAnalysisService,
                                AdviceService adviceService,
                                DoctorLocatorService doctorLocatorService) {
        this.symptomAnalysisService = symptomAnalysisService;
        this.adviceService = adviceService;
        this.doctorLocatorService = doctorLocatorService;
    }

    @PostMapping(value = "/analyze-symptoms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IllnessPredictionDto> analyzeSymptoms(@Valid @RequestBody AnalyzeSymptomsRequestDto request) {
        return this.symptomAnalysisService.analyzeSymptoms(request.symptoms());
    }

    @GetMapping(value = "/advice/{illness}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String advice(@PathVariable
                         @NotBlank(message = "illness must not be blank")
                         @Size(max = 150, message = "illness must not exceed 150 characters")
                         String illness) {
        return this.adviceService.generateAdvice(illness);
    }

    @GetMapping(value = "/doctors/nearby", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NearbyDoctorDto> nearbyDoctors(@Valid NearbyDoctorsQueryDto query) {
        return this.doctorLocatorService.findNearbyDoctors(query.getLat(), query.getLon(), query.getSpecialty());
    }
}
