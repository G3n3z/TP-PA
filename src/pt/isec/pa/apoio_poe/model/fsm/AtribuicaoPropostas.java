package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

public class AtribuicaoPropostas extends StateAdapter{


    public AtribuicaoPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean avancarFase(){
        changeState(EnumState.ATRIBUICAO_ORIENTADORES);
        return true;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public boolean conflitoAtribuicaoCandidatura() {
        changeState(EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAO_PROPOSTAS;
    }

    @Override
    public boolean close() {
        if(data.todosOsAlunosComCandidaturaTemPropostaAssocaida()){
            setClose(true);
            return true;
        }
        Log.getInstance().putMessage("Nao foi possivel fechar");
        Log.getInstance().putMessage(data.qualAlunoComCandidaturaSemPropostaAssocaida());
        return false;
    }

    @Override
    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {
        data.atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();
    }

    @Override
    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() {
        data.atribuicaoAutomaticaSemAtribuicoesDefinidas();
    }

    @Override
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }
        data.exportAlunosCandidaturaProposta();
        CSVWriter.closeFile();

        return true;
    }
}
