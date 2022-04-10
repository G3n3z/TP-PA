package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class AtribuicaoPropostas extends StateAdapter{


    public AtribuicaoPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAOPROPOSTAS;
    }

    @Override
    public boolean close() {
        setClose(true);
        return true;
    }

    @Override
    public void atribuicaoAutomaticaEstagio_Proposta() {
        data.atribuicaoAutomaticaEstagio_Proposta();
    }
}
