package pt.isec.pa.apoio_poe.model.fsm;

public class OpcoesCandidatura extends StateAdapter{

    public OpcoesCandidatura(ApoioContext context, boolean isClosed) {
        super(context, isClosed);
    }


    // teste

    @Override
    public boolean recuarFase() {
        changeState(new ConfigOptions(context, context.getBooleanState(EnumState.CONFIG_OPTIONS)));
        return true;
    }


    //asf
    @Override
    public boolean avancarFase() {
        changeState(new AtribuicaoPropostas(context,context.getBooleanState(EnumState.ATRIBUICAOPROPOSTAS)));
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.OPCOES_CANDIDATURA;
    }

    @Override
    public boolean close() {
        setClose(true);
        return true;
    }
}
