package api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: um alerta gerado por uma medição fora dos limites do idoso,
//                   gravado na tabela alertas (migration V3). Nasce aberto e é
//                   encerrado pela família, que informa o que foi feito.
// SITUAÇÃO: implementada.
@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada alerta vem de uma medição, e uma medição gera no máximo um alerta.
    // A ligação fica na coluna medicao_id.
    @OneToOne
    @JoinColumn(name = "medicao_id", nullable = false, unique = true)
    private Medicao medicao;

    // ALTO (acima do limite máximo) ou BAIXO (abaixo do limite mínimo).
    @Column(nullable = false, length = 10)
    private String tipo;

    // O limite que a medição ultrapassou. Em exercício, o alto é 220 menos a idade (R2).
    @Column(nullable = false)
    private Integer limite;

    // O que a família fez. Fica vazio enquanto o alerta está aberto.
    @Column(length = 255)
    private String providencia;

    // Quando o alerta foi encerrado. Fica vazio enquanto o alerta está aberto.
    @Column(name = "data_hora_encerramento")
    private LocalDateTime dataHoraEncerramento;

    // O Hibernate precisa deste construtor vazio para montar o objeto a partir do banco.
    protected Alerta() {
    }

    // Todo alerta nasce aberto: sem providência e sem data de encerramento.
    public Alerta(Medicao medicao, String tipo, Integer limite) {
        this.medicao = medicao;
        this.tipo = tipo;
        this.limite = limite;
    }

    // R4: o alerta está encerrado quando já tem a data de encerramento.
    public boolean estaEncerrado() {
        return dataHoraEncerramento != null;
    }

    public void encerrar(String providencia, LocalDateTime dataHoraEncerramento) {
        this.providencia = providencia;
        this.dataHoraEncerramento = dataHoraEncerramento;
    }

    public Long getId() {
        return id;
    }

    public Medicao getMedicao() {
        return medicao;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getLimite() {
        return limite;
    }

    public String getProvidencia() {
        return providencia;
    }

    public LocalDateTime getDataHoraEncerramento() {
        return dataHoraEncerramento;
    }
}
