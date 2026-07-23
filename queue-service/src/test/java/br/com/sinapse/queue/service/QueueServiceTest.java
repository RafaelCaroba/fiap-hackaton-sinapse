package br.com.sinapse.queue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sinapse.queue.entity.QueueEntry;
import br.com.sinapse.queue.enums.QueuePriority;
import br.com.sinapse.queue.enums.QueueStatus;
import br.com.sinapse.queue.mapper.QueueEntryMapper;
import br.com.sinapse.queue.repository.QueueEntryRepository;
import br.com.sinapse.shared.event.TriageCompletedEvent;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueEntryRepository queueEntryRepository;

    @Mock
    private QueueEntryMapper queueEntryMapper;

    @InjectMocks
    private QueueService queueService;

    @Test
    void createFromEvent_shouldCreateWaitingEntryAndPersist() {
        TriageCompletedEvent event = event();
        QueueEntry mapped = mappedEntry(event);
        QueueEntry saved = mappedEntry(event);
        saved.setId(UUID.randomUUID());
        saved.setStatus(QueueStatus.WAITING);

        when(queueEntryRepository.existsByTriageId(event.triageId())).thenReturn(false);
        when(queueEntryMapper.toEntity(event)).thenReturn(mapped);
        when(queueEntryRepository.save(mapped)).thenReturn(saved);

        QueueEntry result = queueService.createFromEvent(event);

        assertThat(result).isEqualTo(saved);
        assertThat(mapped.getStatus()).isEqualTo(QueueStatus.WAITING);
        verify(queueEntryRepository).existsByTriageId(event.triageId());
        verify(queueEntryMapper).toEntity(event);
        verify(queueEntryRepository).save(mapped);
    }

    @Test
    void createFromEvent_shouldIgnoreDuplicateAndReturnExistingEntry() {
        TriageCompletedEvent event = event();
        QueueEntry existing = mappedEntry(event);
        existing.setId(UUID.randomUUID());
        existing.setStatus(QueueStatus.WAITING);

        when(queueEntryRepository.existsByTriageId(event.triageId())).thenReturn(true);
        when(queueEntryRepository.findByTriageId(event.triageId())).thenReturn(Optional.of(existing));

        QueueEntry result = queueService.createFromEvent(event);

        assertThat(result).isEqualTo(existing);
        verify(queueEntryRepository).existsByTriageId(event.triageId());
        verify(queueEntryRepository).findByTriageId(event.triageId());
        verify(queueEntryMapper, never()).toEntity(any());
        verify(queueEntryRepository, never()).save(any());
    }

    @Test
    void createFromEvent_shouldFailForInvalidPayload() {
        TriageCompletedEvent invalid = new TriageCompletedEvent(null, null, "", "", LocalDateTime.now());

        assertThatThrownBy(() -> queueService.createFromEvent(invalid))
            .isInstanceOf(IllegalArgumentException.class);

        verify(queueEntryRepository, never()).save(any());
    }

    private TriageCompletedEvent event() {
        return new TriageCompletedEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "12345678901",
            "URGENT",
            LocalDateTime.now()
        );
    }

    private QueueEntry mappedEntry(TriageCompletedEvent event) {
        return QueueEntry.builder()
            .triageId(event.triageId())
            .patientId(event.patientId())
            .cpf(event.cpf())
            .priority(QueuePriority.URGENT)
            .build();
    }
}
