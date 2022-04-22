package pt.isec.pa.apoio_poe.model.data.propostas;

import pt.isec.pa.apoio_poe.model.data.Proposta;

public class Projeto_Estagio extends Proposta {
    public Projeto_Estagio(String id, String tipo, String titulo, Long numAluno) {
        super(id, tipo, titulo, numAluno);
    }

    @Override
    public String toString() {
        return "Projeto_Estagio{ " +
                super.toString() +
                " }\n";
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
        return new Object[]{getTipo(),getId(),getTitulo(),getNumAluno()};
    }
}
