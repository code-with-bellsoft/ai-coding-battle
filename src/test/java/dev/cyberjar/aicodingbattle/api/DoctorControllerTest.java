package dev.cyberjar.aicodingbattle.api;

import dev.cyberjar.aicodingbattle.entity.Doctor;
import dev.cyberjar.aicodingbattle.repository.DoctorRepository;
import dev.cyberjar.aicodingbattle.service.SpecialistSearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoctorControllerTest {

    @Test
    void testDoctorControllerInitialization() {
        // Simple test to verify the controller can be instantiated
        DoctorRepository mockRepo = Mockito.mock(DoctorRepository.class);
        SpecialistSearchService service = new SpecialistSearchService(mockRepo);
        DoctorController controller = new DoctorController(service);
        
        assertNotNull(controller);
    }

    @Test
    void testServiceFindNearbyDoctors() {
        // Test the service directly
        DoctorRepository mockRepo = Mockito.mock(DoctorRepository.class);
        
        Doctor doc1 = new Doctor("John", "Smith", "Cardiology",
                "123 Main St", BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060));
        List<Doctor> doctorsList = new ArrayList<>();
        doctorsList.add(doc1);
        
        Mockito.when(mockRepo.findBySpecialty("Cardiology")).thenReturn(doctorsList);
        
        SpecialistSearchService service = new SpecialistSearchService(mockRepo);
        List<Doctor> result = service.findNearbyDoctors(
                BigDecimal.valueOf(40.7128), 
                BigDecimal.valueOf(-74.0060), 
                "Cardiology"
        );
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
