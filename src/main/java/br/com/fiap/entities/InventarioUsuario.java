package br.com.fiap.entities;

import java.time.LocalDate;

public class InventarioUsuario {

    //atributos
    private String equipado;
    private LocalDate adquiridoEm;
    private int idAcessorio;
    private int idUsuario;

    //construtor
    public InventarioUsuario() {
    }
    public InventarioUsuario(String equipado, LocalDate adquiridoEm, int idAcessorio, int idUsuario) {
        this.equipado = equipado;
        this.adquiridoEm = adquiridoEm;
        this.idAcessorio = idAcessorio;
        this.idUsuario = idUsuario;
    }

    //getters e setters
    public String getEquipado() {
        return equipado;
    }
    public void setEquipado(String equipado) {
        this.equipado = equipado;
    }
    public LocalDate getAdquiridoEm() {
        return adquiridoEm;
    }
    public void setAdquiridoEm(LocalDate adquiridoEm) {
        this.adquiridoEm = adquiridoEm;
    }
    public int getIdAcessorio() {
        return idAcessorio;
    }
    public void setIdAcessorio(int idAcessorio) {
        this.idAcessorio = idAcessorio;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
