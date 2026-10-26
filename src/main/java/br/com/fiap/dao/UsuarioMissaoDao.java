package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.UsuarioMissao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioMissaoDao implements AutoCloseable {

    private Connection minhaConexao;

    public UsuarioMissaoDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(UsuarioMissao um) throws SQLException {
        String sql = "INSERT INTO USUARIO_MISSAO (MISSAO_ID_MISSAO, USUARIO_ID_USUARIO) VALUES (?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, um.getIdMissao());
            stmt.setInt(2, um.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int atualizar(UsuarioMissao um) throws SQLException {
        String sql = "UPDATE USUARIO_MISSAO SET PROGRESSO_MISSAO = ?, MISSAO_CONCLUIDA = ?, "
                + "MISSAO_CONCLUIDA_EM = ?, MOEDAS_GANHAS_MISSAO = ? "
                + "WHERE MISSAO_ID_MISSAO = ? AND USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, um.getProgressoMissao());
            stmt.setString(2, um.getMissaoConcluida());
            if (um.getMissaoConcluidaEm() != null) {
                stmt.setDate(3, Date.valueOf(um.getMissaoConcluidaEm()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setInt(4, um.getMoedasGanhasMissao());
            stmt.setInt(5, um.getIdMissao());
            stmt.setInt(6, um.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int idMissao, int idUsuario) throws SQLException {
        String sql = "DELETE FROM USUARIO_MISSAO WHERE MISSAO_ID_MISSAO = ? AND USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate();
        }
    }

    public UsuarioMissao buscar(int idMissao, int idUsuario) throws SQLException {
        String sql = "SELECT * FROM USUARIO_MISSAO WHERE MISSAO_ID_MISSAO = ? AND USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            stmt.setInt(2, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<UsuarioMissao> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM USUARIO_MISSAO WHERE USUARIO_ID_USUARIO = ? ORDER BY MISSAO_ID_MISSAO";
        List<UsuarioMissao> lista = new ArrayList<>();
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

    public List<UsuarioMissao> listar() throws SQLException {
        String sql = "SELECT * FROM USUARIO_MISSAO ORDER BY USUARIO_ID_USUARIO, MISSAO_ID_MISSAO";
        List<UsuarioMissao> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Conclui a missao e credita as moedas na mesma transacao.
    // Retorna 1 se concluiu, 0 se a missao nao existe para o usuario ou ja estava concluida.
    public int concluirMissao(int idUsuario, int idMissao, int moedas) throws SQLException {
        String sqlMissao = "UPDATE USUARIO_MISSAO SET PROGRESSO_MISSAO = 100, MISSAO_CONCLUIDA = 'S', "
                + "MISSAO_CONCLUIDA_EM = SYSDATE, MOEDAS_GANHAS_MISSAO = ? "
                + "WHERE MISSAO_ID_MISSAO = ? AND USUARIO_ID_USUARIO = ? AND MISSAO_CONCLUIDA = 'N'";
        String sqlSaldo = "UPDATE USUARIO SET SALDO_MOEDAS = SALDO_MOEDAS + ? WHERE ID_USUARIO = ?";

        minhaConexao.setAutoCommit(false);
        try {
            int linhas;
            try (PreparedStatement stmt = minhaConexao.prepareStatement(sqlMissao)) {
                stmt.setInt(1, moedas);
                stmt.setInt(2, idMissao);
                stmt.setInt(3, idUsuario);
                linhas = stmt.executeUpdate();
            }
            if (linhas == 0) {
                minhaConexao.rollback();
                return 0;
            }
            try (PreparedStatement stmt = minhaConexao.prepareStatement(sqlSaldo)) {
                stmt.setInt(1, moedas);
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

    private UsuarioMissao mapear(ResultSet rs) throws SQLException {
        Date concluidaEm = rs.getDate("MISSAO_CONCLUIDA_EM");
        LocalDate dataConclusao = concluidaEm != null ? concluidaEm.toLocalDate() : null;
        return new UsuarioMissao(
                rs.getInt("PROGRESSO_MISSAO"),
                rs.getString("MISSAO_CONCLUIDA"),
                dataConclusao,
                rs.getInt("MOEDAS_GANHAS_MISSAO"),
                rs.getInt("MISSAO_ID_MISSAO"),
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