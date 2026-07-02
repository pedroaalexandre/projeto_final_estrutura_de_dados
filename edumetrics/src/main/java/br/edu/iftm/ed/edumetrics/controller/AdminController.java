package br.edu.iftm.ed.edumetrics.controller;

import br.edu.iftm.ed.edumetrics.estruturas.RateLimiter;
import br.edu.iftm.ed.edumetrics.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private RateLimiter rateLimiter;

    @GetMapping("/cache/stats")
    public ResponseEntity<Object> cacheStats() {
        return ResponseEntity.ok(alunoService.cacheStats());
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> limparCache() {
        alunoService.limparCache();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(
                Map.of(
                        "status", "UP",
                        "rateLimiter", "OK",
                        "redis", "CHECK_MANUALLY",
                        "rabbitmq", "CHECK_MANUALLY"
                ));
    }

    @GetMapping("/rate-limiter/stats")
    public ResponseEntity<Object> rateLimiterStats(@RequestParam(required = false, defaultValue = "unknown") String cliente) {
        return ResponseEntity.ok(rateLimiter.stats(cliente));
    }
}
