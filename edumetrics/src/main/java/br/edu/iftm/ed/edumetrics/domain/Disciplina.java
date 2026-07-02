package br.edu.iftm.ed.edumetrics.domain;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "disciplinas", indexes = {
        @Index(name = "idx_disc_codigo", columnList = "codigo", unique = true)
})
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer creditos;

    public Disciplina() {
    }

    public Disciplina(String codigo, String nome, Integer creditos) {
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disciplina disciplina)) return false;
        return Objects.equals(id, disciplina.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
