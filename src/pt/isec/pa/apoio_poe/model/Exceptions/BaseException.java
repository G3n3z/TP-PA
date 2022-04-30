package pt.isec.pa.apoio_poe.model.Exceptions;


public class BaseException extends Exception{
    String atribute;
    ExceptionsCode errorCode;
    public BaseException(ExceptionsCode errorCode, String atribute) {
        this.errorCode = errorCode;
        this.atribute = atribute;
    }



}
