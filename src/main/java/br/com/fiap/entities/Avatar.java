package br.com.fiap.entities;

import java.time.LocalDate;

public class Avatar {
    //atributos
    private int idAvatar;
    private String nome;
    private LocalDate avatarCriadoEm;
    private int idUsuario;

    //construtor
    public Avatar() {
    }
    public Avatar(int idAvatar, String nome, LocalDate avatarCriadoEm, int idUsuario) {
        this.idAvatar = idAvatar;
        this.nome = nome;
        this.avatarCriadoEm = avatarCriadoEm;
        this.idUsuario = idUsuario;
    }

    //getters e setters
    public int getIdAvatar() {
        return idAvatar;
    }
    public void setIdAvatar(int idAvatar) {
        this.idAvatar = idAvatar;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public LocalDate getAvatarCriadoEm() {
        return avatarCriadoEm;
    }
    public void setAvatarCriadoEm(LocalDate avatarCriadoEm) {
        this.avatarCriadoEm = avatarCriadoEm;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Avatar: " +
                "\nidAvatar: " + idAvatar +
                "\nNome: " + nome +
                "\nUsuarioId: " + idUsuario;
    }
}
