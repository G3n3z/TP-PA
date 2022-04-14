package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class Consulta extends StateAdapter{

    public Consulta(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }


    @Override
    public EnumState getState() {
        return EnumState.CONSULTA;
    }
}
