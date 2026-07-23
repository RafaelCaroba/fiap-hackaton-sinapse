package br.com.sinapse.triage.dto.response;

import br.com.sinapse.triage.dto.VitalSigns;
import br.com.sinapse.triage.enums.Priority;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TriageResponse(
    @Schema(description = "Triage identifier", example = "de7a56a4-3cd9-4e53-9a2e-f47ab6f9e733")
    UUID id,

    @Schema(description = "Patient identifier in patient-service", example = "7a450ed1-2167-41fa-8b51-e08b74991f5b")
    UUID patientId,

    @Schema(description = "Patient CPF with exactly 11 digits", example = "12345678901")
    String cpf,

    @ArraySchema(schema = @Schema(description = "Reported symptom", example = "Shortness of breath"))
    List<String> symptoms,

    @Schema(description = "Vital signs captured during triage")
    VitalSigns vitalSigns,

    @Schema(description = "Risk classification priority", example = "URGENT")
    Priority priority,

    @Schema(description = "Triage creation timestamp", example = "2026-07-22T20:40:00")
    LocalDateTime createdAt
) {
}
