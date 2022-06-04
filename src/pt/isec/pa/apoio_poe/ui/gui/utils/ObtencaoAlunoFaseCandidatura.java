package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;

import java.util.List;

public class ObtencaoAlunoFaseCandidatura extends VBox {

    ModelManager model;
    TableAlunos tableComAutoproposta;
    TableAlunos tableComCandidatura;
    TableAlunos tableSemCandidatura;
    public ObtencaoAlunoFaseCandidatura(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }
    private void createViews() {
        prepararTabelaComAutoproposta();
        prepararTabelaComCandidatura();
        prepararTabelaSemCandidatura();
    }
    private String getAlunoPropostaString(Aluno a ){
        StringBuilder sb = new StringBuilder();
        List<String> id = a.getCandidatura().getIdProposta();
        for (int i = 0; i < id.size(); i++) {
            sb.append(id.get(i));
            if(i < id.size()-1){
                sb.append(",");
            }
        }
        if(id.size() == 0){
            return "n/a";
        }
        return sb.toString();
    }
    private void prepararTabelaComAutoproposta() {
        tableComAutoproposta = new TableAlunos(model,null);

        TableColumn<Aluno, String> colPropostas = new TableColumn<>("Propostas");


    }

    private void prepararTabelaComCandidatura() {

    }
    private void prepararTabelaSemCandidatura() {

    }


    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_CANDIDATURAS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {update();});
    }

    private void updateTabelas() {
    }

    private void update() {

    }


}
