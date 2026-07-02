package br.edu.iftm.ed.edumetrics.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "alunos", indexes = {
        @Index(name = "idx_aluno_matricula", columnList = "matricula", unique = true),
        @Index(name = "idx_aluno_nome", columnList = "nome")
})
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String matricula;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String curso;

    @Column(nullable = false)
    private Integer periodo;

    @OneToMany(mappedBy = "aluno", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Desempenho> desempenhos = new ArrayList<>();

    public Aluno() {
    }

    public Aluno(String matricula, String nome, String email, String curso, Integer periodo) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.curso = curso;
        this.periodo = periodo;
    }

    public Long getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public List<Desempenho> getDesempenhos() {
        return desempenhos;
    }

    public void setDesempenhos(List<Desempenho> desempenhos) {
        this.desempenhos = desempenhos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno aluno)) return false;
        return Objects.equals(id, aluno.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
