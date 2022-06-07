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
    public ModelManager() {
        this.context = new ApoioContext();
        this.pcs = new PropertyChangeSupport(this);
        manager = context.getManager();
    }

    public boolean existsFileBin() {
        return context.existsFileBin();
    }

    public EnumState getState() {
        return context.getState();
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener){
        pcs.addPropertyChangeListener(property, listener);
    }

    public boolean avancarFase(){
        context.avancarFase();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
        return true;
    }

    public void begin() {
        context.begin();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }

    public void gerirAlunos() {
        context.gerirAlunos();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }

    public void recuarFase() {
        context.recuarFase();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());

    }

    public void load() throws IOException, ClassNotFoundException {
        context.load();
        manager = context.getManager();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
        pcs.firePropertyChange(PROP_ALUNOS, null, context.getState());
        pcs.firePropertyChange(PROP_DOCENTES, null, context.getState());
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
    }

    public List<Aluno> getAlunos() {
        return context.getAlunos();
    }

    public ErrorCode insereAluno(Aluno a) {
        ErrorCode erro = context.insereAluno(a);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        return erro;
    }

    public ErrorCode editAluno(String email, String nome, Long nAluno, String curso, String ramo, Double classificacao, Boolean isPossible) {
        ErrorCode error = context.editAluno(email,nome, nAluno,curso,ramo,classificacao,isPossible);
        if(error != ErrorCode.E0){
            return error;
        }
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        return error;
    }

    public void importAlunos(String absolutePath) throws CollectionBaseException {
        try{
            context.addAluno(absolutePath);
        }catch (CollectionBaseException e){
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_ALUNOS, null, null);
        }
    }

    public void removeAllAlunos() {
        System.out.println(context.removeAll());
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, context.getState());
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }

    public ErrorCode exportCSV(String absolutePath) {
        return context.exportaCSV(absolutePath);
    }

    public void removeAluno(long numeroAluno) {
        context.removeAluno(numeroAluno);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }

    public List<Docente> getDocentes() {
        return context.getDocentes();
    }

    public void removeAllDocentes() {
        context.removeAll();
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }

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

    public ErrorCode insereDocente(String email, String nome) {
        ErrorCode e =  context.insereDocente(email, nome);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        return e;
    }

    public void removeDocente(String email) {
        context.removeDocente(email);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }

    public ErrorCode editDocente(String email, String nome) {
        ErrorCode error = context.editDocente(email, nome);
        if(error == ErrorCode.E0){
            pcs.firePropertyChange(PROP_DOCENTES,null ,null);
        }
        return error;
    }

    public void gerirDocentes() {
        context.gerirDocentes();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }

    public ErrorCode removeProposta(String id) {
        ErrorCode e = context.removerProposta(id);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        return e;
    }

    public List<Proposta> getPropostas() {
        return context.getPropostas();
    }

    public void gerirPropostas() {
        context.gerirEstagios();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }

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

    public ErrorCode insereProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        ErrorCode e =  context.insereProposta(tipo,  id, ramos, titulo, docente, entidade, nAluno);
        if(e == ErrorCode.E0){
            pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        }
        return e;
    }

    public ErrorCode editProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        ErrorCode e = context.editProposta(tipo,  id, ramos, titulo, docente, entidade, nAluno);
        if(e == ErrorCode.E0){
            pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        }
        return e;
    }

    public ErrorCode fecharFase() {
        ErrorCode e = context.closeFase();
        if(e== ErrorCode.E0){
            pcs.firePropertyChange(PROP_STATE, null, null);
        }
        return e;
    }

    public boolean isClosed() {
        return context.isClosed();
    }

    public ErrorCode removeCandidatura(TableColumn.CellDataFeatures<Candidatura, Button> candidaturaLongCellDataFeatures) {

        ErrorCode e =  context.removeCandidatura(candidaturaLongCellDataFeatures.getValue().getNumAluno());
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        return e;
    }

    public List<Candidatura> getCandidaturas() {
        return context.getCandidaturasList();

    }

    public ErrorCode insereCandidatura(String nAluno, List<String> ids) {
        ErrorCode e =  context.insereCandidatura(nAluno, ids);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        return e;
    }

    public boolean importCandidaturasCSV(String absolutePath) throws CollectionBaseException {
        boolean b;
        try {
            context.addCandidatura(absolutePath);
        } catch (CollectionBaseException e) {
            throw e;
        }finally {
            pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        }
        return true;
    }

    public ErrorCode editCandidatura(String text, List<String> ids) {
        ErrorCode e = context.editCandidatura(text, ids);
        if(e == ErrorCode.E0){
            pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
        }
        return e;
    }

    public void removeAllCandidatura() {
        context.removeAll();
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);

    }

    public List<Aluno> getAlunosComAutoProposta() {
        return context.getAlunosComAutoProposta();
    }

    public List<Aluno> getAlunosComCandidatura() {
        return context.getAlunosComCandidatura();
    }

    public List<Aluno> getAlunosSemCandidatura() {
        return context.getAlunosSemCandidatura();
    }

    public List<Proposta> getPropostasWithFilters(int ...filtros) {
        return context.getPropostasWithFilters(filtros);
    }

    public void gestaoManualAtribuicoes() {
        context.gestaoManualAtribuicoes();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }

    public List<Aluno> getAlunosComPropostaConfirmada() {
       return context.getAlunosComPropostaConfirmada();
    }

    public void atribuicoesAutomaticas() {
        context.atribuicaoAutomatica();
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
    }

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

    public void conflitoAtribuicaoCandidatura() {
        context.conflitoAtribuicaoCandidatura();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }

    public List<Aluno> getAlunosSemPropostaConfirmada() {
        return context.getAlunosSemPropostaConfirmada();
    }

    public ErrorCode removeAllAtribuicoes() {
        ErrorCode e = manager.removerTodasAtribuicoes();
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }

    public ErrorCode removeAtribuicao(String id, Long nAluno) {
        ErrorCode e = manager.remocaoManual(nAluno, id);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }

    public ErrorCode adicionaAtribuicao(String id, Long nAluno) {
        ErrorCode e = manager.atribuicaoManual(nAluno, id);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }

    public Map<Proposta, ArrayList<Aluno>> getConflito() {
        return context.getConflito();

    }

    public ErrorCode resolveConflito(long numeroAluno) {
        return context.resolveConflito(numeroAluno);
    }

    public void conflitoResolvido() {
        pcs.firePropertyChange(PROP_RESOLVIDO, null, null);
    }

    public List<Long> getStatsAlunos(){
        return context.getStatsAlunos();
    }

    public Double getMediaClassificacao() {
        return context.getMediaClassificacao();
    }

    public List<Long> getStatsPropostas() {
        return context.getStatsPropostas();
    }

    public List<Proposta> getPropostasComOrientador() {
        return context.getPropostasComOrientador();
    }

    public void associacaoAutomaticaDeDocentesAPropostas() {
        context.associacaoAutomaticaDeDocentesAPropostas();
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
    }


    public List<Aluno> getAlunosComPropostaConfirmadaEOrientador() {
        return context.getAlunosComPropostaConfirmadaEOrientador();
    }

    public List<Aluno> getAlunosComPropostaConfirmadaESemOrientador() {
        return context.getAlunosComPropostaConfirmadaESemOrientador();
    }

    public ErrorCode removerAtribuicaoOrientador(String email, String id) {
        ErrorCode e =  manager.removerDocente(email,id);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }

    public ErrorCode insereOrientador(String email, String idProposta) {
        ErrorCode e = manager.atribuirOrientador(email, idProposta);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }

    public ErrorCode editOrientador(String email, String idProposta) {
        ErrorCode e = manager.alterarDocente(email, idProposta);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_UNDO, null, null);
        return e;
    }

    public void gerirOrientadores() {
        context.gerirOrientadores();
        pcs.firePropertyChange(PROP_STATE, null, null);
    }

    public boolean getCloseState(EnumState state) {
        return context.getBooleanState(state);
    }

    public void sair() {
        context.sair();
    }

    public void save() throws IOException {
        context.save();
    }

    public void redo() {
        manager.redo();
        pcs.firePropertyChange(PROP_UNDO, null, null);
        pcs.firePropertyChange(PROP_ALUNOS, null,null);
        pcs.firePropertyChange(PROP_DOCENTES, null,null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
    }
    public void undo() {
        manager.undo();
        pcs.firePropertyChange(PROP_REDO, null, null);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        pcs.firePropertyChange(PROP_PROPOSTAS, null, null);
        pcs.firePropertyChange(PROP_CANDIDATURAS, null, null);
    }

    public boolean hasUndo() {
        return manager.hasUndo();
    }

    public boolean hasRedo() {
        return manager.hasRedo();
    }

    public Map<String, Integer> getPropostasPorRamos() {
        return context.getPropostasPorRamos();
    }

    public List<Double> getDadosAtribuicoes() {
        return context.getDadosAtribuicoes();
    }
}
