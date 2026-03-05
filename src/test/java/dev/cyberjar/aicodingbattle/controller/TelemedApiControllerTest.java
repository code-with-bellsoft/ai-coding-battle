package dev.cyberjar.aicodingbattle.controller;

import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.service.AdviceService;
import dev.cyberjar.aicodingbattle.service.DoctorLocatorService;
import dev.cyberjar.aicodingbattle.service.SymptomAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TelemedApiController.class)
class TelemedApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SymptomAnalysisService symptomAnalysisService;

    @MockitoBean
    private AdviceService adviceService;

    @MockitoBean
    private DoctorLocatorService doctorLocatorService;

    @Test
    void analyzeSymptomsReturnsPredictionList() throws Exception {
        List<IllnessPredictionDto> predictions = List.of(
                new IllnessPredictionDto("Flu", 67.5D, "General Medicine")
        );
        when(symptomAnalysisService.analyzeSymptoms("fever and cough")).thenReturn(predictions);

        mockMvc.perform(post("/api/analyze-symptoms")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"symptoms":"fever and cough"}
                    """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Flu"))
                .andExpect(jsonPath("$[0].certainty").value(67.5D))
                .andExpect(jsonPath("$[0].specialty").value("General Medicine"));

        verify(symptomAnalysisService).analyzeSymptoms("fever and cough");
    }

    @Test
    void analyzeSymptomsRejectsBlankInput() throws Exception {
        mockMvc.perform(post("/api/analyze-symptoms")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"symptoms":"   "}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void adviceReturnsPlainText() throws Exception {
        when(adviceService.generateAdvice("Flu")).thenReturn("- Rest\n- Hydrate");

        mockMvc.perform(get("/api/advice/{illness}", "Flu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("- Rest\n- Hydrate"));

        verify(adviceService).generateAdvice("Flu");
    }

    @Test
    void nearbyDoctorsReturnsList() throws Exception {
        List<NearbyDoctorDto> doctors = List.of(
                new NearbyDoctorDto("Jane", "Doe", "Cardiology", "Main Street 1", 1.2D),
                new NearbyDoctorDto("John", "Smith", "Cardiology", "Second Street 2", 2.4D)
        );
        when(doctorLocatorService.findNearbyDoctors(48.13D, 11.57D, "Cardiology")).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/nearby")
                        .queryParam("lat", "48.13")
                        .queryParam("lon", "11.57")
                        .queryParam("specialty", "Cardiology"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].distanceInKilometers").value(1.2D));

        verify(doctorLocatorService).findNearbyDoctors(48.13D, 11.57D, "Cardiology");
    }

    @Test
    void nearbyDoctorsRejectsInvalidLatitude() throws Exception {
        mockMvc.perform(get("/api/doctors/nearby")
                        .queryParam("lat", "120")
                        .queryParam("lon", "11.57")
                        .queryParam("specialty", "Cardiology"))
                .andExpect(status().isBadRequest());
    }
}
