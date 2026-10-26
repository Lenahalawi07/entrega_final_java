package br.com.fiap.excecoes;

public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException(String mensagem) { super(mensagem); }
}