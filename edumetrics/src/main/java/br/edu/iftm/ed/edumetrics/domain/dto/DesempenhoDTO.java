package br.edu.iftm.ed.edumetrics.domain.dto;

import java.math.BigDecimal;

public record DesempenhoDTO(
        String disciplina,
        BigDecimal nota1,
        BigDecimal nota2,
        BigDecimal notaFinal,
        String semestre
) {
}
