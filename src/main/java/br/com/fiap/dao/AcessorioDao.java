package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Acessorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcessorioDao implements AutoCloseable {

    private Connection minhaConexao;

    public AcessorioDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(Acessorio acessorio) throws SQLException {
        String sql = "INSERT INTO ACESSORIO (NOME, TIPO, RARIDADE, PRECO, ADMINISTRADOR_ID_ADMINISTRADOR) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[]{"ID_ACESSORIO"})) {
            stmt.setString(1, acessorio.getNome());
            stmt.setString(2, acessorio.getTipo());
            stmt.setString(3, acessorio.getRaridade());
            stmt.setInt(4, acessorio.getPreco());
            stmt.setInt(5, acessorio.getIdAdministrador());
            int linhas = stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    acessorio.setIdAcessorio(rs.getInt(1));
                }
            }
            return linhas;
        }
    }

    public int atualizar(Acessorio acessorio) throws SQLException {
        String sql = "UPDATE ACESSORIO SET NOME = ?, TIPO = ?, RARIDADE = ?, PRECO = ? WHERE ID_ACESSORIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, acessorio.getNome());
            stmt.setString(2, acessorio.getTipo());
            stmt.setString(3, acessorio.getRaridade());
            stmt.setInt(4, acessorio.getPreco());
            stmt.setInt(5, acessorio.getIdAcessorio());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int idAcessorio) throws SQLException {
        String sql = "DELETE FROM ACESSORIO WHERE ID_ACESSORIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAcessorio);
            return stmt.executeUpdate();
        }
    }

    public Acessorio buscarPorId(int idAcessorio) throws SQLException {
        String sql = "SELECT * FROM ACESSORIO WHERE ID_ACESSORIO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idAcessorio);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Acessorio> listar() throws SQLException {
        String sql = "SELECT * FROM ACESSORIO ORDER BY ID_ACESSORIO";
        List<Acessorio> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Acessorio mapear(ResultSet rs) throws SQLException {
        return new Acessorio(
                rs.getInt("ID_ACESSORIO"),
                rs.getString("NOME"),
                rs.getString("TIPO"),
                rs.getString("RARIDADE"),
                rs.getInt("PRECO"),
                rs.getInt("ADMINISTRADOR_ID_ADMINISTRADOR")
        );
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}