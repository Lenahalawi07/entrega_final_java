package br.com.fiap.bo;

import br.com.fiap.dao.AcessorioDao;
import br.com.fiap.dao.AdministradorDao;
import br.com.fiap.entities.Acessorio;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;

import java.sql.SQLException;
import java.util.List;

public class AcessorioBo {

    private static final int NOME_MAXIMO = 150;
    private static final int TIPO_MAXIMO = 50;
    private static final int RARIDADE_MAXIMO = 50;
    private static final int ORA_REGISTROS_FILHOS = 2292;
    private static final int URL_MAXIMO = 500;

    public List<Acessorio> listar() throws SQLException, ClassNotFoundException {
        try (AcessorioDao dao = new AcessorioDao()) {
            return dao.listar();
        }
    }

    public Acessorio buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (AcessorioDao dao = new AcessorioDao()) {
            Acessorio acessorio = dao.buscarPorId(id);
            if (acessorio == null) {
                throw new RecursoNaoEncontradoException("Acessorio " + id + " nao encontrado");
            }
            return acessorio;
        }
    }

    public Acessorio cadastrar(Acessorio acessorio) throws SQLException, ClassNotFoundException {
        validarDados(acessorio);
        try (AcessorioDao dao = new AcessorioDao(); AdministradorDao adminDao = new AdministradorDao()) {
            if (adminDao.buscarPorId(acessorio.getIdAdministrador()) == null) {
                throw new RecursoNaoEncontradoException("Administrador " + acessorio.getIdAdministrador() + " nao encontrado");
            }
            dao.inserir(acessorio);
            return acessorio;
        }
    }

    public Acessorio atualizar(int id, Acessorio dados) throws SQLException, ClassNotFoundException {
        validarDados(dados);
        try (AcessorioDao dao = new AcessorioDao()) {
            Acessorio atual = dao.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Acessorio " + id + " nao encontrado");
            }
            atual.setNome(dados.getNome());
            atual.setTipo(dados.getTipo());
            atual.setRaridade(dados.getRaridade());
            atual.setPreco(dados.getPreco());
            if (dados.getImagemUrl() != null) {
                atual.setImagemUrl(dados.getImagemUrl());
            }
            dao.atualizar(atual);
            return atual;
        }
    }

    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (AcessorioDao dao = new AcessorioDao()) {
            int linhas;
            try {
                linhas = dao.deletar(id);
            } catch (SQLException e) {
                if (e.getErrorCode() == ORA_REGISTROS_FILHOS) {
                    throw new ConflitoException("Acessorio ja pertence a usuarios e nao pode ser removido");
                }
                throw e;
            }
            if (linhas == 0) {
                throw new RecursoNaoEncontradoException("Acessorio " + id + " nao encontrado");
            }
        }
    }

    private void validarDados(Acessorio acessorio) {
        if (acessorio == null) {
            throw new ValidacaoException("Dados do acessorio sao obrigatorios");
        }
        acessorio.setNome(limpar(acessorio.getNome(), "Nome", NOME_MAXIMO));
        acessorio.setTipo(limpar(acessorio.getTipo(), "Tipo", TIPO_MAXIMO));
        acessorio.setRaridade(limpar(acessorio.getRaridade(), "Raridade", RARIDADE_MAXIMO));
        if (acessorio.getPreco() < 0) {
            throw new ValidacaoException("Preco nao pode ser negativo");
        }
        if (acessorio.getPreco() < 0) {
            throw new ValidacaoException("Preco nao pode ser negativo");
        }
        if (acessorio.getImagemUrl() != null && acessorio.getImagemUrl().length() > URL_MAXIMO) {   // novo
            throw new ValidacaoException("URL da imagem deve ter no maximo " + URL_MAXIMO + " caracteres");
        }
    }

    private String limpar(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacaoException(campo + " e obrigatorio");
        }
        String limpo = valor.trim();
        if (limpo.length() > maximo) {
            throw new ValidacaoException(campo + " deve ter no maximo " + maximo + " caracteres");
        }
        return limpo;
    }
}