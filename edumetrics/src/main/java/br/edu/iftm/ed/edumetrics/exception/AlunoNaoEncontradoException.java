package br.edu.iftm.ed.edumetrics.exception;

public class AlunoNaoEncontradoException extends RuntimeException {

    public AlunoNaoEncontradoException(Long id) {
        super("Aluno não encontrado: " + id);
    }

    public AlunoNaoEncontradoException(String matricula) {
        super("Aluno não encontrado: " + matricula);
    }
}
