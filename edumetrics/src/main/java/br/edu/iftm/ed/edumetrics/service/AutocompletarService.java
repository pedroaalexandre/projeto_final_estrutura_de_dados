package br.edu.iftm.ed.edumetrics.service;

import br.edu.iftm.ed.edumetrics.estruturas.Trie;
import br.edu.iftm.ed.edumetrics.repository.DisciplinaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutocompletarService {

    private final Trie trie = new Trie();

    @Autowired
    private DisciplinaRepository disciplinaRepo;

    @PostConstruct
    public void carregarDisciplinas() {
        disciplinaRepo.findAll().forEach(d -> trie.inserir(d.getNome()));
    }

    public List<String> sugerir(String prefixo) {
        return trie.autocompletar(prefixo, 10);
    }

    public void indexar(String nomeDisciplina) {
        trie.inserir(nomeDisciplina);
    }
}
