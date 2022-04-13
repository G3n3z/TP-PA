package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;

public class AtribuicaoManualProposta extends CommandAdapter{
    private long nAluno;
    private String idProposta;

    public AtribuicaoManualProposta(Data data, long nAluno ,String idProposta) {
        super(data);
    }

    @Override
    public boolean execute() {
         if(data.existePropostaSemAluno(idProposta)){
            return data.atribuicaoManual(nAluno, idProposta);
         }
         return false;
    }

    @Override
    public boolean undo() {
        return data.remocaoManual(nAluno, idProposta);
    }
}
