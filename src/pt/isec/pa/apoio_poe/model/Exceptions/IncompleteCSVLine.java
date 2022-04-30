package pt.isec.pa.apoio_poe.model.Exceptions;

public class IncompleteCSVLine extends CSVException {

    public IncompleteCSVLine(ExceptionsCode code, String atribute, int index) {
        super(code, atribute, index);
    }
}
