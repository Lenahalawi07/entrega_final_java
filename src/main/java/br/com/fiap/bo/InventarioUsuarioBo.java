package br.com.fiap.bo;

import br.com.fiap.dao.AcessorioDao;
import br.com.fiap.dao.InventarioUsuarioDao;
import br.com.fiap.dao.UsuarioDao;
import br.com.fiap.entities.Acessorio;
import br.com.fiap.entities.InventarioUsuario;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;

import java.sql.SQLException;
import java.util.List;

public class InventarioUsuarioBo {

    private static final int ORA_CHAVE_DUPLICADA = 1;

    public List<InventarioUsuario> listarPorUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
        try (InventarioUsuarioDao dao = new InventarioUsuarioDao(); UsuarioDao usuarioDao = new UsuarioDao()) {
            if (usuarioDao.buscarPorId(idUsuario) == null) {
                throw new RecursoNaoEncontradoException("Usuario " + idUsuario + " nao encontrado");
            }
            return dao.listarPorUsuario(idUsuario);
        }
    }

    public InventarioUsuario buscar(int idUsuario, int idAcessorio) throws SQLException, ClassNotFoundException {
        try (InventarioUsuarioDao dao = new InventarioUsuarioDao()) {
            return buscarOuFalhar(dao, idUsuario, idAcessorio);
        }
    }

    public InventarioUsuario comprar(int idUsuario, int idAcessorio) throws SQLException, ClassNotFoundException {
        try (InventarioUsuarioDao dao = new InventarioUsuarioDao();
             UsuarioDao usuarioDao = new UsuarioDao();
             AcessorioDao acessorioDao = new AcessorioDao()) {
            if (usuarioDao.buscarPorId(idUsuario) == null) {
                throw new RecursoNaoEncontradoException("Usuario " + idUsuario + " nao encontrado");
            }
            Acessorio acessorio = acessorioDao.buscarPorId(idAcessorio);
            if (acessorio == null) {
                throw new RecursoNaoEncontradoException("Acessorio " + idAcessorio + " nao encontrado");
            }
            if (dao.buscar(idAcessorio, idUsuario) != null) {
                throw new ConflitoException("Usuario ja possui este acessorio");
            }
            int linhas;
            try {
                linhas = dao.comprar(idUsuario, idAcessorio, acessorio.getPreco());
            } catch (SQLException e) {
                if (e.getErrorCode() == ORA_CHAVE_DUPLICADA) {
                    throw new ConflitoException("Usuario ja possui este acessorio");
                }
                throw e;
            }
            if (linhas == 0) {
                throw new ValidacaoException("Saldo de moedas insuficiente");
            }
            return dao.buscar(idAcessorio, idUsuario);
        }
    }

    public InventarioUsuario equipar(int idUsuario, int idAcessorio) throws SQLException, ClassNotFoundException {
        try (InventarioUsuarioDao dao = new InventarioUsuarioDao()) {
            buscarOuFalhar(dao, idUsuario, idAcessorio);
            if (dao.equipar(idUsuario, idAcessorio) == 0) {
                throw new RecursoNaoEncontradoException(
                        "Usuario " + idUsuario + " nao possui o acessorio " + idAcessorio);
            }
            return dao.buscar(idAcessorio, idUsuario);
        }
    }

    public InventarioUsuario desequipar(int idUsuario, int idAcessorio) throws SQLException, ClassNotFoundException {
        try (InventarioUsuarioDao dao = new InventarioUsuarioDao()) {
            InventarioUsuario item = buscarOuFalhar(dao, idUsuario, idAcessorio);
            item.setEquipado("N");
            dao.atualizar(item);
            return item;
        }
    }

    public void deletar(int idUsuario, int idAcessorio) throws SQLException, ClassNotFoundException {
        try (InventarioUsuarioDao dao = new InventarioUsuarioDao()) {
            buscarOuFalhar(dao, idUsuario, idAcessorio);
            dao.deletar(idAcessorio, idUsuario);
        }
    }

    private InventarioUsuario buscarOuFalhar(InventarioUsuarioDao dao, int idUsuario, int idAcessorio)
            throws SQLException {
        InventarioUsuario item = dao.buscar(idAcessorio, idUsuario);
        if (item == null) {
            throw new RecursoNaoEncontradoException(
                    "Usuario " + idUsuario + " nao possui o acessorio " + idAcessorio);
        }
        return item;
    }
}