package pt.isec.pa.apoio_poe.model.data.propostas;

import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.List;

public class Estagio extends Proposta {
    private String entidade;

    public Estagio(String id, String tipo, List<String> ramos, String titulo, String entidade) {
        super(id, tipo, ramos, titulo);
        this.entidade = entidade;
    }

    public Estagio(String id, String tipo, List<String> ramos, String titulo, String entidade, Long numAluno) {
        super(id, tipo, ramos, titulo, numAluno);
        this.entidade = entidade;
    }

    @Override
    public String toString() {
        return "Estagio-> " +
                super.toString() +
                "; Entidade: " + entidade +
                ".\n";
    }

    @Override
    public Proposta getClone() {

        Proposta clone =  new Estagio(getId(),getTipo(), getRamos(), getTitulo(), entidade, getNumAluno());
        clone.setAtribuida(isAtribuida());
        clone.setDocenteProponente(getProponente());
        clone.setDocenteOrientador(getOrientador());
        return clone;
    }

    @Override
    public Object[] exportProposta() {
        return new Object[]{getTipo(),getId(),getRamosToExport(),getTitulo(),entidade,getNumAluno()};
    }

    public void changeEntidade(String nova_entidade) {
        entidade = nova_entidade;
    }
}
