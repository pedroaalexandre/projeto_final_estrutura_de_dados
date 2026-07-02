package br.edu.iftm.ed.edumetrics.repository;

import br.edu.iftm.ed.edumetrics.domain.Aluno;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @EntityGraph(attributePaths = {"desempenhos"})
    Optional<Aluno> findByMatricula(String matricula);

    Optional<Aluno> findByMatriculaIgnoreCase(String matricula);
}
