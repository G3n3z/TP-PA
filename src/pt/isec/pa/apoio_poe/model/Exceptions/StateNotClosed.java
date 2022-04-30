package pt.isec.pa.apoio_poe.model.Exceptions;

public class StateNotClosed extends BaseException{

    public StateNotClosed(ExceptionsCode code, String atribute) {
        super(code, atribute);
    }

}
