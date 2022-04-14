package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

public class AtribuicaoOrientadores extends StateAdapter {
    public AtribuicaoOrientadores(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean avancarFase() {
        return false;
    }


    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAOPROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAO_ORIENTADORES;
    }

    @Override
    public boolean close() {
        setClose(true);
        changeState(EnumState.CONSULTA);
        return true;
    }

    @Override
    public void associacaoAutomaticaDeDocentesAPropostas() {
        data.associacaoAutomaticaDeDocentesAPropostas();
    }

    @Override
    public boolean gerirOrientadores() {
        changeState(EnumState.GESTAO_ORIENTADORES);
        return true;
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
