package api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import api.dto.IdosoRequestDTO;
import api.dto.IdosoResponseDTO;
import api.mapper.IdosoMapper;
import api.model.Idoso;
import api.repository.IdosoRepository;

// CAMADA: Service
// RESPONSABILIDADE: cadastrar, listar e buscar idosos.
// REGRAS: R1, na parte do cadastro: limites padrão e mínimo menor que o máximo.
// ROTAS ATENDIDAS: POST /idosos, GET /idosos e GET /idosos/{id}.
// SITUAÇÃO: implementada.
@Service
public class IdosoService {

    private static final int LIMITE_MINIMO_PADRAO = 60;
    private static final int LIMITE_MAXIMO_PADRAO = 100;

    private final IdosoRepository repository;
    private final IdosoMapper mapper;

    public IdosoService(IdosoRepository repository, IdosoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public IdosoResponseDTO criar(IdosoRequestDTO dto) {
        // R1: quem não informa os limites fica com o padrão.
        Integer minimo = dto.limiteMinimo() != null ? dto.limiteMinimo() : LIMITE_MINIMO_PADRAO;
        Integer maximo = dto.limiteMaximo() != null ? dto.limiteMaximo() : LIMITE_MAXIMO_PADRAO;

        // R1: o limite mínimo precisa ser menor que o máximo.
        if (minimo >= maximo) {
            throw new RegraDeNegocioException("R1: o limite mínimo precisa ser menor que o limite máximo");
        }

        Idoso salvo = repository.save(mapper.toEntity(dto, minimo, maximo));
        return mapper.toResponse(salvo);
    }

    public List<IdosoResponseDTO> listarTodos() {
        return mapper.toResponseList(repository.findAll());
    }

    public IdosoResponseDTO buscarPorId(Long id) {
        Idoso idoso = repository.findById(id).orElseThrow(() -> new IdosoNaoEncontradoException(id));
        return mapper.toResponse(idoso);
    }
}
