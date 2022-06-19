package pt.isec.pa.apoio_poe.model;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.*;

public class ModelManager {

    ApoioContext context;
    private ApoioManager manager;
    PropertyChangeSupport pcs;
    public static final String PROP_STATE = "state";
    public static final String PROP_ALUNOS = "alunos";
    public static final String PROP_DOCENTES = "docentes";
    public static final String PROP_PROPOSTAS = "propostas";
    public static final String PROP_CANDIDATURAS = "candidaturas";
    public static final String PROP_RESOLVIDO = "resolvido";
    public static final String PROP_UNDO = "undo";
    public static final String PROP_REDO = "redo";
    public static final String PROP_CLOSE_STATE = "closeState";
    public ModelManager() {
        this.context = new ApoioContext();
        this.pcs = new PropertyChangeSupport(this);
        manager = context.getManager();
    }

    /**
     * Verifica se existe um ficheiro gyardado para carregar
     * @return true se existir ficheiro
     */
    public boolean existsFileBin() {
        return context.existsFileBin();
    }

    /**
     * Obtém o estado em que se encontra a aplicação
     * @return EnumState com o estado atual
     */
    public EnumState getState() {
        return context.getState();
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener){
        pcs.addPropertyChangeListener(property, listener);
    }
    /**
     * Executa o avançar de fase
     *
     */
    public boolean avancarFase(){
        context.avancarFase();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
        return true;
    }
    /**
     * Inicia a aplicação sem carregar ficheiro
     * transita para as Opções de Configuração
     */
    public void begin() {
        context.begin();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }
    /**
     * Transita a visualização para a Gestão de Alunos
     *
     */
    public void gerirAlunos() {
        context.gerirAlunos();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }
    /**
     * Executa o recuar de fase
     *
     */
    public void recuarFase() {
        context.recuarFase();
        manager.clearUndoAndRedo();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());

    }
    /**
     * Carrega o último estado da aplicação gravado em ficheiro
     *
     */
    public void load() throws IOException, ClassNotFoundException {
        context.load();
        manager = context.getManager();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
        pcs.firePropertyChange(PROP_ALUNOS, null, context.getState());
        pcs.firePropertyChange(PROP_DOCENTES, null, context.getState());
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        pcs.firePropertyChange(PROP_CLOSE_STATE, null, null);
    }
    /**
     * Obtém uma cópia da lista de alunos
     * @return List com os alunos
     */
    public List<Aluno> getAlunos() {
        return context.getAlunos();
    }
    /**
     * Insere um aluno
     * @param a Aluno a inserir
     * @return ErrorCode com o resultado da inserção
     */
    public ErrorCode insereAluno(Aluno a) {
        ErrorCode erro = context.insereAluno(a);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        return erro;
    }
    /**
     * Edita um aluno já existente
     * @param email email do Aluno
     * @param nome nome do Aluno
     * @param nAluno número do Aluno
     * @param curso curso do Aluno
     * @param ramo ramo do Aluno, deverá ser um inteiro
     * @param classificacao calssificação do Aluno compreendida entre 0.0 e 1.0
     * @param isPossible possibilidade do Aluno frequentar estágio, true se possível
     * @return ErrorCode com o resultado da edição
     */
    public ErrorCode editAluno(String email, String nome, Long nAluno, String curso, String ramo, Double classificacao, Boolean isPossible) {
        ErrorCode error = context.editAluno(email,nome, nAluno,curso,ramo,classificacao,isPossible);
        if(error != ErrorCode.E0){
            return error;
        }
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        return error;
    }
    /**
     * Importar informação dos alunos de ficheiro CSV
     * @param absolutePath diretoria absoluta onde se encontra o ficheiro
     *
     */
    public boolean importAlunos(String absolutePath) throws CollectionBaseException {
        boolean b;
        try{
            b = context.addAluno(absolutePath);
        }catch (CollectionBaseException e){
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_ALUNOS, null, null);
        }
        return b;
    }
    /**
     * Remove todos os alunos
     *
     */
    public void removeAllAlunos() {
        context.removeAll();
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, context.getState());
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }
    /**
     * Exporta informação dos alunos para ficheiro CSV
     * @param absolutePath diretoria absoluta onde se vai gravar o ficheiro
     *
     */
    public ErrorCode exportCSV(String absolutePath) {
        return context.exportaCSV(absolutePath);
    }

    /**
     * Remove um aluno
     * @param numeroAluno número do aluno a remover
     *
     */
    public void removeAluno(long numeroAluno) {
        context.removeAluno(numeroAluno);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }
    /**
     * Obtém uma cópia da lista de docentes
     * @return List com os docentes
     */
    public List<Docente> getDocentes() {
        return context.getDocentes();
    }
    /**
     * Remove todos os docentes
     *
     */
    public void removeAllDocentes() {
        context.removeAll();
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }
    /**
     * Importar informação dos docentes de ficheiro CSV
     * @param absolutePath diretoria absoluta onde se encontra o ficheiro
     *
     */
    public boolean importDocentes(String absolutePath) throws CollectionBaseException {
        boolean b;
        try{
            b = context.importDocentes(absolutePath);
        }catch (CollectionBaseException e){
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_DOCENTES, null, null);
        }
        return b;
    }
    /**
     * Insere um docente
     * @param email email do docente a inserir
     * @param nome nome do docente a inserir
     * @return ErrorCode com o resultado da inserção
     */
    public ErrorCode insereDocente(String email, String nome) {
        ErrorCode e =  context.insereDocente(email, nome);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        return e;
    }
    /**
     * Remove um docente
     * @param email email do docente a remover
     *
     */
    public void removeDocente(String email) {
        context.removeDocente(email);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }
    /**
     * Edita um docente já existente
     * @param email email do docente
     * @param nome nome do docente
     * @return ErrorCode com o resultado da edição
     */
    public ErrorCode editDocente(String email, String nome) {
        ErrorCode error = context.editDocente(email, nome);
        if(error == ErrorCode.E0){
            pcs.firePropertyChange(PROP_DOCENTES,null ,null);
        }
        return error;
    }
    /**
     * Transita a visualização para a Gestão de Docentes
     *
     */
    public void gerirDocentes() {
        context.gerirDocentes();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }
    /**
     * Remove uma proposta
     * @param id id da proposta a remover
     * @return ErrorCode com o resultado da remoção
     */
    public ErrorCode removeProposta(String id) {
        ErrorCode e = context.removerProposta(id);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        return e;
    }
    /**
     * Obtém uma cópia da lista de porpostas
     * @return List com as propostas
     */
    public List<Proposta> getPropostas() {
        return context.getPropostas();
    }
    /**
     * Transita a visualização para a Gestão de Propostas
     *
     */
    public void gerirPropostas() {
        context.gerirEstagios();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }
    /**
     * Importar informação das propostas de ficheiro CSV
     * @param absolutePath diretoria absoluta onde se encontra o ficheiro
     *
     */
    public boolean importCSV(String absolutePath) throws CollectionBaseException {
        boolean result;
        try {
            result =  context.importPropostas(absolutePath);
        } catch (CollectionBaseException e) {
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_PROPOSTAS, null, context.getState());
        }
        return result;
    }
    /**
     * Insere uma proposta
     * @param tipo tipo da proposta a inserir
     * @param id id da proposta a inserir
     * @param ramos lista dos ramos da proposta a inserir
     * @param titulo titulo da proposta a inserir
     * @param docente email do docente preponente caso exista
     * @param entidade entidade da proposta a inserir
     * @param nAluno número do aluno pré-atribuído caso exista
     * @return ErrorCode com o resultado da inserção
     *
     */
    public ErrorCode insereProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        ErrorCode e =  context.insereProposta(tipo,  id, ramos, titulo, docente, entidade, nAluno);
        if(e == ErrorCode.E0){
            pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        }
        return e;
    }
    /**
     * Edita uma proposta existente
     * @param tipo tipo da proposta a editar
     * @param id id da proposta a editar
     * @param ramos lista dos ramos da proposta a editar
     * @param titulo titulo da proposta a editar
     * @param docente email do docente preponente caso exista
     * @param entidade entidade da proposta a editar
     * @param nAluno número do aluno pré-atribuído caso exista
     * @return ErrorCode com o resultado da edição
     *
     */
    public ErrorCode editProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        ErrorCode e = context.editProposta(tipo,  id, ramos, titulo, docente, entidade, nAluno);
        if(e == ErrorCode.E0){
            pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        }
        return e;
    }
    /**
     * Executa o fecho da fase em que se encontra a aplicação
     * @return ErrorCode com o resultado do fecho
     *
     */
    public ErrorCode fecharFase() {
        ErrorCode e = context.closeFase();
        if(e== ErrorCode.E0){
            pcs.firePropertyChange(PROP_CLOSE_STATE, null, null);
            pcs.firePropertyChange(PROP_STATE, null, null);
        }
        return e;
    }
    /**
     * Verifica se a fase em que se encontra está fechada
     *
     */
    public boolean isClosed() {
        return context.isClosed();
    }
    /**
     * Remove a Candidatura da linha da tabela correspondente
     * @return ErrorCode com o resultado da remoção
     *
     */
    public ErrorCode removeCandidatura(TableColumn.CellDataFeatures<Candidatura, Button> candidaturaLongCellDataFeatures) {

        ErrorCode e =  context.removeCandidatura(candidaturaLongCellDataFeatures.getValue().getNumAluno());
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        return e;
    }
    /**
     * Obtém as candidaturas existentes
     * @return List com as candidaturas
     *
     */
    public List<Candidatura> getCandidaturas() {
        return context.getCandidaturasList();

    }
    /**
     * Insere uma Candidatura
     * @param nAluno número do aluno a que corresponde a candidatura
     * @param ids IDs das propostas às quais o aluno se candidata
     * @return ErrorCode com o resultado da inserção
     *
     */
    public ErrorCode insereCandidatura(String nAluno, List<String> ids) {
        ErrorCode e =  context.insereCandidatura(nAluno, ids);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        return e;
    }
    /**
     * Importar informação das candidaturas de ficheiro CSV
     * @param absolutePath diretoria absoluta onde se encontra o ficheiro
     *
     */
    public boolean importCandidaturasCSV(String absolutePath) throws CollectionBaseException {
        boolean b;
        try {
            b = context.addCandidatura(absolutePath);
        } catch (CollectionBaseException e) {
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        }
        return b;
    }
    /**
     * Edita uma Candidatura
     * @param ids IDs das propostas às quais o aluno se candidata
     * @return ErrorCode com o resultado da inserção
     *
     */
    public ErrorCode editCandidatura(String text, List<String> ids) {
        ErrorCode e = context.editCandidatura(text, ids);
        if(e == ErrorCode.E0){
            pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        }
        return e;
    }
    /**
     * Remove todas as candidaturas
     *
     */
    public void removeAllCandidatura() {
        context.removeAll();
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);

    }
    /**
     * Obtém os alunos com auto proposta
     * @return List com os alunos que têm auto proposta
     *
     */
    public List<Aluno> getAlunosComAutoProposta() {
        return context.getAlunosComAutoProposta();
    }
    /**
     * Obtém os alunos com candidatura efetuada
     * @return List com os alunos que têm candidatura
     *
     */
    public List<Aluno> getAlunosComCandidatura() {
        return context.getAlunosComCandidatura();
    }
    /**
     * Obtém os alunos sem candidatura efetuada
     * @return List com os alunos que não têm candidatura
     *
     */
    public List<Aluno> getAlunosSemCandidatura() {
        return context.getAlunosSemCandidatura();
    }
    /**
     * Obtém uma lista conoante os filtros escolhidos
     * @param filtros filtros escolhidos para obtenção das listas
     * @return List com os alunos que têm candidatura
     *
     */
    public List<Proposta> getPropostasWithFilters(int ...filtros) {
        return context.getPropostasWithFilters(filtros);
    }
    /**
     * Transita a visualização para a Atribuição Manual de Propostas
     *
     */
    public void gestaoManualAtribuicoes() {
        context.gestaoManualAtribuicoes();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }
    /**
     * Obtém os alunos com propostas já confirmadas
     * @return List de alunos com propostas já confirmadas
     *
     */
    public List<Aluno> getAlunosComPropostaConfirmada() {
       return context.getAlunosComPropostaConfirmada();
    }
    /**
     * Executa a atribuição de autopropostas aos alunos correspondentes
     *
     */
    public void atribuicoesAutomaticas() {
        context.atribuicaoAutomatica();
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }
    /**
     * Executa a atribuição automatica dos alunos que têm candidatura
     *
     */
    public void atribuicoesAutomaticasSemAtribuicaoDefinida() throws ConflitoAtribuicaoAutomaticaException {
        try {
            context.atribuicaoAutomaticaSemAtribuicoesDefinidas();
        } catch (ConflitoAtribuicaoAutomaticaException e) {
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_ALUNOS, null, null);
            pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        }
    }
    /**
     * Transita a visualização para o Conflito Atribuição Candidatura
     *
     */
    public void conflitoAtribuicaoCandidatura() {
        context.conflitoAtribuicaoCandidatura();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }
    /**
     * Obtém os alunos sem propostas confirmadas
     * @return List de alunos sem propostas confirmadas
     *
     */
    public List<Aluno> getAlunosSemPropostaConfirmada() {
        return context.getAlunosSemPropostaConfirmada();
    }
    /**
     * Remove todas as atribuições
     * @return ErrorCode com o resultado da remoção
     *
     */
    public ErrorCode removeAllAtribuicoes() {
        ErrorCode e = manager.removerTodasAtribuicoes();
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }
    /**
     * Remove a atribuição da proposta a um aluno
     * @param id ID da proposta atribuída
     * @param nAluno número do aluno
     * @return ErrorCode com o resultado da remoção
     *
     */
    public ErrorCode removeAtribuicao(String id, Long nAluno) {
        ErrorCode e = manager.remocaoManual(nAluno, id);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }
    /**
     * Adiciona a atribuição da proposta a um aluno
     * @param id ID da proposta a atribuir
     * @param nAluno número do aluno
     * @return ErrorCode com o resultado da remoção
     *
     */
    public ErrorCode adicionaAtribuicao(String id, Long nAluno) {
        ErrorCode e = manager.atribuicaoManual(nAluno, id);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }
    /**
     * Obtém os conflitos resultantes da atribuíção automática
     * @return Map com a Proposta que originou o conflito e um ArrayList dos alunos que se candidataram a proposta
     *
     */
    public Map<Proposta, ArrayList<Aluno>> getConflito() {
        return context.getConflito();

    }
    /**
     * Resolve o conflito atribuíndo a proposta a um aluno
     * @param numeroAluno número do aluno ao qual se quer atribuír a proposta
     * @return ErrorCode com o resultado da atribuição
     *
     */
    public ErrorCode resolveConflito(long numeroAluno) {
        return context.resolveConflito(numeroAluno);
    }

    public void conflitoResolvido() {
        pcs.firePropertyChange(PROP_RESOLVIDO, null, null);
    }
    /**
     * Obtém as statisticas relacionadas com os alunos
     * @return List contendo as diferentes estatísticas
     *
     */
    public List<Long> getStatsAlunos(){
        return context.getStatsAlunos();
    }
    /**
     * Obtém média da classificação dos alunos
     * @return Double média da classificação
     *
     */
    public Double getMediaClassificacao() {
        return context.getMediaClassificacao();
    }
    /**
     * Obtém as statisticas relacionadas com as propostas
     * @return List contendo as diferentes estatísticas
     *
     */
    public List<Long> getStatsPropostas() {
        return context.getStatsPropostas();
    }
    /**
     * Obtém as propostas que já possuem um orientador
     * @return List das propostas que já possuem um orientador
     *
     */
    public List<Proposta> getPropostasComOrientador() {
        return context.getPropostasComOrientador();
    }
    /**
     * Executa a atribuição automatica dos docentes proponentes como orientadores das propostas correspondentes
     *
     */
    public void associacaoAutomaticaDeDocentesAPropostas() {
        context.associacaoAutomaticaDeDocentesAPropostas();
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
    }

    /**
     * Obtém os alunos que já possuem proposta confirmada e orientador
     * @return List dos alunos que já possuem proposta confirmada e orientador
     *
     */
    public List<Aluno> getAlunosComPropostaConfirmadaEOrientador() {
        return context.getAlunosComPropostaConfirmadaEOrientador();
    }
    /**
     * Obtém os alunos que já possuem proposta confirmada e sem orientador
     * @return List dos alunos que já possuem proposta confirmada e sem orientador
     *
     */
    public List<Aluno> getAlunosComPropostaConfirmadaESemOrientador() {
        return context.getAlunosComPropostaConfirmadaESemOrientador();
    }
    /**
     * Remove uma atribuição de um Orientador a uma proposta
     * @param email email do docente a remover da atribuição
     * @param id id da proposta a remover da atribuição
     * @return ErroCode com o resultado da remoção
     *
     */
    public ErrorCode removerAtribuicaoOrientador(String email, String id) {
        ErrorCode e =  manager.removerDocente(email,id);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }
    /**
     * Insere uma atribuição de um Orientador a uma proposta
     * @param email email do docente a inserir a atribuição
     * @param idProposta id da proposta a inserir a atribuição
     * @return ErroCode com o resultado da inserção
     *
     */
    public ErrorCode insereOrientador(String email, String idProposta) {
        ErrorCode e = manager.atribuirOrientador(email, idProposta);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }
    /**
     * Edita uma atribuição de um Orientador a uma proposta
     * @param email email do docente a editar a atribuição
     * @param idProposta id da proposta a editar a atribuição
     * @return ErroCode com o resultado da edição
     *
     */
    public ErrorCode editOrientador(String email, String idProposta) {
        ErrorCode e = manager.alterarDocente(email, idProposta);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }
    /**
     * Transita a visualização para a Gestão de Orientadores
     *
     */
    public void gerirOrientadores() {
        context.gerirOrientadores();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }
    /**
     * Verifica se a fase enviada no argumento se encontra fechada
     * @param state enumeração do estado correspondente a verificar
     * @return boolean true se estiver fechado.
     */
    public boolean getCloseState(EnumState state) {
        return context.getBooleanState(state);
    }
    /**
     * Transita a aplicação para o estado de sair
     *
     */
    public void sair() {
        context.sair();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }
    /**
     * Grava o estado da aplicação
     *
     */
    public void save() throws IOException {
        context.save();
    }
    /**
     * Executa a ação de redo
     *
     */
    public void redo() {
        manager.redo();
        pcs.firePropertyChange(PROP_UNDO, null, null);
        pcs.firePropertyChange(PROP_ALUNOS, null,null);
        pcs.firePropertyChange(PROP_DOCENTES, null,null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
    }
    /**
     * Executa a ação de undo
     *
     */
    public void undo() {
        manager.undo();
        pcs.firePropertyChange(PROP_REDO, null, null);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
    }
    /**
     * Verifica a pssibilidade de fazer undo
     *
     */
    public boolean hasUndo() {
        return manager.hasUndo();
    }
    /**
     * Verifica a pssibilidade de fazer redo
     *
     */
    public boolean hasRedo() {
        return manager.hasRedo();
    }
    /**
     * Obtém a soma do número de propostas correspondentes de cada ramo
     * @return Map com o Ramo como chave e a soma das propostas correspondentes como valor
     *
     */
    public Map<String, Integer> getPropostasPorRamos() {
        return context.getPropostasPorRamos();
    }
    /**
     * Obtém as estatisticas das atibuições
     * @return List com os valores reais das atibuições
     *
     */
    public List<Double> getDadosAtribuicoes() {
        return context.getDadosAtribuicoes();
    }
    /**
     * Obtém as 5 entidades com mais propostas
     * @return Map com a entidade como chave e o número de propostas como valor
     *
     */
    public Map<String, Integer> getTop5Empresas() {
        return context.getTop5Empresas();
    }
    /**
     * Obtém os 5 docentes com mais Orientações atribuídas
     * @return Map com o Docente como chave e o número de propostas como valor
     *
     */
    public Map<Docente, Integer> getTop5Orientadores() {
        return context.getTop5Orientadores();
    }
    /**
     * Obtém o número de Orientações de cada Docente
     * @return Map com o Docente como chave e o número de propostas como valor
     *
     */
    public Map<Docente, Integer> getDocentesPorOrientacoes() {
        return context.getDocentesPorOrientacoes();
    }
    /**
     * Obtém número de orientações por docente, em média, mínimo e máximo
     * @return Map com as estatisticas média, mínimo e máximo como chave e o número de propostas como valor
     *
     */
    public Map<String, Number> getDadosNumeroOrientacoes() {
        return context.getDadosNumeroOrientacoes();
    }
    /**
     * Obtém os alunos aos quais a atribuiçao de propostas nao foi previamente definido
     * @return List com os alunos aos quais a atribuiçao de propostas nao foi previamente definido
     *
     */
    public List<Aluno> getAlunosComPropostaConfirmadaEditavel() {
        return context.getAlunosComPropostaConfirmadaEditavel();
    }
    /**
     * Transita a aplicação para o estado de load
     *
     */
    public void goLoad() {
        context.goLoad();
    }
}
