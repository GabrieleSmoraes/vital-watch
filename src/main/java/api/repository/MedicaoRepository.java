package api.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Medicao;

// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar medições na tabela medicoes.
// SITUAÇÃO: implementada.
public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    // R3: diz se já existe uma medição deste idoso com a mesma data e hora.
    boolean existsByIdosoIdAndDataHoraMedicao(Long idosoId, LocalDateTime dataHoraMedicao);
}
