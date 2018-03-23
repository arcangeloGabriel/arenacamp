package com.arena.biel.arenacamp.modelo.classe;

import java.io.Serializable;

/**
 * Created by biel on 22/03/2018.
 */

public class Contato implements Serializable{
    private String identificadorUsuario;
    private String uidContato;
    private String nomeContato;
    private String emailContato;
    private String telefoneContato;
    private String msgContato;

    public Contato() {}

    public String getIdentificadorUsuario() {return identificadorUsuario;}

    public void setIdentificadorUsuario(String identificadorUsuario) {this.identificadorUsuario = identificadorUsuario;}

    public String getUidContato() {
        return uidContato;
    }

    public void setUidContato(String uidContato) {
        this.uidContato = uidContato;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public String getMsgContato() {
        return msgContato;
    }

    public void setMsgContato(String msgContato) {
        this.msgContato = msgContato;
    }

    @Override
    public String toString() {
        return nomeContato;
    }
}
