package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Avatar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvatarDao implements AutoCloseable {

    private Connection minhaConexao;

    public AvatarDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(Avatar avatar) throws SQLException {
        String sql = "INSERT INTO AVATAR (NOME, USUARIO_ID_USUARIO) VALUES (?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, avatar.getNome());
            stmt.setInt(2, avatar.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int atualizar(Avatar avatar) throws SQLException {
        String sql = "UPDATE AVATAR SET NOME = ? WHERE ID_AVATAR = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, avatar.getNome());
            stmt.setInt(2, avatar.getIdAvatar());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int idAvatar) throws SQLException {
        String sql = "DELETE FROM AVATAR WHERE ID_AVATAR = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAvatar);
            return stmt.executeUpdate();
        }
    }

    public Avatar buscarPorId(int idAvatar) throws SQLException {
        String sql = "SELECT * FROM AVATAR WHERE ID_AVATAR = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAvatar);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public Avatar buscarPorUsuarioId(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM AVATAR WHERE USUARIO_ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Avatar> listar() throws SQLException {
        String sql = "SELECT * FROM AVATAR ORDER BY ID_AVATAR";
        List<Avatar> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Avatar mapear(ResultSet rs) throws SQLException {
        return new Avatar(
                rs.getInt("ID_AVATAR"),
                rs.getString("NOME"),
                rs.getDate("AVATAR_CRIADO_EM").toLocalDate(),
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