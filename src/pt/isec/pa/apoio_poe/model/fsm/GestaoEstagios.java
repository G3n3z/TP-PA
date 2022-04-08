package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class GestaoEstagios extends StateAdapter{

    public GestaoEstagios(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_PROPOSTAS;
    }
}
