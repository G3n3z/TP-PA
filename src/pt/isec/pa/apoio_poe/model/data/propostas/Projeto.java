package pt.isec.pa.apoio_poe.model.data.propostas;

import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.List;

public class Projeto extends Proposta {
    String docente;

    public Projeto(String id, String tipo, List<String> ramos, String titulo, String docente) {
        super(id, tipo, ramos, titulo);
        this.docente = docente;
    }

    public Projeto(String id, String tipo, List<String> ramos, String titulo, String docente, long numAluno) {
        super(id, tipo, ramos, titulo, numAluno);
        this.docente = docente;
    }

    @Override
    public String toString() {
        return "Projeto{ " +
                super.toString() +
                " docente='" + docente + '\'' +
                " }\n";
    }
}
