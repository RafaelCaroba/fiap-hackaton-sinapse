package br.com.sinapse.queue.service;

import br.com.sinapse.shared.event.TriageCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TriageCompletedEventListener {

    private final QueueService queueService;

    @RabbitListener(queues = "${triage-event.queue}")
    public void consume(TriageCompletedEvent event) {
        try {
            queueService.createFromEvent(event);
        } catch (Exception ex) {
            log.error("Error consuming TriageCompletedEvent triageId={}", event != null ? event.triageId() : null, ex);
        }
    }
}
