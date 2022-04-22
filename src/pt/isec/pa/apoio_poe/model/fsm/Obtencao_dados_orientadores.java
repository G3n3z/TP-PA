package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class Obtencao_dados_orientadores extends StateAdapter{

    public Obtencao_dados_orientadores(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.OBTENCAO_DADOS_ORIENTADORES;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_ORIENTADORES);
        return true;
    }
}
