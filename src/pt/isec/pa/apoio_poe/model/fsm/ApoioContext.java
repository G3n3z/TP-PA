package pt.isec.pa.apoio_poe.model.fsm;

public class ApoioContext {
    private IState state;


    public ApoioContext() {
        state = new ConfigOptions(this, false);
    }
    void changeState(IState newState){
        state = newState;
    }
    public EnumState getState(){
        return state.getState();
    }
    public String getName(){
        return state.getState().toString();
    }

    public boolean avancarFase(){
        return state.avancarFase();
    }

    public boolean recuarFase(){
        return state.recuarFase();
    }
    public boolean gerirAlunos(){ return state.gerirAlunos();}
    public boolean gerirDocentes(){
        return state.gerirDocentes();
    }
    public  boolean gerirEstagios(){
        return state.gerirEstagios();
    }
    public boolean fecharFase(){
        return state.fechar();
    }

    public boolean fechado(){
        return state.fechado();
    }



}
