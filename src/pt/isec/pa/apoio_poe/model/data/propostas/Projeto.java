package pt.isec.pa.apoio_poe.model.data.propostas;

import java.util.List;

import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class Projeto extends Proposta {
    String docente;
    Docente proponente;
    Docente orientador;

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

    public String getEmailDocente() {
        return docente;
    }

    public void setDocenteOrientador(Docente d) {
        orientador = d;
    }
    public void setDocenteProponente(Docente d) {
        proponente = d;
    }
    public boolean temDocenteOrientador(){
        return orientador != null;
    }
    public boolean temDocenteProponente(){
        return proponente != null;
    }

    public void setDocenteOrientadorDocenteProponente() {
        orientador = proponente;
    }
}
