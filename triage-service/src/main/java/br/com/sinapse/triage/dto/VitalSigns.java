package br.com.sinapse.triage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VitalSigns(
    @Schema(description = "Systolic blood pressure (mmHg)", example = "120")
    @NotNull
    @Min(1)
    @Max(300)
    Integer systolicPressure,

    @Schema(description = "Diastolic blood pressure (mmHg)", example = "80")
    @NotNull
    @Min(1)
    @Max(200)
    Integer diastolicPressure,

    @Schema(description = "Heart rate (bpm)", example = "90")
    @NotNull
    @Min(1)
    @Max(300)
    Integer heartRate,

    @Schema(description = "Respiratory rate (rpm)", example = "18")
    @NotNull
    @Min(1)
    @Max(100)
    Integer respiratoryRate,

    @Schema(description = "Body temperature in Celsius", example = "37.2")
    @NotNull
    @DecimalMin("30.0")
    @DecimalMax("45.0")
    Double temperature,

    @Schema(description = "Peripheral oxygen saturation (%)", example = "96")
    @NotNull
    @Min(1)
    @Max(100)
    Integer oxygenSaturation
) {
}
