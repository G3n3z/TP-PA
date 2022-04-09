package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

abstract class StateAdapter implements IState{
    ApoioContext context;
    Data data;
    private boolean close;

    public StateAdapter(ApoioContext context, boolean isClosed, Data data) {
        this.context = context;
        close = isClosed;
        this.data = data;
    }

    void changeState(IState newState){
        context.changeState(newState);
    }

    void changeState(EnumState newState){
        changeState(newState.createState(context, data));
    }

    @Override
    public boolean close() {
        close = true;
        return true;
    }

    @Override
    public boolean avancarFase() {
        return false;
    }
    @Override
    public boolean recuarFase() {
        return false;
    }
    @Override
    public boolean gerirAlunos() {
        return false;
    }
    @Override
    public boolean gerirDocentes() {return false;}
    @Override
    public boolean gerirEstagios() {
        return false;
    }
    @Override
    public EnumState getState() {
        return null;
    }


    /* Metodos para saber se a fase esta fechada ou nao */
    public boolean isClose() {
        return close;
    }
    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public boolean fechado() {
        return close;
    }

    @Override
    public boolean addAluno(String file) {
        return false;
    }


    @Override
    public boolean importDocentes(String file){ return false; }

}
