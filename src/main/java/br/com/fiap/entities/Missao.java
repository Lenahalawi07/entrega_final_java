package br.com.fiap.entities;

public class Missao {
    //atributos
    private int idMissao;
    private String tituloMissao;
    private String descricaoMissao;
    private String ativo;
    private int valorMoedasMissao;
    private int idAdministrador;

    //construtor
    public Missao() {
    }
    public Missao(int idMissao, String tituloMissao, String descricaoMissao, String ativo, int valorMoedasMissao, int idAdministrador) {
        this.idMissao = idMissao;
        this.tituloMissao = tituloMissao;
        this.descricaoMissao = descricaoMissao;
        this.ativo = ativo;
        this.valorMoedasMissao = valorMoedasMissao;
        this.idAdministrador = idAdministrador;
    }

    //getters e setters
    public int getIdMissao() {
        return idMissao;
    }
    public void setIdMissao(int idMissao) {
        this.idMissao = idMissao;
    }
    public String getTituloMissao() {
        return tituloMissao;
    }
    public void setTituloMissao(String tituloMissao) {
        this.tituloMissao = tituloMissao;
    }
    public String getDescricaoMissao() {
        return descricaoMissao;
    }
    public void setDescricaoMissao(String descricaoMissao) {
        this.descricaoMissao = descricaoMissao;
    }
    public String getAtivo() {
        return ativo;
    }
    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
    public int getValorMoedasMissao() {
        return valorMoedasMissao;
    }
    public void setValorMoedasMissao(int valorMoedasMissao) {
        this.valorMoedasMissao = valorMoedasMissao;
    }
    public int getIdAdministrador() {
        return idAdministrador;
    }
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    //metodo
    public boolean estaDisponivel() {
        return "S".equals(this.ativo);
    }
}
