package api.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.AlertaRequestDTO;
import api.dto.AlertaResponseDTO;
import api.mapper.AlertaMapper;
import api.model.Alerta;
import api.repository.AlertaRepository;

// CAMADA: Service
// RESPONSABILIDADE: listar e buscar os alertas, e encerrar cada um com o que foi
//                   feito. Quem cria os alertas é o MedicaoService (R1 e R2).
// REGRAS: R4 (alerta encerrado não pode ser encerrado de novo).
// ROTAS ATENDIDAS: GET /alertas, GET /alertas/{id} e PUT /alertas/{id}/encerrar.
// SITUAÇÃO: implementada.
@Service
public class AlertaService {

    private final AlertaRepository repository;
    private final AlertaMapper mapper;

    public AlertaService(AlertaRepository repository, AlertaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AlertaResponseDTO> listarTodos() {
        return mapper.toResponseList(repository.findAll());
    }

    public AlertaResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarAlerta(id));
    }

    // @Transactional: no fim do método, o Hibernate grava sozinho o que mudou no alerta.
    @Transactional
    public AlertaResponseDTO encerrar(Long id, AlertaRequestDTO dto) {
        Alerta alerta = buscarAlerta(id);

        // R4: um alerta encerrado não pode ser encerrado de novo.
        if (alerta.estaEncerrado()) {
            throw new RegraDeNegocioException("R4: o alerta " + id + " já foi encerrado");
        }

        // A data do encerramento quem decide é a API, não o cliente.
        alerta.encerrar(dto.providencia(), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return mapper.toResponse(alerta);
    }

    // 404 se o alerta não existir.
    private Alerta buscarAlerta(Long id) {
        return repository.findById(id).orElseThrow(() -> new AlertaNaoEncontradoException(id));
    }
}
