package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class AtribuicaoManualPropostas extends StateAdapter {

    public AtribuicaoManualPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase(){
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() { return EnumState.ATRIBUICAO_MANUAL_PROPOSTAS; }
}
