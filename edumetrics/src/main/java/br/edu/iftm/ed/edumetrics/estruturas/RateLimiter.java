package br.edu.iftm.ed.edumetrics.estruturas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private final ConcurrentHashMap<String, Deque<Long>> janelas = new ConcurrentHashMap<>();

    @Value("${rate.limiter.max-requisicoes:100}")
    private int maxRequisicoes;

    @Value("${rate.limiter.janela-ms:60000}")
    private long janelaMilissegundos;

    public boolean permitir(String clienteId) {
        long agora = System.currentTimeMillis();
        Deque<Long> timestamps = janelas.computeIfAbsent(clienteId, k -> new ArrayDeque<>());
        synchronized (timestamps) {
            while (!timestamps.isEmpty() && agora - timestamps.peekFirst() > janelaMilissegundos) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= maxRequisicoes) {
                return false;
            }
            timestamps.addLast(agora);
            return true;
        }
    }

    public Map<String, Object> stats(String clienteId) {
        Deque<Long> ts = janelas.getOrDefault(clienteId, new ArrayDeque<>());
        return Map.of(
                "cliente", clienteId,
                "requisicoes_usadas", ts.size(),
                "limite", maxRequisicoes,
                "janela_ms", janelaMilissegundos
        );
    }
}
