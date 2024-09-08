package br.ufc.sistemapatrimonio.entities;

import br.ufc.sistemapatrimonio.enums.TipoUsuario;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private static int id = 0;
    private final int idUser;
    private final List<Patrimonio> meusPatrimonios = new ArrayList<>();
    private final List<Bem> meusBens = new ArrayList<>();
    private final List<RequisicaoDeManutencao> minhasManutencoes = new ArrayList<>();
    private final List<RequisicaoDeReserva> minhasRequisicaoDeReservas = new ArrayList<>();
    private String username;
    private String password;
    private TipoUsuario tipoUsuario;

    public Usuario(String username, String password, TipoUsuario tipoUsuario) {
        this.username = username;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.idUser = id;
        id++;
    }

    // Getter para username
    public String getUsername() {
        return username;
    }

    // Setter para username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter para password
    public String getPassword() {
        return password;
    }

    // Setter para password
    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return idUser;
    }

    public List<Patrimonio> getMeusPatrimonios() {
        return meusPatrimonios;
    }

    public List<Bem> getMeusBens() {
        return meusBens;
    }

    public List<RequisicaoDeManutencao> getMinhasManutencoes() {
        return minhasManutencoes;
    }

    public List<RequisicaoDeReserva> getMinhasRequisicaoDeReservas() {
        return minhasRequisicaoDeReservas;
    }
}
