package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class EditarPropostas extends StateAdapter{
    public EditarPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.EDITAR_PROPOSTAS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.GESTAO_PROPOSTAS);
        return true;
    }

}
