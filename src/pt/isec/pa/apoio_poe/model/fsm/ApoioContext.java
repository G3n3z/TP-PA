package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.data.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApoioContext {
    private IState state;
    Map<EnumState, Boolean> closed;
    Data data;

    public ApoioContext() {
        data = new Data();
        closed = new HashMap<>();
        state = new LoadState(this, false, data);
        closed.putIfAbsent(state.getState(),state.fechado());
        data.getBooleanState(EnumState.LOAD_STATE);
    }
    public boolean getBooleanState(EnumState enumstate){
        return data.getBooleanState(enumstate);
    }

    void changeState(IState newState){
        state = newState;
        //data = newState.getData();
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
            data.closeState(state.getState());
        return true;
    }

    public boolean isClosed(){
        return state.fechado();
    }



    public boolean addAluno(String file) throws CollectionBaseException {
        return state.addAluno(file);
    }

    public String getAlunosToString() {
        return state.getAlunosToString();
    }

    public boolean importDocentes(String file) throws CollectionBaseException { return state.importDocentes(file);}

    public String getDocentesToString(){
        return state.getDocentesToString();
    }

    public boolean importPropostas(String file) { return state.importPropostas(file);}

    public String getPropostasToString() {
        return state.getPropostasToString();
    }

    public String getPropostasWithFiltersToString(int ...filters){
        return state.getPropostasWithFiltersToString(filters);
    }

    public String getPropostasWithFiltersToStringAtribuicao(int ...filters){
        return state.getPropostasWithFiltersToStringAtribuicao(filters);
    }

    public String getCandidaturas(){return data.getCandidaturasToString();}

    public void addCandidatura(String file) throws CollectionBaseException {
        state.addCandidatura(file);
    }

    public boolean changeNameAluno(long nAluno, String novo_nome) {
        return state.changeName(novo_nome, nAluno);
    }

    public void removeAluno(long numero_de_aluno) {
        state.removeAluno(numero_de_aluno);
    }

    public void changeCursoAluno(long nAluno, String novo_curso) {
        state.changeCursoAluno(novo_curso, nAluno);
    }

    public void changeRamoAluno(long nAluno,String novo_ramo) {
        state.changeRamoAluno(novo_ramo, nAluno);
    }

    public void changeClassAluno(long nAluno, double nova_classificacao) {
        state.changeClassAluno(nova_classificacao, nAluno);
    }

    public void removeDocente(String emailDocente) {
        state.removeDocente(emailDocente);
    }

    public void changeNomeDocente(String email, String novo_nome) {
        state.changeNameDocente(novo_nome, email);
    }

    public String obtencaoAlunosComAutoProposta(){
        return state.obtencaoAlunosComAutoProposta();
    }

    public String obtencaoAlunosComAutoPropostaAtribuida() {
        return state.obtencaoAlunosComAutoPropostaAtribuida();
    }

    public String obtencaoAlunosComCandidatura(){
        return state.obtencaoAlunosComCandidatura();
    }

    public String obtencaoAlunosSemCandidatura(){
        return state.obtencaoAlunosSemCandidatura();
    }

    public String obtencaoAlunosSemProposta(){
        return state.obtencaoAlunosSemProposta();
    }

    public void addPropostaACandidatura(long nAluno, String idProposta) {
        state.addPropostaACandidatura(nAluno, idProposta);
    }

    public void removePropostaACandidatura(long nAluno, String id) {
        state.removePropostaACandidatura(id, nAluno);
    }

    public void atribuicaoAutomatica() {
        state.atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();
    }

    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() throws ConflitoAtribuicaoAutomaticaException {
        state.atribuicaoAutomaticaSemAtribuicoesDefinidas();
    }

    public void conflitoAtribuicaoCandidatura() {
        state.conflitoAtribuicaoCandidatura();
    }

    public String getConflitoToString() {
        return state.getConflitoToString();
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
        return state.getTodosAlunosComPropostaAtribuida();
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
        return state.getAlunosComPropostaEOrientador();
    }

    public String getAlunosComPropostaESemOrientador() {
        return state.getAlunosComPropostaESemOrientador();
    }

    public String getEstatisticasPorDocente() {
        return state.getEstatisticasPorDocente();
    }

    public String obtencaoAlunosSemPropostaComCandidatura() {
        return state.obtencaoAlunosSemPropostaComCandidatura();
    }

    public void exportaCSV(String file) {
        state.exportarCSV(file);
    }

    public String getPropostasDisponiveis() {
        return state.getPropostasDisponiveis();
    }

    public String getPropostasAtribuidas(){
        return state.getPropostasAtribuidasToString();
    }

    public void removerProposta(String id) {
        state.removeProposta(id);
    }

    public void obtencaoDadosOrientador() {
        state.obtencaoDadosOrientador();
    }

    public void obtencaoListaProposta() {
        state.obtencaoListaProposta();
    }

    public void obtencaoListaAlunos() {
        state.obtencaoListaAlunos();
    }

    public void editarAlunos() {
        state.editarAlunos();
    }

    public void editarCandidaturas() {
        state.editarCandidaturas();
    }

    public void editarDocentes() {
        state.editarDocentes();
    }

    public void editarPropostas() {
        state.editarPropostas();
    }

    public void load() throws IOException, ClassNotFoundException {
        state.load();
    }

    public void begin() {
        state.begin();
    }

    public void sair() {
        state.sair();
    }

    public void save() throws IOException {
        state.save();
    }

    public boolean existsFileBin() {
        return state.existFileBin();
    }

    protected void setData(Data data) {
        this.data = data;
    }

    public void gestaoManualAtribuicoes() {
        state.gestaoManualAtribuicoes();
    }

    public boolean removeAll() {
        return state.removeAll();
    }

    public boolean changeTitulo(String id, String novo_titulo) {
        return state.changeTitulo(id, novo_titulo);
    }

    public boolean changeEntidade(String id, String nova_entidade){
        return state.changeEntidade(id, nova_entidade);
    }

    public boolean addRamo(String id, String ramo) {
        return state.addRamo(id, ramo);
    }

    public boolean removeRamo(String id, String ramo) {
        return state.removeRamo(id, ramo);
    }
}

