package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.Singleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;

import java.util.ArrayList;
import java.util.Collections;

public class AtribuicaoManualProposta extends CommandAdapter{
    private long nAluno;
    private String idProposta;

    public AtribuicaoManualProposta(Data data, long nAluno ,String idProposta) {

        super(data);
        this.nAluno = nAluno;
        this.idProposta = idProposta;
    }

    @Override
    public boolean execute() {
         
        Aluno a = data.getAluno(nAluno);
        if(a == null) {
           MessageCenter.getInstance().putMessage("Número de aluno " + nAluno + " incorreto.\n");
           return false;
        }
        Proposta p = data.getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(idProposta)).get(0);
        if(p instanceof Estagio && !a.isPossibilidade()) {
           MessageCenter.getInstance().putMessage("Aluno " + nAluno + " não pode aceder a estágio.\n");
           return false;
        }
        if(p.isAtribuida()){
           MessageCenter.getInstance().putMessage("Proposta já atribuida\n");
           return false;
        }
        a.setProposta(p);
        return true;
    }

    @Override
    public boolean undo() {
        return new RemocaoManualProposta(data, nAluno, idProposta).execute();
    }
}
