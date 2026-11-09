package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.IdosoRequestDTO;
import api.dto.IdosoResponseDTO;
import api.model.Idoso;

// CAMADA: Mapper
// RESPONSABILIDADE: converter IdosoRequestDTO em Idoso, e Idoso em IdosoResponseDTO.
// SITUAÇÃO: implementada.
@Component
public class IdosoMapper {

    // Os limites vêm separados porque quem decide o valor final é o Service (R1).
    public Idoso toEntity(IdosoRequestDTO dto, Integer limiteMinimo, Integer limiteMaximo) {
        return new Idoso(dto.nome(), dto.dataNascimento(), limiteMinimo, limiteMaximo);
    }

    public IdosoResponseDTO toResponse(Idoso idoso) {
        return new IdosoResponseDTO(idoso.getId(), idoso.getNome(), idoso.getDataNascimento(),
                idoso.getLimiteMinimo(), idoso.getLimiteMaximo());
    }

    public List<IdosoResponseDTO> toResponseList(List<Idoso> idosos) {
        return idosos.stream().map(this::toResponse).toList();
    }
}
