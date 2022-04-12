package pt.isec.pa.apoio_poe.model.data.propostas;

import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.List;

public class Estagio extends Proposta {
    private String entidade;

    public Estagio(String id, String tipo, List<String> ramos, String titulo, String entidade) {
        super(id, tipo, ramos, titulo);
        this.entidade = entidade;
    }

    public Estagio(String id, String tipo, List<String> ramos, String titulo, String entidade, long numAluno) {
        super(id, tipo, ramos, titulo, numAluno);
        this.entidade = entidade;
    }

    @Override
    public String toString() {
        return "Estagio{ " +
                super.toString() +
                ", entidade='" + entidade + '\'' +
                " }\n";
    }
}
