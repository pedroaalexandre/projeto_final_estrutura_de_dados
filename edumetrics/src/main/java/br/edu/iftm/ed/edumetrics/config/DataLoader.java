package br.edu.iftm.ed.edumetrics.config;

import br.edu.iftm.ed.edumetrics.domain.Disciplina;
import br.edu.iftm.ed.edumetrics.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @PostConstruct
    public void init() {
        if (disciplinaRepository.count() == 0) {
            List<Disciplina> disciplinas = List.of(
                    new Disciplina("ED01", "Estrutura de Dados", 4),
                    new Disciplina("ED02", "Programação Web", 4),
                    new Disciplina("ED03", "Banco de Dados", 4),
                    new Disciplina("ED04", "Sistemas Operacionais", 4),
                    new Disciplina("ED05", "Matemática Discreta", 4)
            );
            disciplinaRepository.saveAll(disciplinas);
        }
    }
}
