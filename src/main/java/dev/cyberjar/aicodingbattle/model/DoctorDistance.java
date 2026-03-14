package dev.cyberjar.aicodingbattle.model;

import dev.cyberjar.aicodingbattle.entity.Doctor;

public class DoctorDistance {

    private Doctor doctor;
    private Double distance;

    public DoctorDistance(Doctor doctor, Double distance) {
        this.doctor = doctor;
        this.distance = distance;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
