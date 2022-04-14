package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;

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
         if(data.existePropostaSemAluno(idProposta)){
             if(!data.verificaNumAluno(nAluno)){
                 Log.getInstance().putMessage("Número de aluno " + nAluno + " incorreto.\n");
                 return false;
             }
             if(data.verificaElegibilidade(nAluno, idProposta)){
                 Log.getInstance().putMessage("Aluno " + nAluno + " não pode aceder a estágio.\n");
                 return false;
             }
             if(!data.atribuicaoManual(nAluno, idProposta)){
                 Log.getInstance().putMessage("Algo correu mal\n");
                 return false;
             }
             return true;
         }
         Log.getInstance().putMessage("Proposta " + idProposta + " com id incorreto.\n");
         return false;
    }

    @Override
    public boolean undo() {
        return data.remocaoManual(nAluno, idProposta);
    }
}
