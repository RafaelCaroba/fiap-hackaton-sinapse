package br.com.sinapse.patient.dto;

import br.com.sinapse.patient.domain.BloodType;
import br.com.sinapse.patient.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record CreatePatientRequest(
    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF must contain exactly 11 digits")
    String cpf,

    @NotBlank
    String fullName,

    @NotNull
    @Past
    LocalDate birthDate,

    @NotNull
    Gender gender,

    @NotNull
    BloodType bloodType
) {
}
