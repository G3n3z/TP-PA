package pt.isec.pa.apoio_poe.model.Exceptions;

public class InvalidCSVField extends BaseException{

    public InvalidCSVField(String message) {
        super(message);
    }

    public InvalidCSVField(String message, int index) {
        super(message);
        putLine(index);
    }
}
