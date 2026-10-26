package br.com.fiap.bo;

import br.com.fiap.dao.AdministradorDao;
import br.com.fiap.dao.MissaoDao;
import br.com.fiap.entities.Missao;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;

import java.sql.SQLException;
import java.util.List;

public class MissaoBo {

    private static final int TITULO_MAXIMO = 150;
    private static final int DESCRICAO_MAXIMO = 1000;
    private static final int ORA_REGISTROS_FILHOS = 2292;

    public List<Missao> listar() throws SQLException, ClassNotFoundException {
        try (MissaoDao dao = new MissaoDao()) {
            return dao.listar();
        }
    }

    public List<Missao> listarAtivas() throws SQLException, ClassNotFoundException {
        try (MissaoDao dao = new MissaoDao()) {
            return dao.listarAtivas();
        }
    }

    public Missao buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (MissaoDao dao = new MissaoDao()) {
            Missao missao = dao.buscarPorId(id);
            if (missao == null) {
                throw new RecursoNaoEncontradoException("Missao " + id + " nao encontrada");
            }
            return missao;
        }
    }

    public Missao cadastrar(Missao missao) throws SQLException, ClassNotFoundException {
        validarDados(missao);
        try (MissaoDao dao = new MissaoDao(); AdministradorDao adminDao = new AdministradorDao()) {
            if (adminDao.buscarPorId(missao.getIdAdministrador()) == null) {
                throw new RecursoNaoEncontradoException("Administrador " + missao.getIdAdministrador() + " nao encontrado");
            }
            dao.inserir(missao);
            return missao;
        }
    }

    public Missao atualizar(int id, Missao dados) throws SQLException, ClassNotFoundException {
        validarDados(dados);
        try (MissaoDao dao = new MissaoDao()) {
            Missao atual = dao.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Missao " + id + " nao encontrada");
            }
            atual.setTituloMissao(dados.getTituloMissao());
            atual.setDescricaoMissao(dados.getDescricaoMissao());
            atual.setAtivo(dados.getAtivo());
            atual.setValorMoedasMissao(dados.getValorMoedasMissao());
            dao.atualizar(atual);
            return atual;
        }
    }

    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (MissaoDao dao = new MissaoDao()) {
            int linhas;
            try {
                linhas = dao.deletar(id);
            } catch (SQLException e) {
                if (e.getErrorCode() == ORA_REGISTROS_FILHOS) {
                    throw new ConflitoException("Missao ja foi atribuida a usuarios e nao pode ser removida");
                }
                throw e;
            }
            if (linhas == 0) {
                throw new RecursoNaoEncontradoException("Missao " + id + " nao encontrada");
            }
        }
    }

    private void validarDados(Missao missao) {
        if (missao == null) {
            throw new ValidacaoException("Dados da missao sao obrigatorios");
        }
        if (missao.getTituloMissao() == null || missao.getTituloMissao().isBlank()) {
            throw new ValidacaoException("Titulo da missao e obrigatorio");
        }
        missao.setTituloMissao(missao.getTituloMissao().trim());
        if (missao.getTituloMissao().length() > TITULO_MAXIMO) {
            throw new ValidacaoException("Titulo deve ter no maximo " + TITULO_MAXIMO + " caracteres");
        }
        if (missao.getDescricaoMissao() != null) {
            missao.setDescricaoMissao(missao.getDescricaoMissao().trim());
            if (missao.getDescricaoMissao().length() > DESCRICAO_MAXIMO) {
                throw new ValidacaoException("Descricao deve ter no maximo " + DESCRICAO_MAXIMO + " caracteres");
            }
        }
        if (missao.getAtivo() == null || missao.getAtivo().isBlank()) {
            missao.setAtivo("S");
        }
        missao.setAtivo(missao.getAtivo().trim().toUpperCase());
        if (!missao.getAtivo().equals("S") && !missao.getAtivo().equals("N")) {
            throw new ValidacaoException("Ativo deve ser S ou N");
        }
        if (missao.getValorMoedasMissao() < 0) {
            throw new ValidacaoException("Valor de moedas nao pode ser negativo");
        }
    }
}