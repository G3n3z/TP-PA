package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;

abstract class CommandAdapter implements ICommand{
    protected Data data;

    public CommandAdapter(Data data) {
        this.data = data;
    }

    @Override
    public boolean undo() {
        return false;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
