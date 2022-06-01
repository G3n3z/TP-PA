package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

public class ModelManager {

    ApoioContext context;
    private ApoioManager manager;
    PropertyChangeSupport pcs;
    public static final String PROP_STATE = "state";
    public static final String PROP_ALUNOS = "alunos";
    public static final String PROP_DOCENTES = "docentes";
    public static final String PROP_PROPOSTAS = "propostas";
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
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
        pcs.firePropertyChange(PROP_ALUNOS, null, context.getState());
        pcs.firePropertyChange(PROP_DOCENTES, null, context.getState());
    }

    public List<Aluno> getAlunos() {
        return context.getAlunos();
    }

    public ErrorCode insereAluno(Aluno a) {
        ErrorCode erro = context.insereAluno(a);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
        return erro;
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
    }

    public ErrorCode exportCSV(String absolutePath) {
        return context.exportaCSV(absolutePath);
    }

    public void removeAluno(long numeroAluno) {
        context.removeAluno(numeroAluno);
        pcs.firePropertyChange(PROP_ALUNOS, null, null);
    }

    public List<Docente> getDocentes() {
        return context.getDocentes();
    }

    public void removeAllDocentes() {
        context.removeAll();
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
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

    public ErrorCode insereDocente(Docente d) {
        ErrorCode e =  context.insereDocente(d);
        pcs.firePropertyChange(PROP_DOCENTES, null, null);
        return e;
    }

    public void gerirDocentes() {
        context.gerirDocentes();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }
}
