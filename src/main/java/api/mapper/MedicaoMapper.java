package api.mapper;

import org.springframework.stereotype.Component;

import api.dto.MedicaoRequestDTO;
import api.dto.MedicaoResponseDTO;
import api.model.Idoso;
import api.model.Medicao;

// CAMADA: Mapper
// RESPONSABILIDADE: converter MedicaoRequestDTO em Medicao, e Medicao em
//                   MedicaoResponseDTO, montando o idoso de dentro da resposta
//                   com o IdosoMapper. O gerouAlerta chega pronto: quem decide é
//                   o MedicaoService, porque é regra de negócio (R1 e R2).
// SITUAÇÃO: implementada.
@Component
public class MedicaoMapper {

    private final IdosoMapper idosoMapper;

    public MedicaoMapper(IdosoMapper idosoMapper) {
        this.idosoMapper = idosoMapper;
    }

    // O idoso vem separado porque quem busca o idoso no banco é o Service.
    public Medicao toEntity(MedicaoRequestDTO dto, Idoso idoso) {
        return new Medicao(idoso, dto.batimentos(), dto.dataHoraMedicao(), dto.emExercicio());
    }

    // Entra a entidade, sai o DTO com o idoso completo.
    public MedicaoResponseDTO toResponse(Medicao medicao, boolean gerouAlerta) {
        return new MedicaoResponseDTO(
                medicao.getId(),
                idosoMapper.toResponse(medicao.getIdoso()),
                medicao.getBatimentos(),
                medicao.getDataHoraMedicao(),
                medicao.getEmExercicio(),
                gerouAlerta);
    }
}
