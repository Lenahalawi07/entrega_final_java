package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDao implements AutoCloseable {

    private Connection minhaConexao;

    public AdministradorDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(Administrador administrador) throws SQLException {
        String sql = "INSERT INTO ADMINISTRADOR (NOME, EMAIL, SENHA) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, administrador.getNome());
            stmt.setString(2, administrador.getEmail());
            stmt.setString(3, administrador.getSenha());
            return stmt.executeUpdate();
        }
    }

    public int atualizar(Administrador administrador) throws SQLException {
        String sql = "UPDATE ADMINISTRADOR SET NOME = ?, EMAIL = ?, SENHA = ? WHERE ID_ADMINISTRADOR = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, administrador.getNome());
            stmt.setString(2, administrador.getEmail());
            stmt.setString(3, administrador.getSenha());
            stmt.setInt(4, administrador.getIdAdministrador());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int idAdministrador) throws SQLException {
        String sql = "DELETE FROM ADMINISTRADOR WHERE ID_ADMINISTRADOR = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAdministrador);
            return stmt.executeUpdate();
        }
    }

    public Administrador buscarPorId(int idAdministrador) throws SQLException {
        String sql = "SELECT * FROM ADMINISTRADOR WHERE ID_ADMINISTRADOR = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAdministrador);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public Administrador buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM ADMINISTRADOR WHERE EMAIL = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Administrador> listar() throws SQLException {
        String sql = "SELECT * FROM ADMINISTRADOR ORDER BY ID_ADMINISTRADOR";
        List<Administrador> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Administrador mapear(ResultSet rs) throws SQLException {
        return new Administrador(
                rs.getInt("ID_ADMINISTRADOR"),
                rs.getString("NOME"),
                rs.getString("EMAIL"),
                rs.getString("SENHA"),
                rs.getDate("ADMIN_CRIADO_EM").toLocalDate()
        );
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}