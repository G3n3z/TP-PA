package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.StateNotClosed;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
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
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAO_ORIENTADORES;
    }

    @Override
    public boolean close() throws StateNotClosed {
        if(data.getBooleanState(EnumState.ATRIBUICAO_PROPOSTAS)) {
            setClose(true);
            changeState(EnumState.CONSULTA);
            return true;
        }
        throw new StateNotClosed("Fase anterior aberta");
    }

    @Override
    public void associacaoAutomaticaDeDocentesAPropostas() {
        for(Proposta proposta : data.getProposta()){
            if(!proposta.temDocenteOrientador() && proposta.temDocenteProponente()){
                proposta.setDocenteOrientadorDocenteProponente();
            }
        }
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
        for(Aluno a : data.getAlunos()) {
            CSVWriter.writeLine(",", false, false, a.getExportAluno());
            if(a.temCandidatura())
                CSVWriter.writeLine(",", false, true,a.getCandidatura().getExportCandidatura());
            if(a.temPropostaConfirmada()) {
                CSVWriter.writeLine(",", false, true, a.getProposta().exportProposta());
                CSVWriter.writeLine(",", false, true, a.getOrdem());
            }
            if(a.getProposta().temDocenteOrientador()){
                CSVWriter.writeLine(",",false,true,a.getProposta().getOrientador().getExportDocente());
            }
            CSVWriter.writeLine(",", true,false);
        }

        CSVWriter.closeFile();

        return true;
    }
    @Override
    public void obtencaoDadosOrientador() {
        changeState(EnumState.OBTENCAO_DADOS_ORIENTADORES);
    }
}
