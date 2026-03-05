package dev.cyberjar.aicodingbattle.repository;

import dev.cyberjar.aicodingbattle.TestcontainersConfiguration;
import dev.cyberjar.aicodingbattle.domain.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    void clearRepository() {
        doctorRepository.deleteAll();
    }

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

    @Test
    void savePersistsDoctorToDatabase() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Alice");
        doctor.setLastName("Johnson");
        doctor.setSpecialty("Neurology");
        doctor.setAddress("Park Avenue 10");
        doctor.setLatitude(40.7580D);
        doctor.setLongitude(-73.9855D);

        Doctor saved = doctorRepository.save(doctor);

        assertThat(saved.getId()).isNotNull();
        Optional<Doctor> retrieved = doctorRepository.findById(saved.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getFirstName()).isEqualTo("Alice");
        assertThat(retrieved.get().getSpecialty()).isEqualTo("Neurology");
    }

    @Test
    void findBySpecialtyFiltersCorrectly() {
        doctorRepository.save(createDoctor("Dr1", "Smith", "Cardiology", "Addr1", 48.1D, 11.5D));
        doctorRepository.save(createDoctor("Dr2", "Jones", "Cardiology", "Addr2", 48.2D, 11.6D));
        doctorRepository.save(createDoctor("Dr3", "Brown", "Dermatology", "Addr3", 48.3D, 11.7D));
        doctorRepository.save(createDoctor("Dr4", "White", "Neurology", "Addr4", 48.4D, 11.8D));

        List<Doctor> cardiologists = doctorRepository.findBySpecialty("Cardiology");
        List<Doctor> dermatologists = doctorRepository.findBySpecialty("Dermatology");

        assertThat(cardiologists).hasSize(2);
        assertThat(dermatologists).hasSize(1);
        assertThat(cardiologists).extracting(Doctor::getSpecialty).allMatch(s -> s.equals("Cardiology"));
    }

    @Test
    void deleteRemovesDoctorFromDatabase() {
        Doctor doctor = doctorRepository.save(createDoctor("Test", "Doctor", "Cardiology", "Addr", 48.1D, 11.5D));
        Long doctorId = doctor.getId();

        doctorRepository.deleteById(doctorId);

        Optional<Doctor> retrieved = doctorRepository.findById(doctorId);
        assertThat(retrieved).isEmpty();
    }

    @Test
    void findBySpecialtyReturnsEmptyListForNonexistentSpecialty() {
        doctorRepository.save(createDoctor("Dr", "Test", "Cardiology", "Addr", 48.1D, 11.5D));

        List<Doctor> results = doctorRepository.findBySpecialty("NonexistentSpecialty");

        assertThat(results).isEmpty();
    }

    @Test
    void distanceSortingLogicRequiresPersistentCoordinates() {
        Doctor withCoords = createDoctor("CoordDoc", "Test", "Cardiology", "Addr1", 48.1D, 11.5D);
        Doctor nullLat = createDoctor("NullLatDoc", "Test", "Cardiology", "Addr2", null, 11.5D);
        Doctor nullLon = createDoctor("NullLonDoc", "Test", "Cardiology", "Addr3", 48.1D, null);

        doctorRepository.save(withCoords);
        doctorRepository.save(nullLat);
        doctorRepository.save(nullLon);

        List<Doctor> doctors = doctorRepository.findBySpecialty("Cardiology");

        assertThat(doctors).hasSize(3);
        assertThat(doctors).filteredOn(d -> d.getLatitude() != null && d.getLongitude() != null).hasSize(1);
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
}
