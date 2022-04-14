package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

public class Consulta extends StateAdapter{

    public Consulta(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }


    @Override
    public EnumState getState() {
        return EnumState.CONSULTA;
    }

    @Override
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }
        data.exportAlunosCandidaturaPropostaComOrientador();
        CSVWriter.closeFile();

        return true;
    }
}
