package br.edu.iftm.ed.edumetrics.repository;

import br.edu.iftm.ed.edumetrics.domain.Desempenho;
import br.edu.iftm.ed.edumetrics.domain.dto.RankingItemSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesempenhoRepository extends JpaRepository<Desempenho, Long> {

    @Query("SELECT new br.edu.iftm.ed.edumetrics.domain.dto.RankingItemSummaryDTO(a.nome, a.matricula, AVG(d.notaFinal), COUNT(d)) " +
           "FROM Desempenho d JOIN d.aluno a " +
           "GROUP BY a.id, a.nome, a.matricula " +
           "ORDER BY AVG(d.notaFinal) DESC")
    List<RankingItemSummaryDTO> findMediasPorAluno();
}
