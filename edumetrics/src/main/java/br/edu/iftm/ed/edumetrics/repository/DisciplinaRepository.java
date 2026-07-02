package br.edu.iftm.ed.edumetrics.repository;

import br.edu.iftm.ed.edumetrics.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    Optional<Disciplina> findByCodigoIgnoreCase(String codigo);
}
