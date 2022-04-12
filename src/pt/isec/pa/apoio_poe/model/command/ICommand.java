package pt.isec.pa.apoio_poe.model.command;

public interface ICommand {
    boolean undo();
    boolean execute();
}
