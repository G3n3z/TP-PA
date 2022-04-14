package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public class ConfigOptions extends  StateAdapter{


    public ConfigOptions(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.CONFIG_OPTIONS;
    }

    @Override
    public boolean close() {
        //if(data.verificaCondicaoFechoF1())
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
        changeState(EnumState.GESTAO_PROPOSTAS);
        return true;
    }

    @Override
    public boolean avancarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

}
