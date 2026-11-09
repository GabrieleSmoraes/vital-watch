package api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.ErroDTO;
import api.dto.MedicaoRequestDTO;
import api.dto.MedicaoResponseDTO;
import api.service.MedicaoService;

// CAMADA: Controller
// RESPONSABILIDADE: receber as medições enviadas pelo relógio. As regras R1, R2
//                   e R3 não estão aqui: ficam no MedicaoService.
// ROTAS ATENDIDAS: POST /medicoes.
// SITUAÇÃO: implementada em parte. GET /medicoes e GET /medicoes/{id} estão
//           planejadas no contrato.
@Tag(name = "Medições", description = "Batimentos enviados pelo relógio, com as regras R1, R2 e R3")
@RestController
@RequestMapping("/medicoes")
public class MedicaoController {

    private final MedicaoService service;

    public MedicaoController(MedicaoService service) {
        this.service = service;
    }

    // @Valid confere o formato; as regras de negócio quem confere é o Service.
    @Operation(summary = "Registra uma medição",
            description = "R1: fora dos limites do idoso, gera um alerta. "
                    + "R2: em exercício, o limite alto é 220 menos a idade. "
                    + "R3: a mesma medição (idoso, data e hora) não entra duas vezes.")
    @ApiResponse(responseCode = "201", description = "Medição registrada. gerouAlerta diz se ela gerou alerta")
    @ApiResponse(responseCode = "400", description = "Campo inválido ou regra R3 violada",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "Não existe idoso com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping
    public ResponseEntity<MedicaoResponseDTO> criar(@Valid @RequestBody MedicaoRequestDTO dto) {
        MedicaoResponseDTO criada = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }
}
