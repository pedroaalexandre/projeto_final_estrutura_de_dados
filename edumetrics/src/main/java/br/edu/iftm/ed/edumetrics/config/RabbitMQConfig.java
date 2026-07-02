package br.edu.iftm.ed.edumetrics.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    public static final String EXCHANGE_RELATORIOS = "edumetrics.relatorios";
    public static final String FILA_RELATORIOS = "relatorios.processamento";
    public static final String FILA_DLQ = "relatorios.dlq";
    public static final String ROUTING_KEY_PDF = "relatorio.gerar";

    @Bean
    public TopicExchange exchangeRelatorios() {
        return ExchangeBuilder.topicExchange(EXCHANGE_RELATORIOS)
                .durable(true)
                .build();
    }

    @Bean
    public Queue filaDLQ() {
        return QueueBuilder.durable(FILA_DLQ).build();
    }

    @Bean
    public Queue filaRelatorios() {
        return QueueBuilder.durable(FILA_RELATORIOS)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", FILA_DLQ)
                .build();
    }

    @Bean
    public Binding bindingRelatorios(Queue filaRelatorios, TopicExchange exchangeRelatorios) {
        return BindingBuilder.bind(filaRelatorios)
                .to(exchangeRelatorios)
                .with(ROUTING_KEY_PDF);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(converter);
        return t;
    }
}
