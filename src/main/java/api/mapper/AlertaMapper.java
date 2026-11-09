package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.AlertaResponseDTO;
import api.model.Alerta;

// CAMADA: Mapper
// RESPONSABILIDADE: converter Alerta em AlertaResponseDTO, montando a medição de
//                   dentro da resposta com o MedicaoMapper.
// SITUAÇÃO: implementada.
@Component
public class AlertaMapper {

    private final MedicaoMapper medicaoMapper;

    public AlertaMapper(MedicaoMapper medicaoMapper) {
        this.medicaoMapper = medicaoMapper;
    }

    public AlertaResponseDTO toResponse(Alerta alerta) {
        return new AlertaResponseDTO(
                alerta.getId(),
                // A medição de um alerta sempre gerou alerta: por isso o true.
                medicaoMapper.toResponse(alerta.getMedicao(), true),
                alerta.getTipo(),
                alerta.getLimite(),
                alerta.estaEncerrado(),
                alerta.getProvidencia(),
                alerta.getDataHoraEncerramento());
    }

    public List<AlertaResponseDTO> toResponseList(List<Alerta> alertas) {
        return alertas.stream().map(this::toResponse).toList();
    }
}
