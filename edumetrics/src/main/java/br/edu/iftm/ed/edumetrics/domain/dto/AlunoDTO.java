package br.edu.iftm.ed.edumetrics.domain.dto;

import java.io.Serializable;

public record AlunoDTO(
        Long id,
        String matricula,
        String nome,
        String email,
        String curso,
        Integer periodo
) implements Serializable {
}
