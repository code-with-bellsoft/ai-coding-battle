package dev.cyberjar.aicodingbattle.service;

import dev.cyberjar.aicodingbattle.TestcontainersConfiguration;
import dev.cyberjar.aicodingbattle.domain.Doctor;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DoctorLocatorServiceIntegrationTest {

    @Autowired
    private DoctorLocatorService doctorLocatorService;

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    void clearRepository() {
        doctorRepository.deleteAll();
    }

    @Test
    void resultsAreSortedByDistanceAscending() {
        // Create doctors at different distances from Munich center (48.137154, 11.576124)
        save(createDoctor("David", "Far", "Cardiology", "Addr1", 48.2000D, 11.7000D));
        save(createDoctor("Charlie", "Medium", "Cardiology", "Addr2", 48.1700D, 11.6200D));
        save(createDoctor("Alice", "Near", "Cardiology", "Addr3", 48.1380D, 11.5770D));

        List<NearbyDoctorDto> results = doctorLocatorService.findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");

        assertThat(results).hasSize(3);
        assertThat(results.get(0).firstName()).isEqualTo("Alice");
        assertThat(results.get(1).firstName()).isEqualTo("Charlie");
        assertThat(results.get(2).firstName()).isEqualTo("David");

        double distanceA = results.get(0).distanceInKilometers();
        double distanceB = results.get(1).distanceInKilometers();
        double distanceC = results.get(2).distanceInKilometers();

        assertThat(distanceA).isLessThan(distanceB);
        assertThat(distanceB).isLessThan(distanceC);
    }

    @Test
    void returnsMaximumOfFiveDoctors() {
        for (int i = 0; i < 10; i++) {
            double lat = 48.1D + (i * 0.001D);
            double lon = 11.5D + (i * 0.001D);
            save(createDoctor("Dr" + i, "Name" + i, "Cardiology", "Addr" + i, lat, lon));
        }

        List<NearbyDoctorDto> results = doctorLocatorService.findNearbyDoctors(48.1D, 11.5D, "Cardiology");

        assertThat(results).hasSize(5);
    }

    @Test
    void filtersBySpecialtyCorrectly() {
        save(createDoctor("Card1", "Doctor", "Cardiology", "Addr1", 48.1D, 11.5D));
        save(createDoctor("Card2", "Doctor", "Cardiology", "Addr2", 48.2D, 11.6D));
        save(createDoctor("Derm1", "Doctor", "Dermatology", "Addr3", 48.15D, 11.55D));
        save(createDoctor("Derm2", "Doctor", "Dermatology", "Addr4", 48.25D, 11.65D));

        List<NearbyDoctorDto> cardiologyResults = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");
        List<NearbyDoctorDto> dermatologyResults = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "Dermatology");

        assertThat(cardiologyResults).extracting(NearbyDoctorDto::specialty)
                .allMatch(s -> s.equals("Cardiology"));
        assertThat(dermatologyResults).extracting(NearbyDoctorDto::specialty)
                .allMatch(s -> s.equals("Dermatology"));
    }

    @Test
    void skipsDoctorsWithMissingCoordinates() {
        save(createDoctor("Valid1", "Doc", "Cardiology", "Addr1", 48.1D, 11.5D));
        save(createDoctor("InvalidLat", "Doc", "Cardiology", "Addr2", null, 11.5D));
        save(createDoctor("InvalidLon", "Doc", "Cardiology", "Addr3", 48.1D, null));
        save(createDoctor("Valid2", "Doc", "Cardiology", "Addr4", 48.2D, 11.6D));

        List<NearbyDoctorDto> results = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");

        assertThat(results).hasSize(2);
        assertThat(results).extracting(NearbyDoctorDto::firstName)
                .contains("Valid1", "Valid2")
                .doesNotContain("InvalidLat", "InvalidLon");
    }

    @Test
    void returnsEmptyListForNonexistentSpecialty() {
        save(createDoctor("Card1", "Doc", "Cardiology", "Addr1", 48.1D, 11.5D));
        save(createDoctor("Card2", "Doc", "Cardiology", "Addr2", 48.2D, 11.6D));

        List<NearbyDoctorDto> results = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "NonexistentSpecialty");

        assertThat(results).isEmpty();
    }

    @Test
    void calculatesDistancesAccurately() {
        // Save a doctor at known distance
        save(createDoctor("TestDoc", "Test", "Cardiology", "Addr", 48.137154D, 11.576124D));

        List<NearbyDoctorDto> results = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");

        assertThat(results).hasSize(1);
        // Should be nearly zero distance since coordinates are identical
        assertThat(results.get(0).distanceInKilometers()).isLessThan(0.1D);
    }

    @Test
    void handlesMultipleSpecialtiesCorrectly() {
        save(createDoctor("Dr1", "A", "Cardiology", "Addr1", 48.1D, 11.5D));
        save(createDoctor("Dr2", "B", "Cardiology", "Addr2", 48.12D, 11.52D));
        save(createDoctor("Dr3", "C", "Neurology", "Addr3", 48.11D, 11.51D));
        save(createDoctor("Dr4", "D", "Neurology", "Addr4", 48.13D, 11.53D));

        List<NearbyDoctorDto> cardResults = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "Cardiology");
        List<NearbyDoctorDto> neuroResults = doctorLocatorService
                .findNearbyDoctors(48.137154D, 11.576124D, "Neurology");

        assertThat(cardResults).hasSize(2);
        assertThat(neuroResults).hasSize(2);
        assertThat(cardResults).extracting(NearbyDoctorDto::specialty).allMatch(s -> s.equals("Cardiology"));
        assertThat(neuroResults).extracting(NearbyDoctorDto::specialty).allMatch(s -> s.equals("Neurology"));
    }

    private Doctor createDoctor(String firstName, String lastName, String specialty, String address, Double latitude, Double longitude) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialty(specialty);
        doctor.setAddress(address);
        doctor.setLatitude(latitude);
        doctor.setLongitude(longitude);
        return doctor;
    }

    private void save(Doctor doctor) {
        doctorRepository.save(doctor);
    }
}
