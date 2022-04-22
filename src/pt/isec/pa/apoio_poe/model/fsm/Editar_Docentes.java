package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;

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
    public void changeNameDocente(String novo_nome, String email) {
        if(!data.changeNameDocente(novo_nome, email)){
            Log.getInstance().putMessage("Nao existe docente com este email");
        }
    }
}
