package api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Alerta;

// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar alertas na tabela alertas.
// SITUAÇÃO: implementada.
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}
