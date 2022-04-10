package pt.isec.pa.apoio_poe.model.data;

import java.util.ArrayList;
import java.util.Objects;

public class Docente extends Pessoa{

    ArrayList<Proposta> propostas;

    private Docente(String email){
        super(email,null);
    }

    public static Docente getDummyDocente(String email){
        return new Docente(email);
    }

    public Docente(String email, String nome) {
        super(email, nome);
    }

    @Override
    public String toString() {
        return "Docente{" +
                " nome: " + getNome() +
                " email: " + getEmail() +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Docente docente = (Docente) o;

        return getEmail().equals(docente.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
