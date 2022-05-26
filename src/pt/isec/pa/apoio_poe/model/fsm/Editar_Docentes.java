package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public class Editar_Docentes extends StateAdapter{

    public Editar_Docentes(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.EDITAR_DOCENTES;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.GESTAO_DOCENTES);
        return true;
    }

    @Override
    public ErrorCode changeNameDocente(String novo_nome, String email) {
        if(!data.changeNameDocente(novo_nome, email)){
            //MessageCenter.getInstance().putMessage("Nao existe docente com este email");
            return ErrorCode.E4;
        }
        return ErrorCode.E0;
    }
}
