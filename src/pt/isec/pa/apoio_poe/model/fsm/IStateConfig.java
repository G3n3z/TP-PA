package pt.isec.pa.apoio_poe.model.fsm;

public interface IStateConfig extends IState {
    boolean recuar();
    boolean avancar();
}
