package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.domain.Doctor;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.repository.DoctorRepository;
import dev.cyberjar.aicodingbattle.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DoctorLocatorService {

    private final DoctorRepository doctorRepository;

    public DoctorLocatorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<NearbyDoctorDto> findNearbyDoctors(double latitude, double longitude, String specialty) {
        List<Doctor> doctors = this.doctorRepository.findBySpecialty(specialty.trim());

        return doctors.stream()
                .filter(this::hasCoordinates)
                .map(doctor -> toNearbyDoctorDto(doctor, latitude, longitude))
                .sorted(Comparator.comparingDouble(NearbyDoctorDto::distanceInKilometers))
                .limit(5)
                .toList();
    }

    private boolean hasCoordinates(Doctor doctor) {
        return doctor.getLatitude() != null && doctor.getLongitude() != null;
    }

    private NearbyDoctorDto toNearbyDoctorDto(Doctor doctor, double userLatitude, double userLongitude) {
        double distance = DistanceCalculator.calculateDistanceInKilometers(
                userLatitude,
                userLongitude,
                doctor.getLatitude(),
                doctor.getLongitude());

        return new NearbyDoctorDto(
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialty(),
                doctor.getAddress(),
                distance);
    }
}
