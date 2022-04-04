package pt.isec.pa.apoio_poe.model.fsm;

public class GestaoEstagios extends StateAdapter{

    public GestaoEstagios(ApoioContext context, boolean isClosed) {
        super(context, isClosed);
    }

    @Override
    public boolean recuarFase() {
        changeState(new ConfigOptions(context, false));
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_ESTAGIOS;
    }
}
