package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class ConflitoAtribuicaoCandidatura extends StateAdapter{

    public ConflitoAtribuicaoCandidatura(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean conflitoAtribuicaoCandidatura() {
        return true;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAOPROPOSTAS);
        return true;
    }
}
