package api.service;

// CAMADA: Service
// RESPONSABILIDADE: avisar que um alerta não existe. Herda de
//                   RecursoNaoEncontradoException, por isso vira 404.
// SITUAÇÃO: implementada.
public class AlertaNaoEncontradoException extends RecursoNaoEncontradoException {

    public AlertaNaoEncontradoException(Long id) {
        super("Alerta não encontrado: " + id);
    }
}
