package br.edu.iftm.ed.edumetrics.messaging;

import br.edu.iftm.ed.edumetrics.config.RabbitMQConfig;
import br.edu.iftm.ed.edumetrics.domain.dto.EventoRelatorio;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RelatorioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String solicitarRelatorio(Long alunoId, String tipo, String semestre) {
        String correlationId = UUID.randomUUID().toString();
        EventoRelatorio evento = new EventoRelatorio(correlationId, alunoId, tipo, semestre, Instant.now());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_RELATORIOS,
                RabbitMQConfig.ROUTING_KEY_PDF,
                evento);
        return correlationId;
    }
}
