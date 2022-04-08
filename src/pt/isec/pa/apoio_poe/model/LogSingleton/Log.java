package pt.isec.pa.apoio_poe.model.LogSingleton;

import java.util.ArrayList;

public class Log {
    private static Log instance;
    ArrayList<String> messages;
    private Log(){
        messages = new ArrayList<>();
    }

    public static Log getInstance(){
        if(instance == null){
            instance = new Log();
            return instance;
        }
        return instance;
    }

    public void putMessage(String message){
        messages.add(message);
    }

    public String getMessage(){
        if(messages.size() == 0){
            return "";
        }
        return messages.remove(0);
    }

    public int getNumOfMessages(){
        return messages.size();
    }

    public boolean hasNext(){
        return messages.size() > 0;
    }

}
