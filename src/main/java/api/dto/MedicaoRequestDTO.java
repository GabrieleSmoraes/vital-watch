package api.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// CAMADA: DTO
// RESPONSABILIDADE: o que o relógio envia a cada medição. O idoso vem só pelo id:
//                   quem busca o idoso no banco é o Service.
// SITUAÇÃO: implementada.
@Schema(description = "Dados de uma medição enviada pelo relógio")
public record MedicaoRequestDTO(

        @Schema(description = "Id do idoso", example = "1")
        @NotNull(message = "Id do idoso é obrigatório")
        Long idosoId,

        @Schema(description = "Batimentos por minuto", example = "110")
        @NotNull(message = "Batimentos é obrigatório")
        @Positive(message = "Batimentos precisa ser maior que zero")
        Integer batimentos,

        @Schema(description = "Data e hora em que o relógio mediu", example = "2026-11-09T08:30:00")
        @NotNull(message = "Data e hora da medição é obrigatória")
        LocalDateTime dataHoraMedicao,

        @Schema(description = "Se a pessoa estava se exercitando", example = "false")
        @NotNull(message = "Informe se a pessoa estava se exercitando")
        Boolean emExercicio
) {
}
