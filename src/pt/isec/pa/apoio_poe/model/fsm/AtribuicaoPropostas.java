package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class AtribuicaoPropostas extends StateAdapter{


    public AtribuicaoPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public boolean conflitoAtribuicaoCandidatura() {
        changeState(EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAOPROPOSTAS;
    }

    @Override
    public boolean close() {
        setClose(true);
        return true;
    }

    @Override
    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {
        data.atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();
    }

    @Override
    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() {
        data.atribuicaoAutomaticaSemAtribuicoesDefinidas();
    }
}
