package pt.isec.pa.apoio_poe.model.fsm;

public class OpcoesCandidatura extends StateAdapter{

    IState previousState;
    IState atribuirPropostas = new AtribuicaoPropostas(context, false, this);
    public OpcoesCandidatura(ApoioContext context, boolean isClosed, IState previousState) {
        super(context, isClosed);
        this.previousState = previousState;
    }

    @Override
    public boolean recuarFase() {
        changeState(previousState);
        return true;
    }


    //asf
    @Override
    public boolean avancarFase() {
        changeState(atribuirPropostas);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.OPCOES_CANDIDATURA;
    }
}
