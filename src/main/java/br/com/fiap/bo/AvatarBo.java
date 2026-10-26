package br.com.fiap.bo;

import br.com.fiap.dao.AvatarDao;
import br.com.fiap.dao.UsuarioDao;
import br.com.fiap.entities.Avatar;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;

import java.sql.SQLException;
import java.util.List;

public class AvatarBo {

    private static final int NOME_MAXIMO = 150;

    public List<Avatar> listar() throws SQLException, ClassNotFoundException {
        try (AvatarDao dao = new AvatarDao()) {
            return dao.listar();
        }
    }

    public Avatar buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (AvatarDao dao = new AvatarDao()) {
            Avatar avatar = dao.buscarPorId(id);
            if (avatar == null) {
                throw new RecursoNaoEncontradoException("Avatar " + id + " nao encontrado");
            }
            return avatar;
        }
    }

    public Avatar buscarPorUsuarioId(int idUsuario) throws SQLException, ClassNotFoundException {
        try (AvatarDao dao = new AvatarDao()) {
            Avatar avatar = dao.buscarPorUsuarioId(idUsuario);
            if (avatar == null) {
                throw new RecursoNaoEncontradoException("Usuario " + idUsuario + " nao possui avatar");
            }
            return avatar;
        }
    }

    public Avatar cadastrar(Avatar avatar) throws SQLException, ClassNotFoundException {
        validarNome(avatar);
        try (AvatarDao dao = new AvatarDao(); UsuarioDao usuarioDao = new UsuarioDao()) {
            if (usuarioDao.buscarPorId(avatar.getIdUsuario()) == null) {
                throw new RecursoNaoEncontradoException("Usuario " + avatar.getIdUsuario() + " nao encontrado");
            }
            if (dao.buscarPorUsuarioId(avatar.getIdUsuario()) != null) {
                throw new ConflitoException("Usuario ja possui um avatar");
            }
            dao.inserir(avatar);
            return dao.buscarPorUsuarioId(avatar.getIdUsuario());
        }
    }

    public Avatar atualizar(int id, Avatar dados) throws SQLException, ClassNotFoundException {
        validarNome(dados);
        try (AvatarDao dao = new AvatarDao()) {
            Avatar atual = dao.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Avatar " + id + " nao encontrado");
            }
            atual.setNome(dados.getNome());
            dao.atualizar(atual);
            return atual;
        }
    }

    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (AvatarDao dao = new AvatarDao()) {
            if (dao.deletar(id) == 0) {
                throw new RecursoNaoEncontradoException("Avatar " + id + " nao encontrado");
            }
        }
    }

    private void validarNome(Avatar avatar) {
        if (avatar == null) {
            throw new ValidacaoException("Dados do avatar sao obrigatorios");
        }
        if (avatar.getNome() == null || avatar.getNome().isBlank()) {
            throw new ValidacaoException("Nome do avatar e obrigatorio");
        }
        avatar.setNome(avatar.getNome().trim());
        if (avatar.getNome().length() > NOME_MAXIMO) {
            throw new ValidacaoException("Nome do avatar deve ter no maximo " + NOME_MAXIMO + " caracteres");
        }
    }
}