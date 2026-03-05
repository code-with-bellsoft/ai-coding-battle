package dev.cyberjar.aicodingbattle.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IllnessJsonParsingTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testParsingIllnessJson() throws Exception {
        String json = "[{\"name\": \"Flu\", \"certainty\": 85, \"specialty\": \"General Practitioner\"}]";

        Illness[] illnesses = objectMapper.readValue(json, Illness[].class);
        assertEquals(1, illnesses.length);
        assertEquals("Flu", illnesses[0].getName());
        assertEquals(85.0, illnesses[0].getCertainty());
        assertEquals("General Practitioner", illnesses[0].getSpecialty());
    }

    @Test
    void testParsingMultipleIllnesses() throws Exception {
        String json = "[" +
                "{\"name\": \"Flu\", \"certainty\": 85, \"specialty\": \"General Practitioner\"}," +
                "{\"name\": \"Cold\", \"certainty\": 70, \"specialty\": \"General Practitioner\"}," +
                "{\"name\": \"Pneumonia\", \"certainty\": 50, \"specialty\": \"Pulmonologist\"}" +
                "]";

        Illness[] illnesses = objectMapper.readValue(json, Illness[].class);
        assertEquals(3, illnesses.length);
        assertEquals("Pneumonia", illnesses[2].getName());
        assertEquals(50.0, illnesses[2].getCertainty());
    }

    @Test
    void testIllnessConstructor() {
        Illness illness = new Illness("Migraine", 75.5, "Neurologist");
        assertEquals("Migraine", illness.getName());
        assertEquals(75.5, illness.getCertainty());
        assertEquals("Neurologist", illness.getSpecialty());
    }

    @Test
    void testIllnessSetters() {
        Illness illness = new Illness();
        illness.setName("Asthma");
        illness.setCertainty(60.0);
        illness.setSpecialty("Pulmonologist");

        assertEquals("Asthma", illness.getName());
        assertEquals(60.0, illness.getCertainty());
        assertEquals("Pulmonologist", illness.getSpecialty());
    }
}
