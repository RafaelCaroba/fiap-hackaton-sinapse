package br.com.sinapse.shared.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record TriageCompletedEvent(
    UUID triageId,
    UUID patientId,
    String cpf,
    String priority,
    LocalDateTime completedAt
) {
}
