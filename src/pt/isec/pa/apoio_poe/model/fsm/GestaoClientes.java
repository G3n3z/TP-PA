package pt.isec.pa.apoio_poe.model.fsm;

public class GestaoClientes extends StateAdapter{

    public GestaoClientes(ApoioContext context, boolean isClosed) {
        super(context, isClosed);
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_CLIENTES;
    }

    @Override
    public boolean recuarFase() {
        context.changeState(new ConfigOptions(context, false));
        return true;
    }
}
