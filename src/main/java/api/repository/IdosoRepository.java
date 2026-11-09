package api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Idoso;

// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar idosos na tabela idosos.
// SITUAÇÃO: implementada.
public interface IdosoRepository extends JpaRepository<Idoso, Long> {
}
