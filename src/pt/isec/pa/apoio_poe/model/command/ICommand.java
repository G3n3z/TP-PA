package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public interface ICommand {
    ErrorCode undo();
    ErrorCode execute();
}
