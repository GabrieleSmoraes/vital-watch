package api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.ErroDTO;
import api.dto.IdosoRequestDTO;
import api.dto.IdosoResponseDTO;
import api.service.IdosoService;

// CAMADA: Controller
// RESPONSABILIDADE: receber as requisições de /idosos, validar o DTO de entrada e
//                   devolver o status certo. Não tem regra de negócio.
// ROTAS ATENDIDAS: POST /idosos, GET /idosos e GET /idosos/{id}.
// SITUAÇÃO: implementada.
@Tag(name = "Idosos", description = "Cadastro e consulta dos idosos acompanhados")
@RestController
@RequestMapping("/idosos")
public class IdosoController {

    private final IdosoService service;

    public IdosoController(IdosoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastra um idoso", description = "Sem limites informados, usa 60 e 100 (R1).")
    @ApiResponse(responseCode = "201", description = "Idoso cadastrado")
    @ApiResponse(responseCode = "400", description = "Campo inválido ou regra R1 violada",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping
    public ResponseEntity<IdosoResponseDTO> criar(@Valid @RequestBody IdosoRequestDTO dto) {
        IdosoResponseDTO criado = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Lista os idosos")
    @ApiResponse(responseCode = "200", description = "Lista de idosos (pode vir vazia)")
    @GetMapping
    public List<IdosoResponseDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Busca um idoso pelo id")
    @ApiResponse(responseCode = "200", description = "Idoso encontrado")
    @ApiResponse(responseCode = "404", description = "Não existe idoso com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @GetMapping("/{id}")
    public IdosoResponseDTO buscarPorId(@Parameter(description = "Id do idoso", example = "1") @PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
