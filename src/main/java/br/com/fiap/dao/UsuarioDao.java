package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements AutoCloseable {

    private Connection minhaConexao;

    public UsuarioDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOME, EMAIL, SENHA) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            return stmt.executeUpdate();
        }
    }

    public int atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET NOME = ?, EMAIL = ?, SENHA = ? WHERE ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int atualizarSaldoEOfensiva(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET SALDO_MOEDAS = ?, NIVEL_OFENSIVA = ? WHERE ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getSaldoMoedas());
            stmt.setInt(2, usuario.getNivelOfensiva());
            stmt.setInt(3, usuario.getIdUsuario());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int id) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE ID_USUARIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE EMAIL = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT * FROM USUARIO ORDER BY ID_USUARIO";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("ID_USUARIO"),
                rs.getString("NOME"),
                rs.getString("EMAIL"),
                rs.getString("SENHA"),
                rs.getDate("USUARIO_CRIADO_EM").toLocalDate(),
                rs.getInt("SALDO_MOEDAS"),
                rs.getInt("NIVEL_OFENSIVA")
        );
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}