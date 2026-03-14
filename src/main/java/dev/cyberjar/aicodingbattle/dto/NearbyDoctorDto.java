package dev.cyberjar.aicodingbattle.dto;

public record NearbyDoctorDto(
        String firstName,
        String lastName,
        String specialty,
        String address,
        double distanceInKilometers
) {
}
