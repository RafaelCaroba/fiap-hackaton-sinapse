package br.com.sinapse.triage.service;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.sinapse.triage.dto.VitalSigns;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.enums.Priority;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriorityEngineTest {

    private final PriorityEngine priorityEngine = new PriorityEngine();

    @Test
    void calculate_shouldReturnEmergency_forCriticalSaturation() {
        CreateTriageRequest request = request(List.of("tosse"), vitalSigns(88, 37.0, 90, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.EMERGENCY);
    }

    @Test
    void calculate_shouldReturnVeryUrgent_forSpecificSymptom() {
        CreateTriageRequest request = request(List.of("Confusão Mental"), vitalSigns(96, 37.0, 80, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.VERY_URGENT);
    }

    @Test
    void calculate_shouldReturnUrgent_forHighDiastolicPressure() {
        CreateTriageRequest request = request(List.of("sintoma leve"), vitalSigns(96, 37.0, 95, 18, 120, 120));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.URGENT);
    }

    @Test
    void calculate_shouldReturnStandard_forModerateSymptom() {
        CreateTriageRequest request = request(List.of("dor moderada"), vitalSigns(97, 37.0, 80, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.STANDARD);
    }

    @Test
    void calculate_shouldReturnNonUrgent_whenNoRuleMatches() {
        CreateTriageRequest request = request(List.of("espirro"), vitalSigns(98, 36.5, 75, 16, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.NON_URGENT);
    }

    @Test
    void calculate_shouldPrioritizeHigherSeverity_whenRulesConflict() {
        CreateTriageRequest request = request(List.of("febre"), vitalSigns(88, 38.5, 95, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.EMERGENCY);
    }

    @Test
    void calculate_shouldHandleEmptySymptomsList_whenVitalsDrivePriority() {
        CreateTriageRequest request = request(List.of(), vitalSigns(90, 37.0, 80, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.VERY_URGENT);
    }

    @Test
    void calculate_shouldMatchSymptoms_caseInsensitiveAndAccentInsensitive() {
        CreateTriageRequest request = request(List.of("VÔMITO PERSISTENTE"), vitalSigns(97, 37.0, 80, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.URGENT);
    }

    @Test
    void calculate_shouldReturnEmergency_whenChestPainAndDyspneaPresentTogether() {
        CreateTriageRequest request = request(List.of("dor no peito", "falta de ar"), vitalSigns(97, 37.0, 80, 18, 120, 80));

        Priority priority = priorityEngine.calculate(request);

        assertThat(priority).isEqualTo(Priority.EMERGENCY);
    }

    private CreateTriageRequest request(List<String> symptoms, VitalSigns vitalSigns) {
        return new CreateTriageRequest("12345678901", symptoms, vitalSigns);
    }

    private VitalSigns vitalSigns(
            Integer oxygenSaturation,
            Double temperature,
            Integer heartRate,
            Integer respiratoryRate,
            Integer systolicPressure,
            Integer diastolicPressure) {
        return new VitalSigns(
            systolicPressure,
            diastolicPressure,
            heartRate,
            respiratoryRate,
            temperature,
            oxygenSaturation
        );
    }
}
