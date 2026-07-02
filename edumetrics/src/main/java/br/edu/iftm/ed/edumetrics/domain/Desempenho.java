package br.edu.iftm.ed.edumetrics.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "desempenhos", indexes = {
        @Index(name = "idx_desemp_aluno", columnList = "aluno_id"),
        @Index(name = "idx_desemp_aluno_disc", columnList = "aluno_id, disciplina_id")
})
public class Desempenho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(precision = 5, scale = 2)
    private BigDecimal nota1;

    @Column(precision = 5, scale = 2)
    private BigDecimal nota2;

    @Column(precision = 5, scale = 2)
    private BigDecimal notaFinal;

    @Column(nullable = false, length = 7)
    private String semestre;

    public Desempenho() {
    }

    public Desempenho(Aluno aluno, Disciplina disciplina, BigDecimal nota1, BigDecimal nota2, String semestre) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.notaFinal = calcularNotaFinal(nota1, nota2);
        this.semestre = semestre;
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public BigDecimal getNota1() {
        return nota1;
    }

    public void setNota1(BigDecimal nota1) {
        this.nota1 = nota1;
        this.notaFinal = calcularNotaFinal(this.nota1, this.nota2);
    }

    public BigDecimal getNota2() {
        return nota2;
    }

    public void setNota2(BigDecimal nota2) {
        this.nota2 = nota2;
        this.notaFinal = calcularNotaFinal(this.nota1, this.nota2);
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    private BigDecimal calcularNotaFinal(BigDecimal nota1, BigDecimal nota2) {
        if (nota1 == null || nota2 == null) {
            return null;
        }
        return nota1.add(nota2).divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Desempenho desempenho)) return false;
        return Objects.equals(id, desempenho.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
