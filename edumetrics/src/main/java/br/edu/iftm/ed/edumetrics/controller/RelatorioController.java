package br.edu.iftm.ed.edumetrics.controller;

import br.edu.iftm.ed.edumetrics.domain.dto.EventoRelatorio;
import br.edu.iftm.ed.edumetrics.messaging.RelatorioProducer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

record SolicitacaoRelatorioDTO(Long alunoId, String tipo, String semestre) {
}

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioProducer producer;

    @PostMapping
    public ResponseEntity<Map<String, String>> solicitar(@Valid @RequestBody SolicitacaoRelatorioDTO req) {
        String correlationId = producer.solicitarRelatorio(req.alunoId(), req.tipo(), req.semestre());
        return ResponseEntity.accepted().body(
                Map.of("correlationId", correlationId,
                        "mensagem", "Relatorio em processamento. Use o correlationId para rastrear."));
    }
}
