package pt.isec.pa.apoio_poe.model.data.propostas;

import java.util.ArrayList;
import java.util.List;

import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class Projeto extends Proposta {
    String docente;


    public Projeto(String id, String tipo, List<String> ramos, String titulo, String docente) {
        super(id, tipo, ramos, titulo);
        this.docente = docente;
    }

    public Projeto(String id, String tipo, List<String> ramos, String titulo, String docente, Long numAluno) {
        super(id, tipo, ramos, titulo, numAluno);
        this.docente = docente;
    }

    @Override
    public String toString() {
        return "Projeto-> " +
                super.toString() +
                "; Docente: " + (docente == null ? "n/a" : docente) +
                ".\n";
    }

    public String getEmailDocente() {
        return docente;
    }


    public void setEmailDocente(String email) {
        docente = email;
    }

    @Override
    public Proposta getClone() {

        Proposta clone =  new Projeto(getId(),getTipo(), getRamos(), getTitulo(), docente, getNumAluno());
        clone.setAtribuida(isAtribuida());
        clone.setDocenteProponente(getProponente());
        clone.setDocenteOrientador(getOrientador());
        return clone;
    }

    @Override
    public Object[] exportProposta() {
        List<Object> objetos = new ArrayList<>(List.of(getTipo(),getId(), getRamosToExport(),getTitulo()));
        if(docente != null)
            objetos.add(docente);
        if (getNumAluno() != null){
            objetos.add(getNumAluno());
        }
        return objetos.toArray();
    }

}
