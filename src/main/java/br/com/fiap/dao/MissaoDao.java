package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Missao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MissaoDao implements AutoCloseable {

    private Connection minhaConexao;

    public MissaoDao() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserir(Missao missao) throws SQLException {
        String sql = "INSERT INTO MISSAO (TITULO_MISSAO, DESCRICAO_MISSAO, ATIVO, VALOR_MOEDAS_MISSAO, ADMINISTRADOR_ID_ADMINISTRADOR) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[]{"ID_MISSAO"})) {
            stmt.setString(1, missao.getTituloMissao());
            stmt.setString(2, missao.getDescricaoMissao());
            stmt.setString(3, missao.getAtivo());
            stmt.setInt(4, missao.getValorMoedasMissao());
            stmt.setInt(5, missao.getIdAdministrador());
            int linhas = stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    missao.setIdMissao(rs.getInt(1));
                }
            }
            return linhas;
        }
    }

    public int atualizar(Missao missao) throws SQLException {
        String sql = "UPDATE MISSAO SET TITULO_MISSAO = ?, DESCRICAO_MISSAO = ?, ATIVO = ?, VALOR_MOEDAS_MISSAO = ? WHERE ID_MISSAO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setString(1, missao.getTituloMissao());
            stmt.setString(2, missao.getDescricaoMissao());
            stmt.setString(3, missao.getAtivo());
            stmt.setInt(4, missao.getValorMoedasMissao());
            stmt.setInt(5, missao.getIdMissao());
            return stmt.executeUpdate();
        }
    }

    public int deletar(int idMissao) throws SQLException {
        String sql = "DELETE FROM MISSAO WHERE ID_MISSAO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            return stmt.executeUpdate();
        }
    }

    public Missao buscarPorId(int idMissao) throws SQLException {
        String sql = "SELECT * FROM MISSAO WHERE ID_MISSAO = ?";
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Missao> listar() throws SQLException {
        return listarComFiltro("SELECT * FROM MISSAO ORDER BY ID_MISSAO");
    }

    public List<Missao> listarAtivas() throws SQLException {
        return listarComFiltro("SELECT * FROM MISSAO WHERE ATIVO = 'S' ORDER BY ID_MISSAO");
    }

    private List<Missao> listarComFiltro(String sql) throws SQLException {
        List<Missao> lista = new ArrayList<>();
        try (PreparedStatement stmt = minhaConexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Missao mapear(ResultSet rs) throws SQLException {
        return new Missao(
                rs.getInt("ID_MISSAO"),
                rs.getString("TITULO_MISSAO"),
                rs.getString("DESCRICAO_MISSAO"),
                rs.getString("ATIVO"),
                rs.getInt("VALOR_MOEDAS_MISSAO"),
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