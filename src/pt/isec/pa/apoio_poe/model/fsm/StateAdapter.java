package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * Funcao que permite fechar a fase, implementacoes diferente conforme o estado em si
     * @return ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode close() {
        close = true;
        return ErrorCode.E0;
    }

    /**
     * Transicao de estado, implementado em alguns estados concretos
     * @return boolean para saber como correu a operação
     */
    @Override
    public boolean avancarFase() {
        return false;
    }

    /**
     * Funcao que faz com que os estados que tenham implementado esta transição a efetuem
     * @return booleano a dizer como correu
     */
    @Override
    public boolean recuarFase() {
        return false;
    }

    /**
     * Transicao de estado
     * @return um boolean para saber como correu a operacao
     */
    @Override
    public boolean gerirAlunos() {
        return false;
    }
    /**
     * Transicao de estado
     * @return um boolean para saber como correu a operacao
     */
    @Override
    public boolean gerirDocentes() {return false;}

    /**
     * Transicao de estado
     * @return um boolean para saber como correu a operacao
     */
    @Override
    public boolean gerirEstagios() {
        return false;
    }

    /**
     * Transicao de estado para o conflito
     * @return um boolean para saber como correu a operacao
     */
    @Override
    public boolean conflitoAtribuicaoCandidatura() {
        return false;
    }


    /**
     *  Metodos para saber se a fase esta fechada ou nao */
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
    public boolean load() throws IOException, ClassNotFoundException {
        return false;
    }

    @Override
    public void begin() {}

    /**
     *
     * @return retorna true se consegui guardar o estado da aplicação
     * @throws IOException lança exceção caso não consiga abrir o ficheiro
     */
    @Override
    public boolean save() throws IOException {
        if(getState() != EnumState.LOAD_STATE || getState() != EnumState.SAIR)
            data.setLastState(getState());
        try {
            ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(data.getFicheiroBin()));
            ois.writeObject(data);

        }catch (Exception e){
            throw e;
        }
        return true;
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
        return Files.exists(Path.of(data.getFicheiroBin()));
    }

    @Override
    public boolean fechado() {
        return close;
    }

    @Override
    public boolean addAluno(String file) throws CollectionBaseException {
        return false;
    }

    @Override
    public boolean importDocentes(String file) throws CollectionBaseException { return false; }

    @Override
    public boolean importPropostas(String file) throws CollectionBaseException { return false; }

    @Override
    public boolean addCandidatura(String file) throws CollectionBaseException {return false;}

    @Override
    public ErrorCode changeName(String novo_nome, long nAluno) {return ErrorCode.E25;}

    @Override
    public ErrorCode removeAluno(long numero_de_aluno) {return ErrorCode.E25;}

    @Override
    public ErrorCode changeCursoAluno(String novo_curso, long nAluno) {return ErrorCode.E25;}

    @Override
    public ErrorCode changeRamoAluno(String novo_ramo, long nAluno) {return ErrorCode.E25;}

    @Override
    public ErrorCode changeClassAluno(double nova_classificaçao, long nAluno) { return ErrorCode.E25;}

    @Override
    public ErrorCode changePossibilidadeAluno(long nALuno){ return ErrorCode.E25;}

    @Override
    public ErrorCode removeDocente(String numero_de_aluno){return ErrorCode.E25;}

    @Override
    public ErrorCode changeNameDocente(String novo_nome, String email) {return ErrorCode.E25;}

    @Override
    public ErrorCode addPropostaACandidatura(long nAluno, String idProposta) {return ErrorCode.E25;}

    @Override
    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {}

    @Override
    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() throws ConflitoAtribuicaoAutomaticaException {}

    @Override
    public boolean existConflict() {
        return false;
    }

    @Override
    public ErrorCode resolveConflito(long numAluno) {
        return ErrorCode.E25;
    }

    @Override
    public void associacaoAutomaticaDeDocentesAPropostas() {}

    @Override
    public boolean gerirOrientadores() {
        return false;
    }

    @Override
    public void gestaoManualAtribuicoes() {}

    @Override
    public ErrorCode exportarCSV(String file) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode removeProposta(String id) {return ErrorCode.E25;}

    @Override
    public ErrorCode removePropostaACandidatura(String id, long naluno) {
        return ErrorCode.E25;
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
    public String getPropostasDisponiveisToString() {
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

    @Override
    public boolean removeAll() {
        return false;
    }

    @Override
    public ErrorCode changeTitulo(String id, String novo_titulo) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode changeEntidade(String id, String nova_entidade){return ErrorCode.E25;}

    @Override
    public ErrorCode addRamo(String id, String ramo){return ErrorCode.E25;}

    @Override
    public ErrorCode removeRamo(String id, String ramo){return ErrorCode.E25;}

    @Override
    public ErrorCode insereAluno(Aluno a) {
        return ErrorCode.E25;
    }

    public ErrorCode editAluno(String email, String nome, Long nAluno, String curso, String ramo, Double classificacao, Boolean isPossible){
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode insereDocente(String email, String nome) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode insereProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode editProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode removeCandidatura(long numAluno) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode insereCandidatura(String nAluno, List<String> ids) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode editCandidatura(String text, List<String> ids) {
        return ErrorCode.E25;
    }

    @Override
    public ErrorCode editDocente(String email, String nome) {return ErrorCode.E25;}

    @Override
    public List<Aluno> getAlunosComAutoProposta() {
        return null;
    }

    @Override
    public List<Aluno> getAlunosComCandidatura() {
        return null;
    }

    @Override
    public List<Aluno> getAlunosSemCandidatura() {
        return null;
    }

    @Override
    public List<Proposta> getPropostasWithFiltersCopia(int ...opcoes) {
        return null;
    }

    @Override
    public List<Aluno> getAlunosComPropostaConfirmada() {
        return null;
    }

    @Override
    public List<Aluno> getAlunosSemPropostaConfirmada() {
        return null;
    }

    @Override
    public Map<Proposta, ArrayList<Aluno>> getConflito() {
        return null;
    }

    @Override
    public List<Proposta> getPropostasComOrientador() {
        return null;
    }

    @Override
    public List<Aluno> getAlunosComPropostaConfirmadaEOrientador() {
        return null;
    }

    @Override
    public List<Aluno> getAlunosComPropostaConfirmadaESemOrientador() {
        return null;
    }

    @Override
    public Map<String, Integer> getPropostasPorRamos() {
        return null;
    }

    @Override
    public List<Double> getDadosAtribuicoes() {
        return null;
    }

    @Override
    public Map<String, Integer> getTop5Empresas() {
        return null;
    }

    @Override
    public Map<Docente, Integer> getTop5Orientadores() {
        return null;
    }

    @Override
    public Map<Docente, Integer> getDocentesPorOrientacoes() {
        return null;
    }

    @Override
    public Map<String, Number> getDadosNumeroOrientacoes() {
        return null;
    }

    @Override
    public List<Aluno> getAlunosComPropostaConfirmadaEditavel() {
        return null;
    }

    @Override
    public void goLoad() {
        changeState(EnumState.LOAD_STATE);
    }
    @Override
    public List<Aluno> getTodosAlunosComPropostaAtribuidaCopia(){
        return null;
    }
    @Override
    public List<Aluno> obtencaoAlunosSemPropostaAtribuida(){
        return  null;
    }
    public List<Proposta> getPropostasDisponiveis(){
        return  null;
    }
    public List<Proposta> getPropostasAtribuidas(){
        return null;
    }
}
