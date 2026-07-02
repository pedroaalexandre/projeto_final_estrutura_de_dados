package br.edu.iftm.ed.edumetrics.controller;

import br.edu.iftm.ed.edumetrics.domain.dto.DesempenhoDTO;
import br.edu.iftm.ed.edumetrics.service.DesempenhoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

record DesempenhoRequestDTO(Long alunoId, String codigoDisciplina, BigDecimal nota1, BigDecimal nota2, String semestre) {
}

@RestController
@RequestMapping("/api/desempenhos")
public class DesempenhoController {

    @Autowired
    private DesempenhoService desempenhoService;

    @PostMapping
    public ResponseEntity<DesempenhoDTO> registrar(@Valid @RequestBody DesempenhoRequestDTO request) {
        DesempenhoDTO salvo = desempenhoService.registrarDesempenho(request.alunoId(), request.codigoDisciplina(), request.nota1(), request.nota2(), request.semestre());
        return ResponseEntity.status(201).body(salvo);
    }
}
