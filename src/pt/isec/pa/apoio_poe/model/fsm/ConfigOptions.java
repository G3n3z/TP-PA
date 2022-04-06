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
    public boolean close() {
        setClose(true);
        return true;
    }

    @Override
    public boolean gerirAlunos() {
        changeState(EnumState.GESTAO_CLIENTES);
        return true;
    }

    @Override
    public boolean gerirDocentes() {
        changeState(EnumState.GESTAO_DOCENTES);
        return true;
    }

    @Override
    public boolean gerirEstagios() {
        changeState(EnumState.GESTAO_ESTAGIOS);
        return true;
    }

    @Override
    public boolean avancarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }


}
