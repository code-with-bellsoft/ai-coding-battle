package dev.cyberjar.aicodingbattle.repository;

import dev.cyberjar.aicodingbattle.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialty(String specialty);
}
