package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.domain.Doctor;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorLocatorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorLocatorService doctorLocatorService;

    @Test
    void sortsDoctorsByDistanceAndReturnsClosestFive() {
        when(doctorRepository.findBySpecialty("Cardiology")).thenReturn(List.of(
                createDoctor("Fiona", "Far", "Cardiology", "Addr 1", 48.2000D, 11.7000D),
                createDoctor("Nina", "Near", "Cardiology", "Addr 2", 48.1400D, 11.5800D),
                createDoctor("Mia", "Medium", "Cardiology", "Addr 3", 48.1700D, 11.6200D),
                createDoctor("Oscar", "Outer", "Cardiology", "Addr 4", 48.2100D, 11.7300D),
                createDoctor("Liam", "Local", "Cardiology", "Addr 5", 48.1372D, 11.5762D),
                createDoctor("Quinn", "QuiteFar", "Cardiology", "Addr 6", 48.2500D, 11.7600D)
        ));

        List<NearbyDoctorDto> result = doctorLocatorService.findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");

        assertThat(result).hasSize(5);
        assertThat(result).extracting(NearbyDoctorDto::firstName)
                .containsExactly("Liam", "Nina", "Mia", "Fiona", "Oscar");
        assertThat(result.get(0).distanceInKilometers()).isLessThan(result.get(1).distanceInKilometers());
    }

    @Test
    void skipsDoctorsWithoutCoordinates() {
        Doctor invalid = createDoctor("No", "Coords", "Cardiology", "Addr 0", null, 11.5700D);
        Doctor valid = createDoctor("Valid", "Doc", "Cardiology", "Addr 1", 48.1400D, 11.5800D);
        when(doctorRepository.findBySpecialty("Cardiology")).thenReturn(List.of(invalid, valid));

        List<NearbyDoctorDto> result = doctorLocatorService.findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().firstName()).isEqualTo("Valid");
    }

    private Doctor createDoctor(String firstName,
                                String lastName,
                                String specialty,
                                String address,
                                Double latitude,
                                Double longitude) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialty(specialty);
        doctor.setAddress(address);
        doctor.setLatitude(latitude);
        doctor.setLongitude(longitude);
        return doctor;
    }
}
