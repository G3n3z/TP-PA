package pt.isec.pa.apoio_poe.model.data;

import java.util.ArrayList;

public class Docente extends Pessoa{

    ArrayList<Proposta> propostas;

    public Docente(String email, String nome) {
        super(email, nome);
    }

}
