package dev.cyberjar.aicodingbattle.ui;

import dev.cyberjar.aicodingbattle.TestcontainersConfiguration;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TelemedUiFlowTest {

    @Autowired
    private TelemedUiService telemedUiService;

    @Test
    void buildShareTextFormatsIllnessAndAdvice() {
        IllnessPredictionDto prediction = new IllnessPredictionDto("Common Cold", 85.0D, "General Medicine");
        String advice = "- Rest well\n- Stay hydrated";

        String shareText = telemedUiService.buildShareText(prediction, advice);

        assertThat(shareText).contains("Common Cold");
        assertThat(shareText).contains("85");
        assertThat(shareText).contains("General Medicine");
        assertThat(shareText).contains("Rest well");
    }

    @Test
    void shareTextIncludesCertaintyPercentage() {
        IllnessPredictionDto highCertainty = new IllnessPredictionDto("Flu", 92.5D, "Internal Medicine");
        IllnessPredictionDto lowCertainty = new IllnessPredictionDto("Allergy", 45.0D, "Immunology");
        String advice = "- Consult doctor";

        String highShare = telemedUiService.buildShareText(highCertainty, advice);  
        String lowShare = telemedUiService.buildShareText(lowCertainty, advice);

        assertThat(highShare).contains("93%");
        assertThat(lowShare).contains("45%");
    }

    @Test
    void diseaseGuidanceFormatting() {
        IllnessPredictionDto prediction = new IllnessPredictionDto("Migraine", 70.0D, "Neurology");
        String multiLineAdvice = "- Dark quiet room\n- Hydration\n- Pain relief";

        String shareText = telemedUiService.buildShareText(prediction, multiLineAdvice);

        assertThat(shareText).contains("Dark quiet room");
        assertThat(shareText).contains("Neurology");
    }
}
