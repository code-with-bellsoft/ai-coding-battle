package dev.cyberjar.aicodingbattle.api;

import dev.cyberjar.aicodingbattle.entity.Doctor;
import dev.cyberjar.aicodingbattle.service.SpecialistSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorController {

    private final SpecialistSearchService specialistSearchService;

    public DoctorController(SpecialistSearchService specialistSearchService) {
        this.specialistSearchService = specialistSearchService;
    }

    @GetMapping("/doctors/nearby")
    public ResponseEntity<List<Doctor>> getNearbyDoctors(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam String specialty) {
        List<Doctor> doctors = specialistSearchService.findNearbyDoctors(lat, lon, specialty);
        return ResponseEntity.ok(doctors);
    }
}
