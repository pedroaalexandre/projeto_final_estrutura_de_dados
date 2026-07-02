package br.edu.iftm.ed.edumetrics.controller;

import br.edu.iftm.ed.edumetrics.domain.Aluno;
import br.edu.iftm.ed.edumetrics.domain.dto.AlunoDTO;
import br.edu.iftm.ed.edumetrics.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@Validated
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoDTO> cadastrar(@Valid @RequestBody AlunoDTO dto) {
        Aluno aluno = new Aluno(dto.matricula(), dto.nome(), dto.email(), dto.curso(), dto.periodo());
        AlunoDTO salvo = alunoService.salvar(aluno);
        URI location = URI.create("/api/alunos/" + salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listar() {
        return ResponseEntity.ok(alunoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AlunoDTO> buscarPorMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(alunoService.buscarPorMatricula(matricula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(alunoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        alunoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
