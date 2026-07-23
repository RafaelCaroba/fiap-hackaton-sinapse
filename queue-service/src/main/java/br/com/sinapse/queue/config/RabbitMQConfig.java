package br.com.sinapse.queue.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange triageEventExchange(@Value("${triage-event.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue triageCompletedQueue(@Value("${triage-event.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding triageCompletedBinding(
            Queue triageCompletedQueue,
            TopicExchange triageEventExchange,
            @Value("${triage-event.routing-key}") String routingKey) {
        return BindingBuilder.bind(triageCompletedQueue).to(triageEventExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
