package br.edu.iftm.ed.edumetrics.controller;

import br.edu.iftm.ed.edumetrics.domain.dto.RankingItemDTO;
import br.edu.iftm.ed.edumetrics.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingItemDTO>> topK(@RequestParam(name = "top", defaultValue = "10") int k) {
        return ResponseEntity.ok(rankingService.topK(k));
    }
}
