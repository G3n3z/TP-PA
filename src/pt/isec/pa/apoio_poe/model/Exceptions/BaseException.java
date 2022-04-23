package pt.isec.pa.apoio_poe.model.Exceptions;

import pt.isec.pa.apoio_poe.model.data.Aluno;

public class BaseException extends Exception{
    String message;
    int index;
    public BaseException(String message) {
        this.message = message;
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
}
