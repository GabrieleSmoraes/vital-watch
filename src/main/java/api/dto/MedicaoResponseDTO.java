package api.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre uma medição. Na entrada o idoso vem
//                   só pelo id; na saída ele vem completo. O gerouAlerta não
//                   existe na entrada: quem decide é a API (R1 e R2).
// SITUAÇÃO: implementada.
@Schema(description = "Uma medição de batimentos")
public record MedicaoResponseDTO(

        @Schema(description = "Id da medição", example = "1")
        Long id,

        @Schema(description = "O idoso da medição")
        IdosoResponseDTO idoso,

        @Schema(description = "Batimentos por minuto", example = "110")
        Integer batimentos,

        @Schema(description = "Data e hora em que o relógio mediu", example = "2026-11-09T08:30:00")
        LocalDateTime dataHoraMedicao,

        @Schema(description = "Se a pessoa estava se exercitando", example = "false")
        Boolean emExercicio,

        @Schema(description = "Se a medição saiu dos limites e gerou um alerta (R1 e R2)", example = "true")
        Boolean gerouAlerta
) {
}
