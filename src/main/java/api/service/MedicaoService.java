package api.service;

import java.time.Period;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.MedicaoRequestDTO;
import api.dto.MedicaoResponseDTO;
import api.mapper.MedicaoMapper;
import api.model.Alerta;
import api.model.Idoso;
import api.model.Medicao;
import api.repository.AlertaRepository;
import api.repository.IdosoRepository;
import api.repository.MedicaoRepository;

// CAMADA: Service
// RESPONSABILIDADE: registrar as medições enviadas pelo relógio e gerar o alerta
//                   quando o batimento sai dos limites do idoso.
// REGRAS: R1 (medição fora dos limites gera alerta),
//         R2 (em exercício, o limite alto passa a ser 220 menos a idade) e
//         R3 (medição repetida é recusada).
// ROTAS ATENDIDAS: POST /medicoes.
// SITUAÇÃO: implementada em parte. Só o registro da medição, que as regras usam.
@Service
public class MedicaoService {

    // R2: a conta da frequência máxima em exercício é 220 menos a idade.
    private static final int FREQUENCIA_MAXIMA_BASE = 220;

    private final MedicaoRepository repository;
    private final IdosoRepository idosoRepository;
    private final AlertaRepository alertaRepository;
    private final MedicaoMapper mapper;

    public MedicaoService(MedicaoRepository repository, IdosoRepository idosoRepository,
                          AlertaRepository alertaRepository, MedicaoMapper mapper) {
        this.repository = repository;
        this.idosoRepository = idosoRepository;
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    // @Transactional: gravar a medição e o alerta acontecem juntos. Se uma parte
    // falhar, nenhuma fica gravada.
    @Transactional
    public MedicaoResponseDTO criar(MedicaoRequestDTO dto) {
        // Entra o id, o Service busca o idoso: 404 se ele não existir.
        Idoso idoso = idosoRepository.findById(dto.idosoId())
                .orElseThrow(() -> new IdosoNaoEncontradoException(dto.idosoId()));

        // R3: a mesma medição não pode ser registrada duas vezes.
        if (repository.existsByIdosoIdAndDataHoraMedicao(idoso.getId(), dto.dataHoraMedicao())) {
            throw new RegraDeNegocioException("R3: já existe uma medição deste idoso com a mesma data e hora");
        }

        Medicao salva = repository.save(mapper.toEntity(dto, idoso));
        boolean gerouAlerta = gerarAlertaSeForaDosLimites(salva);
        return mapper.toResponse(salva, gerouAlerta);
    }

    // R1 e R2: compara o batimento com os limites do idoso e grava um alerta se
    // ele sair dos limites. Devolve se gerou alerta.
    private boolean gerarAlertaSeForaDosLimites(Medicao medicao) {
        Idoso idoso = medicao.getIdoso();
        int batimentos = medicao.getBatimentos();

        // R1: abaixo do limite mínimo. O mínimo é o mesmo com ou sem exercício (R2).
        if (batimentos < idoso.getLimiteMinimo()) {
            alertaRepository.save(new Alerta(medicao, "BAIXO", idoso.getLimiteMinimo()));
            return true;
        }

        // R1: o limite alto é o máximo cadastrado no idoso...
        int limiteMaximo = idoso.getLimiteMaximo();
        // R2: ...mas, em exercício, passa a ser 220 menos a idade no dia da medição.
        if (medicao.getEmExercicio()) {
            int idade = Period.between(idoso.getDataNascimento(), medicao.getDataHoraMedicao().toLocalDate())
                    .getYears();
            limiteMaximo = FREQUENCIA_MAXIMA_BASE - idade;
        }

        // R1: acima do limite alto.
        if (batimentos > limiteMaximo) {
            alertaRepository.save(new Alerta(medicao, "ALTO", limiteMaximo));
            return true;
        }

        // Dentro dos limites: a medição fica gravada, sem alerta.
        return false;
    }
}
