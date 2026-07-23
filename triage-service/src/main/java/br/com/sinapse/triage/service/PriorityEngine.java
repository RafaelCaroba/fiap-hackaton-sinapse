package br.com.sinapse.triage.service;

import br.com.sinapse.triage.dto.VitalSigns;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.enums.Priority;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class PriorityEngine {

    public Priority calculate(CreateTriageRequest request) {
        Priority vitalSignsPriority = evaluateVitalSigns(request.vitalSigns());
        Priority symptomsPriority = evaluateSymptoms(request.symptoms());
        return resolvePriority(vitalSignsPriority, symptomsPriority);
    }

    private Priority evaluateVitalSigns(VitalSigns vitalSigns) {
        if (vitalSigns == null) {
            return Priority.NON_URGENT;
        }

        if (vitalSigns.oxygenSaturation() < 90
                || vitalSigns.temperature() >= 40.0
                || vitalSigns.respiratoryRate() >= 35
                || vitalSigns.systolicPressure() < 80) {
            return Priority.EMERGENCY;
        }

        if (isBetween(vitalSigns.oxygenSaturation(), 90, 92)
                || vitalSigns.heartRate() >= 130
                || (vitalSigns.temperature() >= 39.0 && vitalSigns.temperature() < 40.0)
                || isBetween(vitalSigns.systolicPressure(), 80, 90)) {
            return Priority.VERY_URGENT;
        }

        if (vitalSigns.temperature() >= 38.0
                || vitalSigns.heartRate() >= 110
                || vitalSigns.systolicPressure() >= 180
                || vitalSigns.diastolicPressure() >= 110) {
            return Priority.URGENT;
        }

        return Priority.NON_URGENT;
    }

    private Priority evaluateSymptoms(List<String> symptoms) {
        Set<String> normalizedSymptoms = normalizeSymptoms(symptoms);

        if (containsAll(normalizedSymptoms, "dor no peito", "falta de ar")) {
            return Priority.EMERGENCY;
        }

        if (containsAny(normalizedSymptoms, "confusao mental", "convulsao", "desmaio")) {
            return Priority.VERY_URGENT;
        }

        if (containsAny(normalizedSymptoms, "vomito persistente", "dor intensa", "fratura")) {
            return Priority.URGENT;
        }

        if (containsAny(normalizedSymptoms, "dor moderada", "tosse", "nausea", "dor abdominal", "cefaleia")) {
            return Priority.STANDARD;
        }

        return Priority.NON_URGENT;
    }

    private Priority resolvePriority(Priority first, Priority second) {
        return priorityWeight(first) >= priorityWeight(second) ? first : second;
    }

    private int priorityWeight(Priority priority) {
        return switch (priority) {
            case EMERGENCY -> 5;
            case VERY_URGENT -> 4;
            case URGENT -> 3;
            case STANDARD -> 2;
            case NON_URGENT -> 1;
        };
    }

    private boolean isBetween(Integer value, int startInclusive, int endInclusive) {
        return value >= startInclusive && value <= endInclusive;
    }

    private Set<String> normalizeSymptoms(List<String> symptoms) {
        Set<String> normalized = new HashSet<>();
        if (symptoms == null) {
            return normalized;
        }

        for (String symptom : symptoms) {
            if (symptom == null) {
                continue;
            }
            String value = Normalizer.normalize(symptom, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
            if (!value.isEmpty()) {
                normalized.add(value);
            }
        }
        return normalized;
    }

    private boolean containsAll(Set<String> symptoms, String first, String second) {
        return symptoms.contains(first) && symptoms.contains(second);
    }

    private boolean containsAny(Set<String> symptoms, String... candidates) {
        for (String candidate : candidates) {
            if (symptoms.contains(candidate)) {
                return true;
            }
        }
        return false;
    }
}
