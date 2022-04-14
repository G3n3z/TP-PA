package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.ArrayList;
import java.util.Map;

public class ConflitoAtribuicaoCandidatura extends StateAdapter{

    public ConflitoAtribuicaoCandidatura(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }



    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public boolean existConflict() {
        return data.existConflit();
    }

    @Override
    public boolean resolveConflito(long numAluno) {
        boolean exists = false;
        Map<Proposta, ArrayList<Aluno>> proposta_aluno = data.getProposta_aluno();
        if(proposta_aluno.isEmpty())
            return false;
        int index;
        for(Map.Entry<Proposta, ArrayList<Aluno>> set : proposta_aluno.entrySet()){
            if(set.getValue().contains(Aluno.getDummyAluno(numAluno))){
                exists = true;
                index = set.getValue().indexOf(Aluno.getDummyAluno(numAluno));
                set.getValue().get(index).setProposta(set.getKey());
                proposta_aluno.remove(set.getKey());
            }
        }
        if(!exists){
            Log.getInstance().putMessage("Não existe o numero de aluno indicado");
            return false;
        }
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA;
    }
}
