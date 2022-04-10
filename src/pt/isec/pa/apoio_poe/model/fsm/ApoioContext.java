package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

import java.util.HashMap;
import java.util.Map;

public class ApoioContext {
    private IState state;
    Map<EnumState, Boolean> closed;
    Data data;

    public ApoioContext() {
        data = new Data();
        closed = new HashMap<>();
        state = new ConfigOptions(this, false, data);
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


    public boolean addAluno(String file) {
        return state.addAluno(file);
    }

    public String getAlunos() {
        return data.getAlunos();
    }

    public boolean importDocentes(String file){ return state.importDocentes(file);}

    public String getDocentes(){
        return data.getDocentes();
    }

    public boolean importPropostas(String file) { return state.importPropostas(file);}

    public String getPropostas() {
        return data.getPropostas();
    }
}
