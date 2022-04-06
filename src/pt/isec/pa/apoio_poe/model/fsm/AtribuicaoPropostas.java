package pt.isec.pa.apoio_poe.model.fsm;

public class AtribuicaoPropostas extends StateAdapter{


    public AtribuicaoPropostas(ApoioContext context, boolean isClosed) {
        super(context, isClosed);
    }

    @Override
    public boolean recuarFase() {
        changeState(new OpcoesCandidatura(context, context.getBooleanState(EnumState.OPCOES_CANDIDATURA)));
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
}
