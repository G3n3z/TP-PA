package pt.isec.pa.apoio_poe.model.Exceptions;

import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public class InvalidCSVField extends BaseException{


    public InvalidCSVField(String message) {
        super(message);
    }

    public InvalidCSVField(String message, int index) {
        super(message);
        putLine(index);
    }
    public InvalidCSVField(String message, int index, ErrorCode ...e) {
        super(message, e);
        putLine(index);
    }
}
