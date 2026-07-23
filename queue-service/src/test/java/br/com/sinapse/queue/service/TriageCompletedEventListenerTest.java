package br.com.sinapse.queue.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sinapse.shared.event.TriageCompletedEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TriageCompletedEventListenerTest {

    @Mock
    private QueueService queueService;

    @InjectMocks
    private TriageCompletedEventListener listener;

    @Test
    void consume_shouldForwardEventToQueueService() {
        TriageCompletedEvent event = event();

        listener.consume(event);

        verify(queueService).createFromEvent(event);
    }

    @Test
    void consume_shouldNotThrow_whenQueueServiceFails() {
        TriageCompletedEvent event = event();
        when(queueService.createFromEvent(event)).thenThrow(new RuntimeException("boom"));

        assertThatCode(() -> listener.consume(event)).doesNotThrowAnyException();
    }

    private TriageCompletedEvent event() {
        return new TriageCompletedEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "12345678901",
            "STANDARD",
            LocalDateTime.now()
        );
    }
}
