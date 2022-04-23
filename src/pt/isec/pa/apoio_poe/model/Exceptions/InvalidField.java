package pt.isec.pa.apoio_poe.model.Exceptions;

import pt.isec.pa.apoio_poe.model.data.Pessoa;

public class InvalidField extends BaseException{

    public InvalidField(String message) {
        super(message);
    }
}
