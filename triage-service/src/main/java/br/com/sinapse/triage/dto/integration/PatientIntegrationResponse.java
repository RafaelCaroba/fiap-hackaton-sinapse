package br.com.sinapse.triage.dto.integration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PatientIntegrationResponse(
    UUID id,
    String cpf,
    String fullName,
    LocalDate birthDate,
    String gender,
    String bloodType,
    LocalDateTime createdAt
) {
}
