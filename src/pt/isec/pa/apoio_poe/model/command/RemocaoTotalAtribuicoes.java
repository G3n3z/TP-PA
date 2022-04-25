package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;

import java.util.HashMap;
import java.util.Map;

public class RemocaoTotalAtribuicoes extends CommandAdapter {

    Map<Long, String> aluno_proposta;

    public RemocaoTotalAtribuicoes(Data data) {
        super(data);
        aluno_proposta = new HashMap<>();
        for (Aluno a : data.getAlunos()){
            if(a.getProposta() != null) {
                aluno_proposta.put(a.getNumeroAluno(), a.getProposta().getId());
            }
        }
    }

    @Override
    public boolean execute(){
        for(Aluno a : data.getAlunos()){
            a.removeProposta();
        }
        return true;
    }

    @Override
    public boolean undo(){
        aluno_proposta.forEach((nAluno, idProposta) -> new AtribuicaoManualProposta(data, nAluno, idProposta));
        return true;
    }

}
