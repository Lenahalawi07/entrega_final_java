package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.InventarioUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioUsuarioDao implements AutoCloseable {

    private Connection minhaConexao;

    public InventarioUsuarioDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(InventarioUsuario inv) throws SQLException {
        String sql = "INSERT INTO INVENTARIO_USUARIO (ACESSORIO_ID_ACESSORIO, USUARIO_ID_USUARIO) VALUES (?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, inv.getIdAcessorio());
            stmt.setInt(2, inv.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int atualizar(InventarioUsuario inv) throws SQLException {
        String sql = "UPDATE INVENTARIO_USUARIO SET EQUIPADO = ? WHERE ACESSORIO_ID_ACESSORIO = ? AND USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, inv.getEquipado());
            stmt.setInt(2, inv.getIdAcessorio());
            stmt.setInt(3, inv.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int idAcessorio, int idUsuario) throws SQLException {
        String sql = "DELETE FROM INVENTARIO_USUARIO WHERE ACESSORIO_ID_ACESSORIO = ? AND USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAcessorio);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate();
        }
    }

    public InventarioUsuario buscar(int idAcessorio, int idUsuario) throws SQLException {
        String sql = "SELECT * FROM INVENTARIO_USUARIO WHERE ACESSORIO_ID_ACESSORIO = ? AND USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAcessorio);
            stmt.setInt(2, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<InventarioUsuario> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM INVENTARIO_USUARIO WHERE USUARIO_ID_USUARIO = ? ORDER BY ACESSORIO_ID_ACESSORIO";
        List<InventarioUsuario> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<InventarioUsuario> listar() throws SQLException {
        String sql = "SELECT * FROM INVENTARIO_USUARIO ORDER BY USUARIO_ID_USUARIO, ACESSORIO_ID_ACESSORIO";
        List<InventarioUsuario> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Debita o preco e adiciona o acessorio ao inventario na mesma transacao.
    // Retorna 1 se comprou, 0 se o saldo for insuficiente.
    public int comprar(int idUsuario, int idAcessorio, int preco) throws SQLException {
        String sqlDebito = "UPDATE USUARIO SET SALDO_MOEDAS = SALDO_MOEDAS - ? "
                + "WHERE ID_USUARIO = ? AND SALDO_MOEDAS >= ?";
        String sqlInventario = "INSERT INTO INVENTARIO_USUARIO (ACESSORIO_ID_ACESSORIO, USUARIO_ID_USUARIO) VALUES (?, ?)";

        minhaConexao.setAutoCommit(false);
        try {
            int linhas;
            try (PreparedStatement stmt = minhaConexao.prepareStatement(sqlDebito)) {
                stmt.setInt(1, preco);
                stmt.setInt(2, idUsuario);
                stmt.setInt(3, preco);
                linhas = stmt.executeUpdate();
            }
            if (linhas == 0) {
                minhaConexao.rollback();
                return 0;
            }
            try (PreparedStatement stmt = minhaConexao.prepareStatement(sqlInventario)) {
                stmt.setInt(1, idAcessorio);
                stmt.setInt(2, idUsuario);
                stmt.executeUpdate();
            }
            minhaConexao.commit();
            return 1;
        } catch (SQLException e) {
            minhaConexao.rollback();
            throw e;
        } finally {
            minhaConexao.setAutoCommit(true);
        }
    }

    // Desequipa os acessorios do mesmo tipo e equipa o escolhido, na mesma transacao.
    // Retorna 1 se equipou, 0 se o usuario nao possui o acessorio.
    public int equipar(int idUsuario, int idAcessorio) throws SQLException {
        String sqlDesequipar = "UPDATE INVENTARIO_USUARIO SET EQUIPADO = 'N' "
                + "WHERE USUARIO_ID_USUARIO = ? AND ACESSORIO_ID_ACESSORIO IN ("
                + "SELECT ID_ACESSORIO FROM ACESSORIO WHERE TIPO = ("
                + "SELECT TIPO FROM ACESSORIO WHERE ID_ACESSORIO = ?))";
        String sqlEquipar = "UPDATE INVENTARIO_USUARIO SET EQUIPADO = 'S' "
                + "WHERE USUARIO_ID_USUARIO = ? AND ACESSORIO_ID_ACESSORIO = ?";

        minhaConexao.setAutoCommit(false);
        try {
            try (PreparedStatement stmt = minhaConexao.prepareStatement(sqlDesequipar)) {
                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idAcessorio);
                stmt.executeUpdate();
            }
            int linhas;
            try (PreparedStatement stmt = minhaConexao.prepareStatement(sqlEquipar)) {
                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idAcessorio);
                linhas = stmt.executeUpdate();
            }
            if (linhas == 0) {
                minhaConexao.rollback();
                return 0;
            }
            minhaConexao.commit();
            return 1;
        } catch (SQLException e) {
            minhaConexao.rollback();
            throw e;
        } finally {
            minhaConexao.setAutoCommit(true);
        }
    }

    private InventarioUsuario mapear(ResultSet rs) throws SQLException {
        return new InventarioUsuario(
                rs.getString("EQUIPADO"),
                rs.getDate("ADQUIRIDO_EM").toLocalDate(),
                rs.getInt("ACESSORIO_ID_ACESSORIO"),
                rs.getInt("USUARIO_ID_USUARIO")
        );
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}