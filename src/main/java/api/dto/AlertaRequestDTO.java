package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o que a família envia para encerrar um alerta. Quem cria o
//                   alerta é a API, quando a medição sai dos limites (R1 e R2),
//                   por isso a entrada é só o que foi feito (R4).
// SITUAÇÃO: implementada.
@Schema(description = "Dados para encerrar um alerta")
public record AlertaRequestDTO(

        @Schema(description = "O que foi feito", example = "Liguei para ela, estava bem e tinha subido a escada")
        @NotBlank(message = "Informe o que foi feito para encerrar o alerta (R4)")
        @Size(max = 255, message = "O que foi feito tem no máximo 255 caracteres")
        String providencia
) {
}
