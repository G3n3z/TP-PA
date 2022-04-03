package pt.isec.pa.apoio_poe.model.fsm;

abstract class StateAdapter implements IState{
    ApoioContext context;
    private boolean close;
    public StateAdapter(ApoioContext context, boolean isClosed) {
        this.context = context;
        close = isClosed;
    }

    void changeState(IState newState){
        context.changeState(newState);
    }

    @Override
    public boolean fechar() {
        close = true;
        return true;
    }

    @Override
    public boolean gerirAlunos() {
        return false;
    }

    @Override
    public boolean gerirDocentes() {
        return false;
    }

    @Override
    public boolean gerirEstagios() {
        return false;
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
    public EnumState getState() {
        return null;
    }

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
}
