package br.edu.iftm.ed.edumetrics.domain.dto;

import java.io.Serializable;
import java.time.Instant;

public record EventoRelatorio(
        String correlationId,
        Long alunoId,
        String tipo,
        String semestre,
        Instant solicitadoEm
) implements Serializable {
}
