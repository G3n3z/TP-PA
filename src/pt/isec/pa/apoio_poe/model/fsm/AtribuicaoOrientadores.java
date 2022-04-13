package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class AtribuicaoOrientadores extends StateAdapter {
    public AtribuicaoOrientadores(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean avancarFase() {
        return false;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAOPROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAO_ORIENTADORES;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public void associacaoAutomaticaDeDocentesAPropostas() {
        data.associacaoAutomaticaDeDocentesAPropostas();
    }

    @Override
    public boolean gestaoOrientadores() {
        changeState(EnumState.GESTAO_ORIENTADORES);
        return true;
    }
}
