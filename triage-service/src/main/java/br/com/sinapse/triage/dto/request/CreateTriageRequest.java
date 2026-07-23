package br.com.sinapse.triage.dto.request;

import br.com.sinapse.triage.dto.VitalSigns;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record CreateTriageRequest(
    @Schema(description = "Patient CPF with exactly 11 digits", example = "12345678901")
    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF must contain exactly 11 digits")
    String cpf,

    @ArraySchema(schema = @Schema(description = "Reported symptom", example = "Chest pain"))
    @NotEmpty
    List<@NotBlank String> symptoms,

    @Schema(description = "Vital signs captured during triage")
    @NotNull
    @Valid
    VitalSigns vitalSigns
) {
}
