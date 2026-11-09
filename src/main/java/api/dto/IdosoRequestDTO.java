package api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o que o cliente envia para cadastrar um idoso. Não tem id,
//                   porque quem gera é o banco. Os limites são opcionais: quem
//                   não informa fica com o padrão da R1.
// SITUAÇÃO: implementada.
@Schema(description = "Dados para cadastrar um idoso")
public record IdosoRequestDTO(

        @Schema(description = "Nome do idoso", example = "Maria Silva")
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome tem no máximo 100 caracteres")
        String nome,

        @Schema(description = "Data de nascimento", example = "1950-03-15")
        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento precisa estar no passado")
        LocalDate dataNascimento,

        @Schema(description = "Limite mínimo de batimentos. Se não vier, usa 60", example = "50")
        @Positive(message = "Limite mínimo precisa ser maior que zero")
        Integer limiteMinimo,

        @Schema(description = "Limite máximo de batimentos. Se não vier, usa 100", example = "100")
        @Positive(message = "Limite máximo precisa ser maior que zero")
        Integer limiteMaximo
) {
}
