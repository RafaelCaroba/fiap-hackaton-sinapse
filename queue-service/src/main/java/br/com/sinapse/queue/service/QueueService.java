package br.com.sinapse.queue.service;

import br.com.sinapse.queue.entity.QueueEntry;
import br.com.sinapse.queue.enums.QueueStatus;
import br.com.sinapse.queue.mapper.QueueEntryMapper;
import br.com.sinapse.queue.repository.QueueEntryRepository;
import br.com.sinapse.shared.event.TriageCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueueService {

    private final QueueEntryRepository queueEntryRepository;
    private final QueueEntryMapper queueEntryMapper;

    @Transactional
    public QueueEntry createFromEvent(TriageCompletedEvent event) {
        validateEvent(event);

        if (queueEntryRepository.existsByTriageId(event.triageId())) {
            log.info("Ignoring duplicated triage event for triageId={}", event.triageId());
            return queueEntryRepository.findByTriageId(event.triageId())
                .orElseThrow(() -> new IllegalStateException("Duplicate triageId without existing queue entry"));
        }

        QueueEntry queueEntry = queueEntryMapper.toEntity(event);
        queueEntry.setStatus(QueueStatus.WAITING);
        return queueEntryRepository.save(queueEntry);
    }

    private void validateEvent(TriageCompletedEvent event) {
        if (event == null
                || event.triageId() == null
                || event.patientId() == null
                || event.cpf() == null
                || event.cpf().isBlank()
                || event.priority() == null
                || event.priority().isBlank()) {
            throw new IllegalArgumentException("Invalid triage completed event payload");
        }
    }
}
