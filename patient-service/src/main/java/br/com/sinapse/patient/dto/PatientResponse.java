package br.com.sinapse.patient.dto;

import br.com.sinapse.patient.domain.BloodType;
import br.com.sinapse.patient.domain.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PatientResponse(
    UUID id,
    String cpf,
    String fullName,
    LocalDate birthDate,
    Gender gender,
    BloodType bloodType,
    LocalDateTime createdAt
) {
}
