package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
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
    public ErrorCode close() {
        //TODO Verificar se a anterior esta fechada
        setClose(true);
        changeState(EnumState.CONSULTA);
        data.closeState(getState());
        return ErrorCode.E0;
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
    public ErrorCode exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return ErrorCode.E2;
        }
        for(Aluno a : data.getAlunos()) {
            CSVWriter.writeLine(",", false, false, a.getExportAluno());
            if(a.temCandidatura())
                CSVWriter.writeLine(",", false, true,a.getCandidatura().getExportCandidatura());

            if(a.temPropostaConfirmada()) {
                CSVWriter.writeLine(",", false, true, a.getProposta().exportProposta());
                CSVWriter.writeLine(",", false, true, a.getOrdem());

                if (a.getProposta().temDocenteOrientador()) {
                    CSVWriter.writeLine(",", false, true, a.getProposta().getOrientador().getExportDocente());
                }
            }

            CSVWriter.writeLine(",", true,false);
        }

        CSVWriter.closeFile();

        return ErrorCode.E0;
    }
    @Override
    public void obtencaoDadosOrientador() {
        changeState(EnumState.OBTENCAO_DADOS_ORIENTADORES);
    }
}
