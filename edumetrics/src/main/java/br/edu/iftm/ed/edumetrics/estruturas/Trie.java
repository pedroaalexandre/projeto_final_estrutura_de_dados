package br.edu.iftm.ed.edumetrics.estruturas;

import java.util.*;

public class Trie {

    private static class No {
        final Map<Character, No> filhos = new HashMap<>();
        boolean fimDePalavra = false;
        String valorCompleto;
    }

    private final No raiz = new No();
    private int totalPalavras = 0;

    public void inserir(String palavra) {
        if (palavra == null || palavra.isBlank()) {
            return;
        }
        No atual = raiz;
        String texto = palavra.toLowerCase();
        for (char c : texto.toCharArray()) {
            atual = atual.filhos.computeIfAbsent(c, k -> new No());
        }
        if (!atual.fimDePalavra) {
            atual.fimDePalavra = true;
            atual.valorCompleto = palavra;
            totalPalavras++;
        }
    }

    public List<String> autocompletar(String prefixo, int maxResultados) {
        if (prefixo == null || prefixo.isBlank() || maxResultados <= 0) {
            return Collections.emptyList();
        }
        No no = descerAte(prefixo.toLowerCase());
        if (no == null) {
            return Collections.emptyList();
        }
        List<String> resultados = new ArrayList<>();
        coletarPalavras(no, resultados, maxResultados);
        return Collections.unmodifiableList(resultados);
    }

    private No descerAte(String prefixo) {
        No atual = raiz;
        for (char c : prefixo.toCharArray()) {
            atual = atual.filhos.get(c);
            if (atual == null) {
                return null;
            }
        }
        return atual;
    }

    private void coletarPalavras(No no, List<String> resultado, int max) {
        if (resultado.size() >= max) {
            return;
        }
        if (no.fimDePalavra) {
            resultado.add(no.valorCompleto);
        }
        for (No filho : no.filhos.values()) {
            coletarPalavras(filho, resultado, max);
            if (resultado.size() >= max) {
                return;
            }
        }
    }

    public int size() {
        return totalPalavras;
    }

    public boolean isEmpty() {
        return totalPalavras == 0;
    }
}
