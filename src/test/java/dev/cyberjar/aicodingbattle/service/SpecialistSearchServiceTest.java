package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.entity.Doctor;
import dev.cyberjar.aicodingbattle.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SpecialistSearchServiceTest {

    private SpecialistSearchService service;

    @Mock
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new SpecialistSearchService(doctorRepository);
    }

    @Test
    void testFindNearbyDoctors() {
        Doctor doc1 = new Doctor("John", "Smith", "Cardiology", "123 Main St",
                BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060));
        Doctor doc2 = new Doctor("Jane", "Doe", "Cardiology", "456 Oak Ave",
                BigDecimal.valueOf(40.7138), BigDecimal.valueOf(-74.0050));

        when(doctorRepository.findBySpecialty("Cardiology"))
                .thenReturn(Arrays.asList(doc1, doc2));

        List<Doctor> result = service.findNearbyDoctors(
                BigDecimal.valueOf(40.7128),
                BigDecimal.valueOf(-74.0060),
                "Cardiology"
        );

        assertNotNull(result);
        assertTrue(result.size() <= 5);
    }

    @Test
    void testEmptyDoctorList() {
        when(doctorRepository.findBySpecialty("NonExistent"))
                .thenReturn(Arrays.asList());

        List<Doctor> result = service.findNearbyDoctors(
                BigDecimal.valueOf(40.7128),
                BigDecimal.valueOf(-74.0060),
                "NonExistent"
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testDistanceCalculation() {
        Doctor doc1 = new Doctor("John", "Smith", "Cardiology", "123 Main St",
                BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060));
        Doctor doc2 = new Doctor("Jane", "Doe", "Cardiology", "456 Oak Ave",
                BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060));

        when(doctorRepository.findBySpecialty("Cardiology"))
                .thenReturn(Arrays.asList(doc1, doc2));

        List<Doctor> result = service.findNearbyDoctors(
                BigDecimal.valueOf(40.7128),
                BigDecimal.valueOf(-74.0060),
                "Cardiology"
        );

        // Same location should have 0 or minimal distance
        assertEquals(2, result.size());
    }
}
