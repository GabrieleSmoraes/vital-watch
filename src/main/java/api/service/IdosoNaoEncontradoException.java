package api.service;

// CAMADA: Service
// RESPONSABILIDADE: avisar que um idoso não existe. Herda de
//                   RecursoNaoEncontradoException, por isso vira 404.
// SITUAÇÃO: implementada.
public class IdosoNaoEncontradoException extends RecursoNaoEncontradoException {

    public IdosoNaoEncontradoException(Long id) {
        super("Idoso não encontrado: " + id);
    }
}
