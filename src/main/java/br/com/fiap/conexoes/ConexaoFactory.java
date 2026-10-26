package br.com.fiap.conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {

    private  final static String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
    private  final static String USUARIO = "rm572102";
    private  final static String SENHA = "180408";

    // metodo de conexão com o banco de dados
    public Connection conexao() throws ClassNotFoundException, SQLException {

        // Driver oracle
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Retornar conexão
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
