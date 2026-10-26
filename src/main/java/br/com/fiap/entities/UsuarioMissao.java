package br.com.fiap.entities;

import java.time.LocalDate;

public class UsuarioMissao {

    //atributos
    private int progressoMissao;
    private String missaoConcluida;
    private LocalDate missaoConcluidaEm;
    private int moedasGanhasMissao;
    private int idMissao;
    private int idUsuario;

    //construtor
    public UsuarioMissao() {
    }
    public UsuarioMissao(int progressoMissao, String missaoConcluida, LocalDate missaoConcluidaEm, int moedasGanhasMissao, int idMissao, int idUsuario) {
        this.progressoMissao = progressoMissao;
        this.missaoConcluida = missaoConcluida;
        this.missaoConcluidaEm = missaoConcluidaEm;
        this.moedasGanhasMissao = moedasGanhasMissao;
        this.idMissao = idMissao;
        this.idUsuario = idUsuario;
    }

    //getters e setters
    public int getProgressoMissao() {
        return progressoMissao;
    }
    public void setProgressoMissao(int progressoMissao) {
        this.progressoMissao = progressoMissao;
    }
    public String getMissaoConcluida() {
        return missaoConcluida;
    }
    public void setMissaoConcluida(String missaoConcluida) {
        this.missaoConcluida = missaoConcluida;
    }
    public LocalDate getMissaoConcluidaEm() {
        return missaoConcluidaEm;
    }
    public void setMissaoConcluidaEm(LocalDate missaoConcluidaEm) {
        this.missaoConcluidaEm = missaoConcluidaEm;
    }
    public int getMoedasGanhasMissao() {
        return moedasGanhasMissao;
    }
    public void setMoedasGanhasMissao(int moedasGanhasMissao) {
        this.moedasGanhasMissao = moedasGanhasMissao;
    }
    public int getIdMissao() {
        return idMissao;
    }
    public void setIdMissao(int idMissao) {
        this.idMissao = idMissao;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    //metodo
    public void concluirMissao(int moedasRecompensa) {
        if (!"S".equals(this.missaoConcluida)) {
            this.progressoMissao = 100;
            this.missaoConcluida = "S";
            this.missaoConcluidaEm = java.time.LocalDate.now();
            this.moedasGanhasMissao = moedasRecompensa;
        }
    }
}
