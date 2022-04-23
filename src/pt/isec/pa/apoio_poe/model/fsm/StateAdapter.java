package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

import java.io.*;

abstract class StateAdapter implements IState{
    ApoioContext context;
    Data data;
    private boolean close;

    public StateAdapter(ApoioContext context, boolean isClosed, Data data) {
        this.context = context;
        close = isClosed;
        this.data = data;
    }

    void changeState(IState newState){
        context.changeState(newState);
    }

    void changeState(EnumState newState){
        changeState(newState.createState(context, data));
    }

    @Override
    public boolean close() {
        close = true;
        return true;
    }

    @Override
    public boolean avancarFase() {
        return false;
    }
    @Override
    public boolean recuarFase() {
        return false;
    }
    @Override
    public boolean gerirAlunos() {
        return false;
    }
    @Override
    public boolean gerirDocentes() {return false;}
    @Override
    public boolean gerirEstagios() {
        return false;
    }

    @Override
    public boolean conflitoAtribuicaoCandidatura() {
        return false;
    }


    /* Metodos para saber se a fase esta fechada ou nao */
    public boolean isClose() {
        return close;
    }
    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public void obtencaoDadosOrientador() {}
    public void obtencaoListaProposta(){}

    @Override
    public void editarCandidaturas() {}

    @Override
    public void editarDocentes() {}

    @Override
    public void editarAlunos() {}

    @Override
    public void editarPropostas() {}

    @Override
    public boolean load() throws IOException, ClassNotFoundException {return false;}

    @Override
    public void begin() {}

    @Override
    public boolean save() throws IOException {
        return false;
    }

    @Override
    public void sair() {
        data.setLastState(getState());
        changeState(EnumState.SAIR);
    }

    @Override
    public void obtencaoListaAlunos() {}


    @Override
    public boolean existFileBin() {
        return false;
    }

    @Override
    public boolean fechado() {
        return close;
    }

    @Override
    public boolean addAluno(String file) {
        return false;
    }

    @Override
    public boolean importDocentes(String file){ return false; }

    @Override
    public boolean importPropostas(String file) { return false; }

    @Override
    public boolean addCandidatura(String file) {return false;}

    @Override
    public void changeName(String novo_nome, long nAluno) {}

    @Override
    public void removeAluno(long numero_de_aluno) {}

    @Override
    public void changeCursoAluno(String novo_curso, long nAluno) {}

    @Override
    public void changeRamoAluno(String novo_ramo, long nAluno) {}

    @Override
    public void changeClassAluno(double nova_classificaçao, long nAluno) {}

    @Override
    public void removeDocente(String numero_de_aluno){}

    @Override
    public void changeNameDocente(String novo_nome, String email) {}

    @Override
    public void addPropostaACandidatura(long nAluno, String idProposta) {}

    @Override
    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {}

    @Override
    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() {}

    @Override
    public boolean existConflict() {
        return false;
    }

    @Override
    public boolean resolveConflito(long numAluno) {
        return false;
    }

    @Override
    public void associacaoAutomaticaDeDocentesAPropostas() {}

    @Override
    public boolean gerirOrientadores() {
        return false;
    }

    @Override
    public boolean exportarCSV(String file) {
        return false;
    }

    @Override
    public void removeProposta(String id) {}

    @Override
    public void removePropostaACandidatura(String id, long naluno) {

    }

    @Override
    public String getAlunosToString() {
        return null;
    }

    @Override
    public String getDocentesToString() {
        return null;
    }

    @Override
    public String getPropostasToString() {
        return null;
    }

    @Override
    public String getPropostasWithFiltersToString(int[] filters) {
        return null;
    }

    @Override
    public String obtencaoAlunosComAutoProposta() {
        return null;
    }

    @Override
    public String obtencaoAlunosComCandidatura() {
        return null;
    }

    @Override
    public String obtencaoAlunosSemCandidatura() {
        return null;
    }

    @Override
    public String getTodosAlunosComPropostaAtribuida() {
        return null;
    }

    @Override
    public String obtencaoAlunosComAutoPropostaAtribuida() {
        return null;
    }

    @Override
    public String obtencaoAlunosSemProposta() {
        return null;
    }

    @Override
    public String getPropostasWithFiltersToStringAtribuicao(int[] filters) {
        return null;
    }

    @Override
    public String getAlunosComPropostaEOrientador() {
        return null;
    }

    @Override
    public String getAlunosComPropostaESemOrientador() {
        return null;
    }

    @Override
    public String getEstatisticasPorDocente() {
        return null;
    }

    @Override
    public String obtencaoAlunosSemPropostaComCandidatura() {
        return null;
    }

    @Override
    public String getPropostasDisponiveis() {
        return null;
    }

    @Override
    public String getConflitoToString() {
        return null;
    }

    @Override
    public String getPropostasAtribuidasToString() {
        return null;
    }
}
