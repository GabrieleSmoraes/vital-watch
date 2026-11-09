package api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.AlertaRequestDTO;
import api.dto.AlertaResponseDTO;
import api.dto.ErroDTO;
import api.service.AlertaService;

// CAMADA: Controller
// RESPONSABILIDADE: receber as requisições de /alertas: a família consulta os
//                   alertas e encerra cada um. A regra R4 não está aqui: fica no
//                   AlertaService.
// ROTAS ATENDIDAS: GET /alertas, GET /alertas/{id} e PUT /alertas/{id}/encerrar.
// SITUAÇÃO: implementada.
@Tag(name = "Alertas", description = "Alertas gerados pelas medições fora dos limites, com a regra R4")
@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista os alertas")
    @ApiResponse(responseCode = "200", description = "Lista de alertas (pode vir vazia)")
    @GetMapping
    public List<AlertaResponseDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Busca um alerta pelo id")
    @ApiResponse(responseCode = "200", description = "Alerta encontrado")
    @ApiResponse(responseCode = "404", description = "Não existe alerta com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @GetMapping("/{id}")
    public AlertaResponseDTO buscarPorId(@Parameter(description = "Id do alerta", example = "1") @PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Encerra um alerta",
            description = "R4: é preciso informar o que foi feito, e um alerta encerrado não pode ser encerrado de novo.")
    @ApiResponse(responseCode = "200", description = "Alerta encerrado")
    @ApiResponse(responseCode = "400", description = "Faltou dizer o que foi feito, ou o alerta já estava encerrado (R4)",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "Não existe alerta com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PutMapping("/{id}/encerrar")
    public AlertaResponseDTO encerrar(@Parameter(description = "Id do alerta", example = "1") @PathVariable Long id,
                                      @Valid @RequestBody AlertaRequestDTO dto) {
        return service.encerrar(id, dto);
    }
}
