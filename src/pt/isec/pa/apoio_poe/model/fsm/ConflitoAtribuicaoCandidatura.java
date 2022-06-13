package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        return data.getProposta_aluno().size() > 0;
    }

    @Override
    public ErrorCode resolveConflito(long numAluno) {
        boolean exists = false;
        Map<Proposta, ArrayList<Aluno>> proposta_aluno = data.getProposta_aluno();
        if(proposta_aluno.isEmpty())
            return ErrorCode.E0; //Verificar TODO
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
            //MessageCenter.getInstance().putMessage("Não existe o numero de aluno indicado");
            return ErrorCode.E3;
        }
        
        return ErrorCode.E0;
    }

    @Override
    public EnumState getState() {
        return EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA;
    }

    @Override
    public String getConflitoToString() {
        StringBuilder sb = new StringBuilder();
        data.getProposta_aluno().forEach((k,v) -> {
            sb.append("Proposta com id: ").append(k.getId()).append(" com conflito\n");
            sb.append("Lista de alunos com sobreposição\n");
            v.forEach(aluno -> {
                sb.append("Aluno: ").append(aluno.getNumeroAluno()).append(" email: ").append(aluno.getEmail()).append("\n");
            } );
        });
        return sb.toString();
    }

    @Override
    public  Map<Proposta, ArrayList<Aluno>> getConflito() {
        Map<Proposta, ArrayList<Aluno>> proposta_aluno = data.getProposta_aluno();
        Map<Proposta, ArrayList<Aluno>> copia = new HashMap<>();
        ArrayList<Aluno> al = new ArrayList<>();
        int i = 0;
        for(Map.Entry<Proposta, ArrayList<Aluno>> set : proposta_aluno.entrySet()){
            if(i == 0){
                copia.put(set.getKey().getClone(),al);
            }
            for (Aluno aluno : set.getValue()) {
                al.add(aluno.getClone());
            }
            i++;
        }

        return copia;

    }
}
