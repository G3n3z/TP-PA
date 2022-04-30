package pt.isec.pa.apoio_poe.model.Exceptions;

public class InvalidCSVField extends CSVException {

    public InvalidCSVField(ExceptionsCode code, String atribute, int index) {
        super(code, atribute, index);
    }
    }
}
