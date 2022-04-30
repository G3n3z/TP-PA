package pt.isec.pa.apoio_poe.model.Exceptions;

public class InvalidOperation extends BaseException{
    public InvalidOperation(ExceptionsCode code, String atribute) {
        super(code, atribute);
    }
}
