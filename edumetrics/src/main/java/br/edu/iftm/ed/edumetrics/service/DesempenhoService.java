package br.edu.iftm.ed.edumetrics.service;

import br.edu.iftm.ed.edumetrics.domain.Aluno;
import br.edu.iftm.ed.edumetrics.domain.Desempenho;
import br.edu.iftm.ed.edumetrics.domain.Disciplina;
import br.edu.iftm.ed.edumetrics.domain.dto.DesempenhoDTO;
import br.edu.iftm.ed.edumetrics.exception.AlunoNaoEncontradoException;
import br.edu.iftm.ed.edumetrics.repository.AlunoRepository;
import br.edu.iftm.ed.edumetrics.repository.DesempenhoRepository;
import br.edu.iftm.ed.edumetrics.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesempenhoService {

    @Autowired
    private DesempenhoRepository desempenhoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private RankingService rankingService;

    @CacheEvict(value = "ranking", allEntries = true)
    public DesempenhoDTO registrarDesempenho(Long alunoId, String codigoDisciplina,
                                              BigDecimal nota1, BigDecimal nota2, String semestre) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new AlunoNaoEncontradoException(alunoId));
        Disciplina disciplina = disciplinaRepository.findByCodigoIgnoreCase(codigoDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada: " + codigoDisciplina));
        Desempenho desempenho = new Desempenho(aluno, disciplina, nota1, nota2, semestre);
        Desempenho salvo = desempenhoRepository.save(desempenho);
        return toDTO(salvo);
    }

    @Cacheable(value = "desempenhos", key = "#id")
    public List<DesempenhoDTO> buscarPorAluno(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNaoEncontradoException(id));
        return aluno.getDesempenhos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DesempenhoDTO toDTO(Desempenho d) {
        return new DesempenhoDTO(d.getDisciplina().getNome(), d.getNota1(), d.getNota2(), d.getNotaFinal(), d.getSemestre());
    }
}
