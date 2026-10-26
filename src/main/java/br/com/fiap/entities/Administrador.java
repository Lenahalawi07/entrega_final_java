package br.com.fiap.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Administrador {
    //atributos
    private int idAdministrador;
    private String nome;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
    private LocalDate adminCriadoEm;

    //construtor
    public Administrador() {
    }
    public Administrador(int idAdministrador, String nome, String email, String senha, LocalDate adminCriadoEm) {
        this.idAdministrador = idAdministrador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.adminCriadoEm = adminCriadoEm;
    }

    //getters e setters
    public int getIdAdministrador() {
        return idAdministrador;
    }
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public LocalDate getAdminCriadoEm() {
        return adminCriadoEm;
    }
    public void setAdminCriadoEm(LocalDate adminCriadoEm) {
        this.adminCriadoEm = adminCriadoEm;
    }
}
