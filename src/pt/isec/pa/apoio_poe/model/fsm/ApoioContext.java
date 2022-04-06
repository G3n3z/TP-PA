package pt.isec.pa.apoio_poe.model.fsm;

import java.util.HashMap;
import java.util.Map;

public class ApoioContext {
    private IState state;
    Map<EnumState, Boolean> closed;

    public ApoioContext() {
        closed = new HashMap<>();
        state = new ConfigOptions(this, false);
        closed.putIfAbsent(state.getState(),state.fechado());
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

    public boolean closeFase(){
        closed.put(state.getState(), true);
        return state.close();
    }

    public boolean isClosed(){
        return state.fechado();
    }

    public boolean getBooleanState(EnumState enumstate){
        Boolean state = closed.get(enumstate);
        if(state == null){
            closed.put(enumstate, false);
            return false;
        }
        else {
            return state;
        }
    }







}
