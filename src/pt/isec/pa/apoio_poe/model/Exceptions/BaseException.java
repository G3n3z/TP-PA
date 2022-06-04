package pt.isec.pa.apoio_poe.model.Exceptions;


import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class BaseException extends ApoioException{
    String message;
    int index;
    List<ErrorCode> erros;
    public BaseException(String message) {
        this.message = message;
    }
    public BaseException(String message, ErrorCode ...e) {
        this.message = message;
        erros = new ArrayList<>();
        erros.addAll(List.of(e));
    }
    public void putLine(int index) {
        this.index = index;
    }

    public void addToMessage(String text) {
        message = message + text;
    }
    public void addToBeginMessage(String text){
        message = text + message;
    }

    public String getExcepMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        if (erros == null){
            return ErrorCode.E0;
        }
        if(erros.size() > 0){
            return erros.get(0);
        }
        return ErrorCode.E0;
    }
}
