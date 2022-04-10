package pt.isec.pa.apoio_poe.model.data.propostas;

import pt.isec.pa.apoio_poe.model.data.Proposta;

public class Projeto_Estagio extends Proposta {
    public Projeto_Estagio(String id, String tipo, String titulo, long numAluno) {
        super(id, tipo, titulo, numAluno);
    }

    @Override
    public String toString() {
        return "Projeto_Estagio{ " +
                super.toString() +
                " }\n";
    }
}
