package br.com.sinapse.triage.service;

import br.com.sinapse.triage.event.TriageCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TriageEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${triage-event.exchange}")
    private String exchange;

    @Value("${triage-event.routing-key}")
    private String routingKey;

    public void publish(TriageCompletedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
