package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

abstract class CommandAdapter implements ICommand{
    protected Data data;

    public CommandAdapter(Data data) {
        this.data = data;
    }

    @Override
    public ErrorCode undo() {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode execute() {
        return ErrorCode.E25;
    }
}
