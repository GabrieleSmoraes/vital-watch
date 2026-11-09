package api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: uma medição de batimentos enviada pelo relógio de um idoso,
//                   gravada na tabela medicoes (migration V2).
// SITUAÇÃO: implementada.
@Entity
@Table(name = "medicoes")
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitas medições podem ser do mesmo idoso. A ligação fica na coluna idoso_id.
    @ManyToOne
    @JoinColumn(name = "idoso_id", nullable = false)
    private Idoso idoso;

    @Column(nullable = false)
    private Integer batimentos;

    @Column(name = "data_hora_medicao", nullable = false)
    private LocalDateTime dataHoraMedicao;

    @Column(name = "em_exercicio", nullable = false)
    private Boolean emExercicio;

}
