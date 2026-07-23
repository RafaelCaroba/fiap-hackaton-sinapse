package br.com.sinapse.triage.event;

import br.com.sinapse.triage.enums.Priority;
import java.time.LocalDateTime;
import java.util.UUID;

public record TriageCompletedEvent(
    UUID triageId,
    UUID patientId,
    String cpf,
    Priority priority,
    LocalDateTime completedAt
) {
}
