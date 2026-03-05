package dev.cyberjar.aicodingbattle.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Illness {

    @JsonProperty("name")
    private String name;

    @JsonProperty("certainty")
    private Double certainty;

    @JsonProperty("specialty")
    private String specialty;

    public Illness() {
    }

    public Illness(String name, Double certainty, String specialty) {
        this.name = name;
        this.certainty = certainty;
        this.specialty = specialty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCertainty() {
        return certainty;
    }

    public void setCertainty(Double certainty) {
        this.certainty = certainty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
