package pt.isec.pa.apoio_poe.model.fsm;

public class AtribuicaoPropostas extends StateAdapter{

    IState previousState;
    public AtribuicaoPropostas(ApoioContext context, boolean isClosed, IState previousState) {
        super(context, isClosed);
        this.previousState = previousState;
    }

    @Override
    public boolean recuarFase() {
        changeState(previousState);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAOPROPOSTAS;
    }
}
