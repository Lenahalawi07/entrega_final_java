package br.com.fiap.bo;

import br.com.fiap.dao.MissaoDao;
import br.com.fiap.dao.UsuarioDao;
import br.com.fiap.dao.UsuarioMissaoDao;
import br.com.fiap.entities.Missao;
import br.com.fiap.entities.UsuarioMissao;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;

import java.sql.SQLException;
import java.util.List;

public class UsuarioMissaoBo {

    public List<UsuarioMissao> listarPorUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
        try (UsuarioMissaoDao dao = new UsuarioMissaoDao(); UsuarioDao usuarioDao = new UsuarioDao()) {
            if (usuarioDao.buscarPorId(idUsuario) == null) {
                throw new RecursoNaoEncontradoException("Usuario " + idUsuario + " nao encontrado");
            }
            return dao.listarPorUsuario(idUsuario);
        }
    }

    public UsuarioMissao buscar(int idUsuario, int idMissao) throws SQLException, ClassNotFoundException {
        try (UsuarioMissaoDao dao = new UsuarioMissaoDao()) {
            return buscarOuFalhar(dao, idUsuario, idMissao);
        }
    }

    public UsuarioMissao atribuir(int idUsuario, int idMissao) throws SQLException, ClassNotFoundException {
        try (UsuarioMissaoDao dao = new UsuarioMissaoDao();
             UsuarioDao usuarioDao = new UsuarioDao();
             MissaoDao missaoDao = new MissaoDao()) {
            if (usuarioDao.buscarPorId(idUsuario) == null) {
                throw new RecursoNaoEncontradoException("Usuario " + idUsuario + " nao encontrado");
            }
            Missao missao = missaoDao.buscarPorId(idMissao);
            if (missao == null) {
                throw new RecursoNaoEncontradoException("Missao " + idMissao + " nao encontrada");
            }
            if (!missao.estaDisponivel()) {
                throw new ValidacaoException("Missao " + idMissao + " nao esta ativa");
            }
            if (dao.buscar(idMissao, idUsuario) != null) {
                throw new ConflitoException("Usuario ja possui esta missao");
            }
            UsuarioMissao nova = new UsuarioMissao();
            nova.setIdUsuario(idUsuario);
            nova.setIdMissao(idMissao);
            dao.inserir(nova);
            return dao.buscar(idMissao, idUsuario);
        }
    }

    public UsuarioMissao atualizarProgresso(int idUsuario, int idMissao, int progresso)
            throws SQLException, ClassNotFoundException {
        if (progresso < 0 || progresso > 99) {
            throw new ValidacaoException("Progresso deve ficar entre 0 e 99. Para finalizar, conclua a missao");
        }
        try (UsuarioMissaoDao dao = new UsuarioMissaoDao()) {
            UsuarioMissao atual = buscarOuFalhar(dao, idUsuario, idMissao);
            if ("S".equals(atual.getMissaoConcluida())) {
                throw new ConflitoException("Missao ja foi concluida");
            }
            atual.setProgressoMissao(progresso);
            dao.atualizar(atual);
            return atual;
        }
    }

    public UsuarioMissao concluir(int idUsuario, int idMissao) throws SQLException, ClassNotFoundException {
        try (UsuarioMissaoDao dao = new UsuarioMissaoDao(); MissaoDao missaoDao = new MissaoDao()) {
            UsuarioMissao atual = buscarOuFalhar(dao, idUsuario, idMissao);
            if ("S".equals(atual.getMissaoConcluida())) {
                throw new ConflitoException("Missao ja foi concluida");
            }
            Missao missao = missaoDao.buscarPorId(idMissao);
            if (missao == null) {
                throw new RecursoNaoEncontradoException("Missao " + idMissao + " nao encontrada");
            }
            if (!missao.estaDisponivel()) {
                throw new ValidacaoException("Missao " + idMissao + " nao esta ativa");
            }
            int linhas = dao.concluirMissao(idUsuario, idMissao, missao.getValorMoedasMissao());
            if (linhas == 0) {
                throw new ConflitoException("Missao ja foi concluida");
            }
            return dao.buscar(idMissao, idUsuario);
        }
    }

    public void deletar(int idUsuario, int idMissao) throws SQLException, ClassNotFoundException {
        try (UsuarioMissaoDao dao = new UsuarioMissaoDao()) {
            UsuarioMissao atual = buscarOuFalhar(dao, idUsuario, idMissao);
            if ("S".equals(atual.getMissaoConcluida())) {
                throw new ConflitoException("Missao concluida nao pode ser removida");
            }
            dao.deletar(idMissao, idUsuario);
        }
    }

    private UsuarioMissao buscarOuFalhar(UsuarioMissaoDao dao, int idUsuario, int idMissao) throws SQLException {
        UsuarioMissao um = dao.buscar(idMissao, idUsuario);
        if (um == null) {
            throw new RecursoNaoEncontradoException(
                    "Usuario " + idUsuario + " nao possui a missao " + idMissao);
        }
        return um;
    }
}