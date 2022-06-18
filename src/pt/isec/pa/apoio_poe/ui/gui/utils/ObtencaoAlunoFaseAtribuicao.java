package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.tables.TableAlunos;

import java.util.List;

public class ObtencaoAlunoFaseAtribuicao extends VBox{
    ModelManager model;
    TableAlunos tableComAutoproposta;
    TableAlunos tableComCandidatura;
    TableAlunos tableAlunoComProposta;
    TableAlunos tableAlunoSemProposta;
    public ObtencaoAlunoFaseAtribuicao(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        Label lAutoPropostas = new Label("Alunos com AutoPropostas");
        lAutoPropostas.setFont(new Font(18));
        Label lPropostaCandidatura = new Label("Alunos com Propostas com Candidatura");
        lPropostaCandidatura.setFont(new Font(18));
        Label lAlunoComProposta = new Label("Alunos com Propostas Atribuidas");
        lAlunoComProposta.setFont(new Font(18));
        Label lAlunoSemProposta = new Label("Alunos sem Propostas Atribuidas");
        lAlunoSemProposta.setFont(new Font(18));
        VBox.setMargin(lAutoPropostas, new Insets(20,0,20,0));
        VBox.setMargin(lPropostaCandidatura, new Insets(20,0,20,0));
        VBox.setMargin(lAlunoComProposta, new Insets(20,0,20,0));
        VBox.setMargin(lAlunoSemProposta, new Insets(20,0,20,0));
        getChildren().addAll(lAutoPropostas,tableComAutoproposta, lPropostaCandidatura,tableComCandidatura, lAlunoComProposta,tableAlunoComProposta, lAlunoSemProposta, tableAlunoSemProposta);
    }
    private void createViews() {
        prepararTabelaComAutoproposta();
        prepararTabelaComCandidatura();
        preparaTabelaComProposta();
        preparaTabelaSemProposta();
    }

    private void preparaTabelaSemProposta() {
        tableAlunoSemProposta = new TableAlunos(model);
    }

    private void preparaTabelaComProposta() {
        tableAlunoComProposta = new TableAlunos(model);
        TableColumn<Aluno, String> colPropostas = new TableColumn<>("Propostas");
        colPropostas.setCellValueFactory(alunoStringCellDataFeatures -> new ReadOnlyObjectWrapper<>(getAlunoPropostaString(alunoStringCellDataFeatures.getValue())));
        tableAlunoComProposta.addCols(colPropostas);
        TableColumn<Aluno, Integer> colOrdem = new TableColumn<>("Ordem");
        colOrdem.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(!alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>(-1);
            }
            Integer ordem = alunoStringCellDataFeatures.getValue().getOrdem();
            return new ReadOnlyObjectWrapper<>(ordem);
        });
        tableAlunoComProposta.addCols(colOrdem);
    }

    private String getAlunoPropostaString(Aluno value) {
        if(!value.temPropostaConfirmada()){
            return "";
        }
        return value.getProposta().getId();
    }

    private String getAlunoPropostaCandidaturaString(Aluno a ){
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
        TableColumn<Aluno, String> colPropostas = new TableColumn<>("Propostas");
        colPropostas.setCellValueFactory(alunoStringCellDataFeatures -> new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getPropostaNaoConfirmada().getId()));
        tableComAutoproposta.addCols(colPropostas);
    }

    private void prepararTabelaComCandidatura() {
        tableComCandidatura = new TableAlunos(model);
        TableColumn<Aluno, String> colPropostas = new TableColumn<>("Propostas");
        colPropostas.setCellValueFactory(alunoStringCellDataFeatures -> new ReadOnlyObjectWrapper<>(getAlunoPropostaCandidaturaString(alunoStringCellDataFeatures.getValue())));
        tableComCandidatura.addCols(colPropostas);
    }



    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_CANDIDATURAS, evt -> {updateTabelas();});
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {updateTabelas();});

    }

    public void updateTabelas() {
        if(model.getState() == EnumState.ATRIBUICAO_PROPOSTAS) {
            tableComAutoproposta.getItems().clear();
            tableComAutoproposta.getItems().addAll(model.getAlunosComAutoProposta());
            tableComCandidatura.getItems().clear();
            tableComCandidatura.getItems().addAll(model.getAlunosComCandidatura());
            tableAlunoComProposta.getItems().clear();
            tableAlunoComProposta.getItems().addAll(model.getAlunosComPropostaConfirmada());
            tableAlunoSemProposta.getItems().clear();
            tableAlunoSemProposta.getItems().addAll(model.getAlunosSemPropostaConfirmada());
        }
    }

}
