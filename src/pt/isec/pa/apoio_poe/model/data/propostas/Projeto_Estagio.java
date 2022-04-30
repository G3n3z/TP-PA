package pt.isec.pa.apoio_poe.model.data.propostas;

import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.ArrayList;
import java.util.List;

public class Projeto_Estagio extends Proposta {
    public Projeto_Estagio(String id, String tipo, String titulo, Long numAluno) {
        super(id, tipo, titulo, numAluno);
        setAtribuida(true);
    }

    @Override
    public String toString() {
        return "Projeto_Estagio-> " +
                super.toString() +
                ".\n";
    }

    @Override
    public Proposta getClone() {
        Proposta clone =  new Projeto_Estagio(getId(),getTipo(), getTitulo(), getNumAluno());
        clone.setAtribuida(isAtribuida());
        clone.setDocenteProponente(getProponente());
        clone.setDocenteOrientador(getOrientador());
        return clone;
    }

    @Override
    public Object[] exportProposta() {
        List<Object> objetos = new ArrayList<>(List.of(getTipo(),getId(),  getTitulo(), getNumAluno()));
        return objetos.toArray();
    }
}
