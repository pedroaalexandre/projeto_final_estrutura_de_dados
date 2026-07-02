package br.edu.iftm.ed.edumetrics.domain.dto;

public record RankingItemSummaryDTO(
        String nome,
        String matricula,
        Double mediaGeral,
        Long disciplinasConcluidas
) {
}
