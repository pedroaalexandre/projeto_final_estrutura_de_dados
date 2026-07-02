package br.edu.iftm.ed.edumetrics.domain.dto;

public record RankingItemDTO(
        int posicao,
        String nome,
        String matricula,
        Double mediaGeral,
        Long disciplinasConcluidas
) implements Comparable<RankingItemDTO> {
    @Override
    public int compareTo(RankingItemDTO outro) {
        return Double.compare(this.mediaGeral(), outro.mediaGeral());
    }
}
