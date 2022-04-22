package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class Editar_Candidaturas extends StateAdapter{
    public Editar_Candidaturas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.EDITAR_CANDIDATURAS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }
}
