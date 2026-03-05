package dev.cyberjar.aicodingbattle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnalyzeSymptomsRequestDto(
        @NotBlank(message = "symptoms must not be blank")
        @Size(max = 4000, message = "symptoms must not exceed 4000 characters")
        String symptoms
) {
}
