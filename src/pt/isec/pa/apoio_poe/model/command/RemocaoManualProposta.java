package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;

public class RemocaoManualProposta extends CommandAdapter{

    private long nAluno;
    private String idProposta;

    public RemocaoManualProposta(Data data, long nAluno ,String idProposta) {
        super(data);
    }

    @Override
    public boolean execute() {
        return data.remocaoManual(nAluno, idProposta);
    }

    @Override
    public boolean undo() {
        if(data.existePropostaSemAluno(idProposta)){
            return data.atribuicaoManual(nAluno, idProposta);
        }
        return false;
    }
}
