package br.com.fiap.entities;

public class Acessorio {

    //atributos
    private int idAcessorio;
    private String nome;
    private String tipo;
    private String raridade;
    private int preco;
    private int idAdministrador;

    //construtores
    public Acessorio() {
    }
    public Acessorio(int idAcessorio, String nome, String tipo, String raridade, int preco, int idAdministrador) {
        this.idAcessorio = idAcessorio;
        this.nome = nome;
        this.tipo = tipo;
        this.raridade = raridade;
        this.preco = preco;
        this.idAdministrador = idAdministrador;
    }

    //getters e setters
    public int getIdAcessorio() {
        return idAcessorio;
    }
    public void setIdAcessorio(int idAcessorio) {
        this.idAcessorio = idAcessorio;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getRaridade() {
        return raridade;
    }
    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }
    public int getPreco() {
        return preco;
    }
    public void setPreco(int preco) {
        this.preco = preco;
    }
    public int getIdAdministrador() {
        return idAdministrador;
    }
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    //toString
    @Override
    public String toString() {
        return "Acessorio: " +
                "\nidAcessorio: " + idAcessorio +
                "\nNome: " + nome +
                "\nTipo: " + tipo +
                "\nRaridade: " + raridade +
                "\nPreco: " + preco;
    }
}
