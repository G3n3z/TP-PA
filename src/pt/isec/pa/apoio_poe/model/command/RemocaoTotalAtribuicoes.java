package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class RemocaoTotalAtribuicoes extends CommandAdapter {

    Map<Long, String> aluno_proposta;

    public RemocaoTotalAtribuicoes(Data data) {
        super(data);
        aluno_proposta = new HashMap<>();
    }

    @Override
    public ErrorCode execute(){
        for(Aluno a : data.getAlunos()){
            if(a.getProposta() != null && !(a.getProposta() instanceof Projeto_Estagio || a.getProposta().getNumAluno() != null)) {
                aluno_proposta.put(a.getNumeroAluno(), a.getProposta().getId());
                a.removeProposta();
            }
        }
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode undo(){
        aluno_proposta.forEach((nAluno, idProposta) -> new AtribuicaoManualProposta(data, nAluno, idProposta).execute());
        aluno_proposta.clear();
        return ErrorCode.E0;
    }

}
