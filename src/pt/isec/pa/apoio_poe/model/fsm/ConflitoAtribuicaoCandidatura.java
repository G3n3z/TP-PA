package pt.isec.pa.apoio_poe.model.fsm;

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
        changeState(EnumState.ATRIBUICAOPROPOSTAS);
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

        for(Map.Entry<Proposta, ArrayList<Aluno>> set : proposta_aluno.entrySet()){
            if(set.getValue().contains(numAluno)){
                exists = true;
                //set.getValue().get()
            }
        }
        return true;
    }
}
