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
        context.changeState(new GestaoClientes(context, context.fechado()));
        return true;
    }

    @Override
    public boolean gerirDocentes() {
        context.changeState(new GestaoDocentes(context, context.fechado()));
        return true;
    }

    @Override
    public boolean gerirEstagios() {
        context.changeState(new GestaoEstagios(context, context.fechado()));
        return true;
    }
}
