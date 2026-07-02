package br.edu.iftm.ed.edumetrics.controller;

import br.edu.iftm.ed.edumetrics.service.AutocompletarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    @Autowired
    private AutocompletarService autocompletarService;

    @GetMapping("/autocompletar")
    public ResponseEntity<List<String>> autocompletar(@RequestParam String q) {
        return ResponseEntity.ok(autocompletarService.sugerir(q));
    }
}
