package pt.isec.pa.apoio_poe.model.Singleton;

import java.util.ArrayList;

public class MessageCenter {
    private static MessageCenter instance;
    ArrayList<String> messages;

    private MessageCenter() {
        messages = new ArrayList<>();
    }

    public static MessageCenter getInstance() {
        if (instance == null) {
            instance = new MessageCenter();
            return instance;
        }
        return instance;
    }

    public void putMessage(String message) {
        messages.add(message);
    }

    public String getMessage() {
        if (messages.size() == 0) {
            return "";
        }
        return messages.remove(0);
    }

    public int getNumOfMessages() {
        return messages.size();
    }

    public boolean hasNext() {
        return messages.size() > 0;
    }

    public String getAllMessage() {
        StringBuilder sb = new StringBuilder();
        messages.forEach(m -> sb.append(m).append("\n"));
        messages.clear();
        return sb.toString();
    }
}