package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private Deque<ICommand> history;
    private Deque<ICommand> redoCmds;

    public CommandManager() {
        history = new ArrayDeque<>();
        redoCmds = new ArrayDeque<>();
    }


    public ErrorCode invokeCommand(ICommand cmd) {
        redoCmds.clear();
        ErrorCode errorCode = cmd.execute();
        if (errorCode == ErrorCode.E0) {
            history.push(cmd);
            return errorCode;
        }
        history.clear();
        return errorCode;
    }

    public boolean undo() {
        if (history.isEmpty())
            return false;
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true;
    }

    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }

    public boolean hasUndo(){
        return history.size() > 0;
    }

    public boolean hasRedo(){
        return redoCmds.size() > 0;
    }


}
