package pt.isec.pa.apoio_poe.model.Exceptions;

import java.util.ArrayList;
import java.util.List;

public class CollectionBaseException extends Exception{
    List<BaseException> list;

    public CollectionBaseException() {
        this.list = new ArrayList<>();
    }
    public void putException(BaseException e){
        list.add(e);
    }

    public String getMessageOfExceptions() {
        StringBuilder sb = new StringBuilder();
        list.forEach(e -> sb.append(e.getExcepMessage()).append("\n"));
        return sb.toString();
    }
}
