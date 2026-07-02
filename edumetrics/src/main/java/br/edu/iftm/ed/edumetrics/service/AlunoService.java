package br.edu.iftm.ed.edumetrics.service;

import br.edu.iftm.ed.edumetrics.domain.Aluno;
import br.edu.iftm.ed.edumetrics.domain.dto.AlunoDTO;
import br.edu.iftm.ed.edumetrics.estruturas.LRUCache;
import br.edu.iftm.ed.edumetrics.repository.AlunoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlunoService {

    private final Map<String, AlunoDTO> cacheMatricula = LRUCache.create(500);
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final int capacidadeCache = 500;

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private AutocompletarService autocompletarService;

    public AlunoDTO buscarPorMatricula(String matricula) {
        AlunoDTO dto = cacheMatricula.get(matricula);
        if (dto != null) {
            hits.incrementAndGet();
            return dto;
        }
        misses.incrementAndGet();
        return repository.findByMatriculaIgnoreCase(matricula)
                .map(this::cacheResult)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado: " + matricula));
    }

    @Cacheable(value = "alunos", key = "#id")
    public AlunoDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado: " + id));
    }

    @CachePut(value = "alunos", key = "#result.id")
    public AlunoDTO salvar(Aluno aluno) {
        Aluno salvo = repository.save(aluno);
        autocompletarService.indexar(salvo.getNome());
        return toDTO(salvo);
    }

    @CachePut(value = "alunos", key = "#id")
    public AlunoDTO atualizar(Long id, AlunoDTO dto) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado: " + id));
        aluno.setMatricula(dto.matricula());
        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setCurso(dto.curso());
        aluno.setPeriodo(dto.periodo());
        Aluno atualizado = repository.save(aluno);
        return toDTO(atualizado);
    }

    @CacheEvict(value = "alunos", key = "#id")
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = "alunos", allEntries = true)
    public void limparCache() {
        cacheMatricula.clear();
    }

    public List<AlunoDTO> buscarTodos() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public Map<String, Object> cacheStats() {
        long h = hits.get();
        long m = misses.get();
        long total = h + m;
        return Map.of(
                "entradas", cacheMatricula.size(),
                "capacidade", capacidadeCache,
                "hits", h,
                "misses", m,
                "hitRate", total == 0 ? 0.0 : (double) h / total
        );
    }

    private AlunoDTO cacheResult(Aluno aluno) {
        AlunoDTO dto = toDTO(aluno);
        cacheMatricula.put(aluno.getMatricula(), dto);
        return dto;
    }

    private AlunoDTO toDTO(Aluno a) {
        return new AlunoDTO(a.getId(), a.getMatricula(), a.getNome(), a.getEmail(), a.getCurso(), a.getPeriodo());
    }
}
