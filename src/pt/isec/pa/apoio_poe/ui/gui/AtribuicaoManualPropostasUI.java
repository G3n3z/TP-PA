package pt.isec.pa.apoio_poe.ui.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;
import pt.isec.pa.apoio_poe.ui.resource.CSSManager;

public class AtribuicaoManualPropostasUI extends BorderPane {
    ModelManager model;
    MenuVertical menu;
    ButtonMenu btnAtribuirProposta, btnRemoverAtribuicao, btnRemoverAllAtribuicao, btnVoltar;
    TableAlunoProposta tableAlunoProposta;
    VBox vBoxTableAlunoProposta, center;
    HBox boxInput;
    TextField tfProposta, tfAluno;
    Button btnInsere, btnRemove;
    public AtribuicaoManualPropostasUI(ModelManager modelManager) {
        this.model = modelManager;
        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        preparaMenu();
        preparaTabela();
        preparaInput();
        center = new VBox(tableAlunoProposta, boxInput);
        setCenter(center);
    }

    private void preparaInput() {
        Label lProposta = new Label("ID Proposta");
        Label lAluno = new Label("Num. Aluno");
        tfAluno = new TextField();
        tfProposta = new TextField();
        btnInsere = new Button("Insere Atribuição");
        btnRemove = new Button("Remove Atribuição");

        VBox boxInput = new VBox(lProposta, tfProposta, lAluno, tfAluno, btnInsere);
        VBox.setMargin(lProposta, new Insets(50,0,20,0));
        VBox.setMargin(lAluno, new Insets(30,0,20,0));
        VBox.setMargin(tfAluno, new Insets(0,0,30,0));
        this.boxInput = new HBox(boxInput);
        this.boxInput.setAlignment(Pos.CENTER);


    }

    private void preparaTabela() {
        tableAlunoProposta = new TableAlunoProposta(model, EnumState.ATRIBUICAO_MANUAL_PROPOSTAS);
        TableColumn<Aluno, Button> colRemover = new TableColumn<>("Remover");
        colRemover.setCellValueFactory(alunoButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                String id = alunoButtonCellDataFeatures.getValue().getProposta().getId();
                Long nAluno = alunoButtonCellDataFeatures.getValue().getNumeroAluno();
                model.removeAtribuicao(id, nAluno);
            });
            remover.setId("button_delete");
            CSSManager.applyCSS(remover,"buttonDelete.css");
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colRemover.setPrefWidth(120);
        tableAlunoProposta.addColButton(colRemover);
        Label ltitle = new Label("Propostas Atribuidas");
        ltitle.setFont(new Font(18));
        HBox title = new HBox(ltitle);
        title.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(30,0,20,0));
        vBoxTableAlunoProposta = new VBox(title,tableAlunoProposta);
    }

    private void preparaMenu() {
        btnAtribuirProposta = new ButtonMenu("Atribuir Proposta");
        btnRemoverAtribuicao = new ButtonMenu("Remover Manual");
        btnRemoverAllAtribuicao = new ButtonMenu("Remover Todas");
        btnVoltar = new ButtonMenu("Voltar");
        menu = new MenuVertical(btnAtribuirProposta,btnRemoverAtribuicao,btnRemoverAllAtribuicao, btnVoltar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {update();});
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS , evt -> atualizaTabela());
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS , evt -> atualizaTabela());

        btnVoltar.setOnAction(actionEvent -> {
            model.recuarFase();
        });
        btnRemoverAllAtribuicao.setOnAction(actionEvent -> {
            ErrorCode e = model.removeAllAtribuicoes();
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas na Remoção das Atribuições", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }

        });
        btnRemoverAtribuicao.setOnAction(actionEvent -> {
            if(tfProposta.getText() == null || tfProposta.getText().equals("")){
                AlertSingleton.getInstanceWarning().setAlertText("", "ID Proposta Por Preencher", "");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            if(tfAluno.getText() == null || tfAluno.getText().equals("")){
                AlertSingleton.getInstanceWarning().setAlertText("", "Numero de Aluno Por Preencher", "");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            long nAluno;
            try{
                nAluno = Long.parseLong(tfAluno.getText());
            } catch (NumberFormatException e){
                AlertSingleton.getInstanceWarning().setAlertText("", "Número de aluno tem de ser um número inteiro", "");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            ErrorCode e = model.removeAtribuicao(tfProposta.getText(),nAluno);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Erro na Remocao Da Atribuição", "");
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnInsere.setOnAction(actionEvent -> {
            if(tfProposta.getText() == null || tfProposta.getText().equals("")){
                AlertSingleton.getInstanceWarning().setAlertText("", "ID Proposta Por Preencher", "");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            if(tfAluno.getText() == null || tfAluno.getText().equals("")){
                AlertSingleton.getInstanceWarning().setAlertText("", "Numero de Aluno Por Preencher", "");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            long nAluno;
            try{
                nAluno = Long.parseLong(tfAluno.getText());
            } catch (NumberFormatException e){
                AlertSingleton.getInstanceWarning().setAlertText("", "Número de aluno tem de ser um número inteiro", "");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            ErrorCode e = model.adicionaAtribuicao(tfProposta.getText(),nAluno);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Problema na Atribuição", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.ATRIBUICAO_MANUAL_PROPOSTAS);
        if (model != null && model.getState() == EnumState.ATRIBUICAO_MANUAL_PROPOSTAS){
            atualizaTabela();
        }
    }

    private void atualizaTabela(){
        if(model.getState() == EnumState.ATRIBUICAO_MANUAL_PROPOSTAS) {
            tableAlunoProposta.getItems().clear();
            tableAlunoProposta.getItems().addAll(model.getAlunosComPropostaConfirmadaEditavel());
        }
    }
}
