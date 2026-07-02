package br.edu.iftm.ed.edumetrics.service;

import br.edu.iftm.ed.edumetrics.domain.dto.RankingItemDTO;
import br.edu.iftm.ed.edumetrics.domain.dto.RankingItemSummaryDTO;
import br.edu.iftm.ed.edumetrics.repository.DesempenhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankingService {

    @Autowired
    private DesempenhoRepository desempenhoRepository;

    @Cacheable(value = "ranking", key = "#k")
    public List<RankingItemDTO> topK(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k deve ser positivo");
        }

        PriorityQueue<RankingItemDTO> heap = new PriorityQueue<>(Comparator.comparingDouble(RankingItemDTO::mediaGeral));
        for (RankingItemSummaryDTO item : desempenhoRepository.findMediasPorAluno()) {
            if (item.mediaGeral() == null) {
                continue;
            }
            heap.offer(new RankingItemDTO(0, item.nome(), item.matricula(), item.mediaGeral(), item.disciplinasConcluidas()));
            if (heap.size() > k) {
                heap.poll();
            }
        }

        List<RankingItemDTO> ranking = new ArrayList<>(heap);
        ranking.sort(Comparator.comparingDouble(RankingItemDTO::mediaGeral).reversed());
        List<RankingItemDTO> resultado = new ArrayList<>();
        for (int i = 0; i < ranking.size(); i++) {
            RankingItemDTO item = ranking.get(i);
            resultado.add(new RankingItemDTO(i + 1, item.nome(), item.matricula(), item.mediaGeral(), item.disciplinasConcluidas()));
        }
        return Collections.unmodifiableList(resultado);
    }

    @CacheEvict(value = "ranking", allEntries = true)
    public void invalidarRanking() {
    }
}
