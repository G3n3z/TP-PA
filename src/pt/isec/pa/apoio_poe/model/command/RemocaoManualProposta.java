package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

public class RemocaoManualProposta extends CommandAdapter{

    private long nAluno;
    private String idProposta;

    public RemocaoManualProposta(Data data, long nAluno ,String idProposta) {
        super(data);
        this.nAluno = nAluno;
        this.idProposta = idProposta;
    }

    @Override
    public boolean execute() {
        Aluno a = data.getAluno(nAluno);
        if(a == null)
            return false;
        if(a.getProposta() instanceof Projeto_Estagio || a.getProposta().getNumAluno() != null)
            return false;
        a.removeProposta();
        return true;
    }

    @Override
    public boolean undo() {
        return new AtribuicaoManualProposta(data, nAluno,idProposta).execute();
    }
}
