package br.com.fiap.excecoes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final int ORA_CHAVE_DUPLICADA = 1;
    private static final int ORA_REGISTROS_FILHOS = 2292;

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroResposta> validacao(ValidacaoException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroResposta> requisicaoInvalida(Exception e) {
        LOG.warn("Requisicao invalida", e);
        return resposta(HttpStatus.BAD_REQUEST, "Requisicao invalida: verifique o JSON e os parametros enviados");
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<ErroResposta> naoAutorizado(NaoAutorizadoException e) {
        return resposta(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResposta> conflito(ConflitoException e) {
        return resposta(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErroResposta> banco(SQLException e) {
        if (e.getErrorCode() == ORA_CHAVE_DUPLICADA) {
            return resposta(HttpStatus.CONFLICT, "Registro ja existe");
        }
        if (e.getErrorCode() == ORA_REGISTROS_FILHOS) {
            return resposta(HttpStatus.CONFLICT, "Registro possui vinculos e nao pode ser removido");
        }
        LOG.error("Erro de banco de dados", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados");
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<ErroResposta> driver(ClassNotFoundException e) {
        LOG.error("Driver do banco nao encontrado", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }

    private ResponseEntity<ErroResposta> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResposta(status.value(), mensagem));
    }
}