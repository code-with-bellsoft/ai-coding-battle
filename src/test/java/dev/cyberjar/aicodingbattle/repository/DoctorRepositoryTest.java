package dev.cyberjar.aicodingbattle.repository;

import dev.cyberjar.aicodingbattle.TestcontainersConfiguration;
import dev.cyberjar.aicodingbattle.domain.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void findBySpecialtyReturnsMatchingDoctors() {
        Doctor cardiologist = new Doctor();
        cardiologist.setFirstName("Jane");
        cardiologist.setLastName("Doe");
        cardiologist.setSpecialty("Cardiology");
        cardiologist.setAddress("Main Street 1");
        cardiologist.setLatitude(48.137154D);
        cardiologist.setLongitude(11.576124D);

        Doctor dermatologist = new Doctor();
        dermatologist.setFirstName("John");
        dermatologist.setLastName("Smith");
        dermatologist.setSpecialty("Dermatology");
        dermatologist.setAddress("Second Street 5");
        dermatologist.setLatitude(50.110924D);
        dermatologist.setLongitude(8.682127D);

        doctorRepository.save(cardiologist);
        doctorRepository.save(dermatologist);

        List<Doctor> results = doctorRepository.findBySpecialty("Cardiology");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getFirstName()).isEqualTo("Jane");
        assertThat(results.getFirst().getSpecialty()).isEqualTo("Cardiology");
    }
}
