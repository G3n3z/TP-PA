package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

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
        Label lAutoPropostas = new Label("Alunos com Autopropostas");
        lAutoPropostas.setFont(new Font(18));
        Label lPropostaCandidatura = new Label("Alunos com Candidatura");
        lPropostaCandidatura.setFont(new Font(18));
        Label lPropostaSemCandidatura = new Label("Alunos sem Candidatura");
        lPropostaSemCandidatura.setFont(new Font(18));
        VBox.setMargin(lAutoPropostas, new Insets(20,0,20,20));
        VBox.setMargin(lPropostaCandidatura, new Insets(20,0,20,20));
        VBox.setMargin(lPropostaSemCandidatura, new Insets(20,0,20,20));
        getChildren().addAll(lAutoPropostas,tableComAutoproposta, lPropostaCandidatura,tableComCandidatura, lPropostaSemCandidatura,tableSemCandidatura);
    }
    private void createViews() {
        prepararTabelaComAutoproposta();
        prepararTabelaComCandidatura();
        prepararTabelaSemCandidatura();
    }
    private String getAlunoPropostaString(Aluno a ){
        StringBuilder sb = new StringBuilder();
        Candidatura c = a.getCandidatura();
        if(c == null){
            return "";
        }
        List<String> id = a.getCandidatura().getIdProposta();
        if(id == null || id.size() == 0){
            return "n/a";
        }
        for (int i = 0; i < id.size(); i++) {
            sb.append(id.get(i));
            if(i < id.size()-1){
                sb.append(",");
            }
        }

        return sb.toString();
    }
    private void prepararTabelaComAutoproposta() {
        tableComAutoproposta = new TableAlunos(model);
        tableComAutoproposta.removeCols("Editar", "Remover");
        TableColumn<Aluno, String> colPropostas = new TableColumn<>("Propostas");
        colPropostas.setCellValueFactory(alunoStringCellDataFeatures -> new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getPropostaNaoConfirmada().getId()));
        tableComAutoproposta.addCols(colPropostas);
    }

    private void prepararTabelaComCandidatura() {
        tableComCandidatura = new TableAlunos(model);
        tableComCandidatura.removeCols("Editar", "Remover");
        TableColumn<Aluno, String> colPropostas = new TableColumn<>("Propostas");
        colPropostas.setCellValueFactory(alunoStringCellDataFeatures -> new ReadOnlyObjectWrapper<>(getAlunoPropostaString(alunoStringCellDataFeatures.getValue())));
        tableComCandidatura.addCols(colPropostas);
    }
    private void prepararTabelaSemCandidatura() {
        tableSemCandidatura = new TableAlunos(model);
        tableSemCandidatura.removeCols("Editar", "Remover");
    }


    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_CANDIDATURAS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {updateTabelas();});
    }

    public void updateTabelas() {
        if(model.getState() == EnumState.OPCOES_CANDIDATURA) {
            tableComAutoproposta.getItems().clear();
            tableComAutoproposta.getItems().addAll(model.getAlunosComAutoProposta());
            tableComCandidatura.getItems().clear();
            tableComCandidatura.getItems().addAll(model.getAlunosComCandidatura());
            tableSemCandidatura.getItems().clear();
            tableSemCandidatura.getItems().addAll(model.getAlunosSemCandidatura());
        }
    }

    private void update() {

    }


}
