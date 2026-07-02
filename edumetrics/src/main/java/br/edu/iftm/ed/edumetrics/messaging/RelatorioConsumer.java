package br.edu.iftm.ed.edumetrics.messaging;

import br.edu.iftm.ed.edumetrics.config.RabbitMQConfig;
import br.edu.iftm.ed.edumetrics.domain.dto.EventoRelatorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RelatorioConsumer {

    private static final Logger log = LoggerFactory.getLogger(RelatorioConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.FILA_RELATORIOS)
    public void processar(EventoRelatorio evento) {
        log.info("Processando relatório {} para aluno {}", evento.tipo(), evento.alunoId());
        try {
            Thread.sleep(2000);
            log.info("Relatorio {} concluido — correlationId={}", evento.tipo(), evento.correlationId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Erro ao gerar relatório: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_DLQ)
    public void processarDLQ(EventoRelatorio evento) {
        log.error("Mensagem na DLQ — relatório {} para aluno {} não foi gerado.", evento.tipo(), evento.alunoId());
    }
}
