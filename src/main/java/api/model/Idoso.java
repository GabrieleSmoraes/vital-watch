package api.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: um idoso acompanhado pelo sistema, gravado na tabela idosos
//                   (migration V1).
// SITUAÇÃO: implementada.
@Entity
@Table(name = "idosos")
public class Idoso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "limite_minimo", nullable = false)
    private Integer limiteMinimo;

    @Column(name = "limite_maximo", nullable = false)
    private Integer limiteMaximo;

    // O Hibernate precisa deste construtor vazio para montar o objeto a partir do banco.
    protected Idoso() {
    }

    public Idoso(String nome, LocalDate dataNascimento, Integer limiteMinimo, Integer limiteMaximo) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.limiteMinimo = limiteMinimo;
        this.limiteMaximo = limiteMaximo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Integer getLimiteMinimo() {
        return limiteMinimo;
    }

    public Integer getLimiteMaximo() {
        return limiteMaximo;
    }
}
