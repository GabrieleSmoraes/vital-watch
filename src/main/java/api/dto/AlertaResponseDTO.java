package api.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre um alerta, com a medição que o
//                   gerou e o idoso dentro dela.
// SITUAÇÃO: implementada.
@Schema(description = "Um alerta de batimento fora dos limites")
public record AlertaResponseDTO(

        @Schema(description = "Id do alerta", example = "1")
        Long id,

        @Schema(description = "A medição que gerou o alerta")
        MedicaoResponseDTO medicao,

        @Schema(description = "ALTO (acima do limite máximo) ou BAIXO (abaixo do limite mínimo)", example = "ALTO")
        String tipo,

        @Schema(description = "O limite que a medição ultrapassou", example = "100")
        Integer limite,

        @Schema(description = "Se a família já encerrou o alerta", example = "false")
        Boolean encerrado,

        @Schema(description = "O que foi feito. Vazio enquanto o alerta está aberto",
                example = "Liguei para ela, estava bem e tinha subido a escada")
        String providencia,

        @Schema(description = "Quando o alerta foi encerrado. Vazio enquanto está aberto",
                example = "2026-11-09T09:00:00")
        LocalDateTime dataHoraEncerramento
) {
}
