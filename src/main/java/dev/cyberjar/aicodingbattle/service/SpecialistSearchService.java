package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.entity.Doctor;
import dev.cyberjar.aicodingbattle.model.DoctorDistance;
import dev.cyberjar.aicodingbattle.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SpecialistSearchService {

    private final DoctorRepository doctorRepository;

    public SpecialistSearchService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findNearbyDoctors(BigDecimal latitude, BigDecimal longitude, String specialty) {
        List<Doctor> doctorsBySpecialty = doctorRepository.findBySpecialty(specialty);

        return doctorsBySpecialty.stream()
                .map(doctor -> new DoctorDistance(doctor, calculateDistance(latitude, longitude, 
                        doctor.getLatitude(), doctor.getLongitude())))
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .limit(5)
                .map(DoctorDistance::getDoctor)
                .toList();
    }

    private Double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return Double.MAX_VALUE;
        }

        double latDiff = lat2.doubleValue() - lat1.doubleValue();
        double lonDiff = lon2.doubleValue() - lon1.doubleValue();

        // Simple Euclidean distance (for demo purposes, not geodesic)
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}
