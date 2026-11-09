package api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre um idoso.
// SITUAÇÃO: implementada.
@Schema(description = "Um idoso acompanhado pelo sistema")
public record IdosoResponseDTO(

        @Schema(description = "Id do idoso", example = "1")
        Long id,

        @Schema(description = "Nome do idoso", example = "Maria Silva")
        String nome,

        @Schema(description = "Data de nascimento", example = "1950-03-15")
        LocalDate dataNascimento,

        @Schema(description = "Limite mínimo de batimentos", example = "50")
        Integer limiteMinimo,

        @Schema(description = "Limite máximo de batimentos", example = "100")
        Integer limiteMaximo
) {
}
