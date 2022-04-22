package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class Pessoa implements Serializable {
    private String email;
    private String nome;

    public Pessoa(String email, String nome) {
        this.email = email;
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
