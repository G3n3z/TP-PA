package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.data.Data;

import java.util.HashMap;
import java.util.Map;

public class ApoioContext {
    private IState state;
    Map<EnumState, Boolean> closed;
    Data data;

    public ApoioContext() {
        data = new Data();
        closed = new HashMap<>();
        state = new ConfigOptions(this, false, data);
        closed.putIfAbsent(state.getState(),state.fechado());
    }

    void changeState(IState newState){
        state = newState;
    }

    public EnumState getState(){
        return state.getState();
    }

    public String getName(){
        return state.getState().toString();
    }

    public boolean avancarFase(){
        return state.avancarFase();
    }

    public boolean recuarFase(){
        return state.recuarFase();
    }

    public boolean gerirAlunos(){ return state.gerirAlunos();}

    public boolean gerirDocentes(){
        return state.gerirDocentes();
    }

    public  boolean gerirEstagios(){
        return state.gerirEstagios();
    }

    public boolean closeFase(){
        if(state.close())
            closed.put(state.getState(), true);
        return true;
    }

    public boolean isClosed(){
        return state.fechado();
    }

    public boolean getBooleanState(EnumState enumstate){
        Boolean state = closed.get(enumstate);
        if(state == null){
            closed.put(enumstate, false);
            return false;
        }
        else {
            return state;
        }
    }


    public boolean addAluno(String file) {
        return state.addAluno(file);
    }

    public String getAlunos() {
        return data.getAlunos();
    }

    public boolean importDocentes(String file){ return state.importDocentes(file);}

    public String getDocentes(){
        return data.getDocentes();
    }

    public boolean importPropostas(String file) { return state.importPropostas(file);}

    public String getPropostas() {
        return data.getPropostasToString();
    }

    public String getPropostasWithFiltersToString(int ...filters){
        return data.getPropostasWithFiltersToString(filters);
    }

    public String getPropostasWithFiltersToStringAtribuicao(int ...filters){
        return data.getPropostasWithFiltersToStringAtribuicao(filters);
    }

    public String getCandidaturas(){return data.getCandidaturasToString();}

    public void addCandidatura(String file) {
        state.addCandidatura(file);
    }

    public void changeNameAluno(String novo_nome, long nAluno) {
        state.changeName(novo_nome, nAluno);
    }

    public void removeAluno(long numero_de_aluno) {
        state.removeAluno(numero_de_aluno);
    }

    public void changeCursoAluno(String novo_curso, long nAluno) {
        state.changeCursoAluno(novo_curso, nAluno);
    }

    public void changeRamoAluno(String novo_ramo, long nAluno) {
        state.changeRamoAluno(novo_ramo, nAluno);
    }

    public void changeClassAluno(double nova_classificaçao, long nAluno) {
        state.changeClassAluno(nova_classificaçao, nAluno);
    }

    public void removeDocente(String emailDocente) {
        state.removeDocente(emailDocente);
    }

    public void changeNomeDocente(String novo_nome, String email) {
        state.changeNameDocente(novo_nome, email);
    }

    public String obtencaoAlunosComAutoProposta(){
        return data.obtencaoAlunosComAutoProposta();
    }

    public String obtencaoAlunosComAutoPropostaAtribuida() {
        return data.obtencaoAlunosComAutoPropostaAtribuida();
    }

    public String obtencaoAlunosComCandidatura(){
        return data.obtencaoAlunosComCandidatura();
    }

    public String obtencaoAlunosSemCandidatura(){
        return data.obtencaoAlunosSemCandidatura();
    }

    public String obtencaoAlunosSemProposta(){
        return data.obtencaoAlunosSemProposta();
    }

    public void addPropostaACandidatura(long nAluno, String idProposta) {
        state.addPropostaACandidatura(nAluno, idProposta);
    }

    public void removePropostaACandidatura(long readLong, String readString) {
    }

    public void atribuicaoAutomatica() {
        state.atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();
    }

    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() {
        state.atribuicaoAutomaticaSemAtribuicoesDefinidas();
    }

    public void conflitoAtribuicaoCandidatura() {
        state.conflitoAtribuicaoCandidatura();
    }

    public String getConflitoToString() {
        return data.getConflitoToString();
    }

    public boolean existConflict() {
        return state.existConflict();
    }

    public String consultaAlunosConflito() {
        return data.consultaAlunosConflito();
    }

    public String consultaPropostaConflito() {
        return data.consultaPropostaConflito();
    }

    public boolean resolveConflito(long numAluno) {
        return state.resolveConflito(numAluno);
    }

    public String getTodosAlunosComPropostaAtribuida() {
        return data.getTodosAlunosComPropostaAtribuida();
    }

    public ApoioManager getManager() {
        return new ApoioManager(data);
    }

    public void associacaoAutomaticaDeDocentesAPropostas() {
        state.associacaoAutomaticaDeDocentesAPropostas();
    }

    public void gerirOrientadores() {
        state.gerirOrientadores();
    }

    public String getAlunosComPropostaEOrientador() {
        return data.getAlunosComPropostaEOrientador();
    }

    public String getAlunosComPropostaESemOrientador() {
        return data.getAlunosComPropostaESemOrientador();
    }

    public String getEstatisticasPorDocente() {
        return data.getEstatisticasPorDocente();
    }

    public String obtencaoAlunosSemPropostaComCandidatura() {
        return data.obtencaoAlunosSemPropostaComCandidatura();
    }

    public void exportaCSV(String file) {
        state.exportarCSV(file);
    }
}

