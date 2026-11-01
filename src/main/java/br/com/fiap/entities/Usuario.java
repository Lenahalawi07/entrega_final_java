package br.com.fiap.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Usuario {

    // atributos
    private int idUsuario;
    private String nome;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
    private LocalDate usuarioCriadoEm;
    private int saldoMoedas;
    private int nivelOfensiva;
    private String fotoUrl;


    //construtores
    public Usuario() {
    }
    public Usuario(int idUsuario, String nome, String email, String senha,
                 LocalDate usuarioCriadoEm, int saldoMoedas, int nivelOfensiva, String fotoUrl) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.usuarioCriadoEm = usuarioCriadoEm;
        this.saldoMoedas = saldoMoedas;
        this.nivelOfensiva = nivelOfensiva;
        this.fotoUrl = fotoUrl;
    }

    //getters e setters
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
    public LocalDate getUsuarioCriadoEm() {
        return usuarioCriadoEm;
    }
    public void setUsuarioCriadoEm(LocalDate usuarioCriadoEm) {
        this.usuarioCriadoEm = usuarioCriadoEm;
    }

    public int getSaldoMoedas() {
        return saldoMoedas;
    }
    public void setSaldoMoedas(int saldoMoedas) {
        this.saldoMoedas = saldoMoedas;
    }
    public int getNivelOfensiva() {
        return nivelOfensiva;
    }
    public void setNivelOfensiva(int nivelOfensiva) {
        this.nivelOfensiva = nivelOfensiva;
    }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }


    //toString

    @Override
    public String toString() {
        return "Usuario " +
                "\nidUsuario: " + idUsuario +
                "\nNome: " + nome +
                "\nEmail: " + email +
                "\nSaldo moedas: " + saldoMoedas +
                "\nNivel ofensiva: " + nivelOfensiva +
                "\nUsuario criado em: " + usuarioCriadoEm;
    }

    //metodos
    public void creditarMoedas(int valor) {
        if (valor > 0) {
            this.saldoMoedas += valor;
        }
    }

    public boolean resgatarRecompensa(Acessorio acessorio) {
        if (this.saldoMoedas >= acessorio.getPreco()) {
            this.saldoMoedas -= acessorio.getPreco();
            return true;
        }
        return false;
    }

    public void atualizarOfensiva() {
        this.nivelOfensiva++;
    }

    public static boolean emailValido(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}
