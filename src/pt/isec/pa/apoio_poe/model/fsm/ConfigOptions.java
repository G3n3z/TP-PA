package pt.isec.pa.apoio_poe.model.fsm;

public class ConfigOptions extends  StateAdapter{
    public ConfigOptions(ApoioContext context, Boolean isClosed) {
        super(context, isClosed);
    }

    @Override
    public EnumState getState() {
        return EnumState.CONFIG_OPTIONS;
    }

    @Override
    public boolean fechar() {
        setClose(true);
        return true;
    }

    @Override
    public boolean gerirAlunos() {
        if(isClose())
            return false;
        context.changeState(new GestaoClientes(context, false));
        return true;
    }

    @Override
    public boolean gerirDocentes() {
        if(isClose())
            return false;
        context.changeState(new GestaoDocentes(context, false));
        return true;
    }

    @Override
    public boolean gerirEstagios() {
        if(isClose())
            return false;
        context.changeState(new GestaoEstagios(context, false));
        return true;
    }
}
