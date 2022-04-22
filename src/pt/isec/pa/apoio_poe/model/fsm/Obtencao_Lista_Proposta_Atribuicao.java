package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class Obtencao_Lista_Proposta_Atribuicao extends StateAdapter{

    public Obtencao_Lista_Proposta_Atribuicao(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.OBTENCAO_LISTA_PROPOSTA_ATRIBUICAO;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }


}
