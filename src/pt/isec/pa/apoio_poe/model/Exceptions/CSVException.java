package pt.isec.pa.apoio_poe.model.Exceptions;

public class CSVException extends BaseException{
    String message;
    int index;

    public CSVException(ExceptionsCode errorCode, String atribute, int index) {
        super(errorCode, atribute);
        this.index = index;
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
