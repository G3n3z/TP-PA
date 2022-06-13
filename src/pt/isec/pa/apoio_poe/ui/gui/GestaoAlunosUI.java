package pt.isec.pa.apoio_poe.ui.gui;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;
import pt.isec.pa.apoio_poe.ui.resource.CSSManager;
import pt.isec.pa.apoio_poe.utils.Constantes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class GestaoAlunosUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoAlunos, btnInsereManual, btnExport, btnEdit, btnRemove, btnRemoveAll,btnRecuar;
    MenuVertical menu;
    Label title, numAlunos, numLEIPL, numLEI, numDA, numSI, numRAS, medClassificacao, numPossibilidade;;
    TableAlunos tableView;

    Aluno a;
    HBox hboxInsereAluno, hBtn, hBtnEdit;
    VBox hboxInsereCSV,boxRight;
    Button btnExeInsereAluno, btnExeInsereCSV, btnExeEditAluno, btnInsereCSV, btnCancelEdit;
    TextField txNumero, tfNome,tfEmail, tfClass;
    ChoiceBox<String> curso, ramo;
    CheckBox possibilidade;
    List<Node> nodeShow;
    FileChooser fileChooser;
    Long nAlunos, nPL, nLEI, nDA, nSI, nRAS, nPosssibilidade;
    Double mClassificacao;
    List<Long> stats = new ArrayList<>();
    Boolean isClosed = false;
    public GestaoAlunosUI(ModelManager model) {
        this.model = model;
        a = new Aluno("asd","Daniel", 123L,"LEI","DA",1.0,true);
        createViews();
        registerHandlers();
        update();

    }

    private void createViews() {
        createMenu();
        title = new Label("Gestão de Alunos");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        titulo.setPadding(new Insets(25,0,25,0));
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);
        BorderPane container = new BorderPane();
        preparaTable();
        preparaInsereAluno();
        preparaInsereCSV();

        nodeShow = new ArrayList<>();
        nodeShow.add(hboxInsereAluno);
        nodeShow.add(hboxInsereCSV);
        hboxInsereAluno.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));

        nodeShow.forEach(n -> n.setVisible(false));

        numAlunos = new Label();
        numLEIPL = new Label();
        numLEI = new Label();
        numDA = new Label();
        numSI = new Label();
        numRAS = new Label();
        medClassificacao = new Label();
        numPossibilidade = new Label();

        HBox statsFooter = new HBox();
        statsFooter.getChildren().addAll(numAlunos, numLEIPL, numLEI, numDA, numSI, numRAS, medClassificacao, numPossibilidade);
        statsFooter.setAlignment(Pos.CENTER);
        statsFooter.setSpacing(20.0);
        statsFooter.setPadding(new Insets(25));
        //statsFooter.setPrefHeight(100);
        statsFooter.setBackground(new Background(new BackgroundFill(Color.web("#37304a"),CornerRadii.EMPTY,Insets.EMPTY)));

        //tableView.setMinHeight(400);
        //hboxInsereAluno.setPrefHeight(100);
        hboxInsereAluno.setPadding(new Insets(10,0,10,0));
        VBox centro = new VBox(tableView, hboxInsereAluno, hboxInsereCSV);
        container.setTop(titulo);
        container.setCenter(centro);
        container.setBottom(statsFooter);
        container.autosize();
        setCenter(container);

    }

    private void formatLabelFooter(Label label){
        label.setFont(new Font(14));
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold");
    }

    private void atualizaStats(){
        stats = model.getStatsAlunos();
        nAlunos = stats.get(0);
        numAlunos.setText("Alunos: "+nAlunos);
        nPL = stats.get(1);
        numLEIPL.setText("LEI-PL: "+nPL);
        nLEI = stats.get(2);
        numLEI.setText("LEI: "+nLEI);
        nDA = stats.get(3);
        numDA.setText("DA: "+nDA);
        nSI = stats.get(4);
        numSI.setText("SI: "+nSI);
        nRAS = stats.get(5);
        numRAS.setText("RAS: "+nRAS);
        nPosssibilidade = stats.get(6);
        numPossibilidade.setText("C/ Possibilidade de estagio : "+nPosssibilidade);
        mClassificacao = model.getMediaClassificacao();
        medClassificacao.setText("Classificacao media : "+String.format("%.4f",mClassificacao));
        formatLabelFooter(numAlunos);
        formatLabelFooter(numLEIPL);
        formatLabelFooter(numLEI);
        formatLabelFooter(numDA);
        formatLabelFooter(numSI);
        formatLabelFooter(numRAS);
        formatLabelFooter(numPossibilidade);
        formatLabelFooter(medClassificacao);

    }

    private void preparaInsereCSV() {
        hboxInsereCSV = new VBox();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Ficheiro CSV");
        btnExeInsereCSV = new Button("Abrir Ficheiro");
        btnExeInsereCSV.setId("button_submit");
        CSSManager.applyCSS(btnExeInsereCSV, "button_SUBMIT.css");
        hboxInsereCSV.getChildren().addAll(btnExeInsereCSV);

    }

    private void preparaInsereAluno() {

        Label nAluno = new Label("N.Aluno");
        nAluno.setPrefWidth(50);
        txNumero = new TextField();
        txNumero.setPrefWidth(300);
        Label email = new Label("Email:");
        email.setPrefWidth(50);
        tfEmail = new TextField();
        tfEmail.setPrefWidth(300);

        Label nome = new Label("Nome:");
        nome.setPrefWidth(50);
        tfNome = new TextField();
        tfNome.setPrefWidth(300);

        Label lcurso = new Label("Curso");
        lcurso.setPrefWidth(75);
        curso = new ChoiceBox<>();
        curso.getItems().addAll(Constantes.getCursos());
        curso.setPrefWidth(90);

        Label lRamo = new Label("Ramo");
        lRamo.setPrefWidth(40);
        ramo = new ChoiceBox<>();
        ramo.getItems().addAll(Constantes.getRamos());
        ramo.setPrefWidth(90);

        Label lclass = new Label("Classificação");
        lclass.setPrefWidth(75);
        tfClass = new TextField();
        tfClass.setPrefWidth(60);
        possibilidade = new CheckBox("Possibilidade de Estágio");

        btnExeInsereAluno = new Button("Inserir Aluno");
        btnInsereCSV= new Button("Inserir Por CSV");
        btnExeInsereAluno.setId("button_submit");
        CSSManager.applyCSS(btnExeInsereAluno, "button_SUBMIT.css");
        btnInsereCSV.setId("button_submit");
        CSSManager.applyCSS(btnInsereCSV, "button_SUBMIT.css");
        HBox boxNum = new HBox();
        boxNum.getChildren().addAll(nAluno, txNumero);
        boxNum.setSpacing(30);
        HBox boxEmail = new HBox();
        boxEmail.getChildren().addAll(email, tfEmail);
        boxEmail.setSpacing(30);
        HBox boxNome = new HBox();
        boxNome.getChildren().addAll(nome, tfNome);
        boxNome.setSpacing(30);
        VBox boxInputText = new VBox();
        boxInputText.getChildren().addAll(boxNum,boxEmail, boxNome);
        boxInputText.setSpacing(30);

        HBox boxClass = new HBox();
        boxClass.getChildren().addAll(lclass,tfClass, possibilidade);
        boxClass.setSpacing(30);
        HBox choices = new HBox();
        choices.getChildren().addAll(lcurso,curso,lRamo, ramo);
        choices.setSpacing(30);
        boxRight = new VBox();
        hBtn = new HBox();
        hBtn.getChildren().addAll(btnExeInsereAluno, btnInsereCSV);
        hBtn.setAlignment(Pos.CENTER);
        hBtn.setSpacing(30);
        boxRight.getChildren().addAll(boxClass,choices,hBtn);
        boxRight.setSpacing(30);

        hboxInsereAluno = new HBox();
        hboxInsereAluno.getChildren().addAll(boxInputText,boxRight);
        hboxInsereAluno.setSpacing(100);
        hboxInsereAluno.setAlignment(Pos.CENTER);
        HBox.setMargin(boxInputText, new Insets(30,0,30,0));
        HBox.setMargin(boxRight, new Insets(30,0,30,0));

        btnExeEditAluno = new Button("Atualizar");
        btnCancelEdit = new Button("Cancelar");

        hBtnEdit = new HBox();
        hBtnEdit.getChildren().addAll(btnExeEditAluno, btnCancelEdit);
        hBtnEdit.setAlignment(Pos.CENTER);
        hBtnEdit.setSpacing(30);

    }


    private void createMenu() {
        btnGestaoAlunos = new ButtonMenu("Gestão de Alunos");
        btnInsereManual= new ButtonMenu("Insere Aluno");
        btnExport= new ButtonMenu("Exportar Alunos Para CSV");
        btnRemoveAll= new ButtonMenu("Remover Todos");
        btnRecuar= new ButtonMenu("Voltar");
        menu = new MenuVertical(btnGestaoAlunos, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
            atualizaStats();
        });
        model.addPropertyChangeListener(ModelManager.PROP_CLOSE_STATE, evt -> {
            updateClose();
        });
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {
            atualizaStats();
            updateTable();
        });

        btnRecuar.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
            model.recuarFase();
        });

        btnInsereManual.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
            hboxInsereAluno.setVisible(true);

            clearFields();
            hBtn.getChildren().clear();
            hBtn.getChildren().addAll(btnExeInsereAluno, btnExeInsereCSV);
            update();
        });
        btnExeInsereCSV.setOnAction(actionEvent -> {
            File f = fileChooser.showOpenDialog(null);

            try {
                model.importAlunos(f.getAbsolutePath());
            } catch (CollectionBaseException e) {
                System.out.println(e.getMessageOfExceptions());

                AlertSingleton.getInstanceWarning().setAlertText("Problemas na Inserção do Aluno","as", e.getMessageOfExceptions());
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnRemoveAll.setOnAction(actionEvent -> {
            model.removeAllAlunos();
            update();
        });
        btnExeInsereAluno.setOnAction(actionEvent -> {
            String nome = tfNome.getText();
            Long nAluno;
            Double classificacao;
            if(txNumero.getText().equals("")){
                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Inserção do Aluno", "Numero de Aluno não preenchido");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            try {
                nAluno = Long.parseLong(txNumero.getText());
                classificacao = Double.parseDouble(tfClass.getText());

            }catch (NumberFormatException e){

                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Inserção do Aluno", "Numero de ALuno/Classificação Inválido\n" +
                        "O Número de aluno deverá ser um inteiro e a classificação compreendida entre 0.0 e 1.0");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            String email = tfEmail.getText();
            Boolean isPossible = possibilidade.isSelected();
            String curso = this.curso.getValue();
            String ramo = this.ramo.getValue();
            if(nome.equals("") || email.equals("") || curso == null || ramo == null){

                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Inserção do Aluno", "Dados Por Preencher");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            Aluno a = new Aluno(email,nome, nAluno,curso,ramo,classificacao,isPossible);
            ErrorCode e = model.insereAluno(a);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("Problemas na Inserção do Aluno","", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
            clearFields();

        });

        btnCancelEdit.setOnAction(actionEvent -> {
            clearFields();
            nodeShow.forEach(n -> n.setVisible(false));
        });

        btnExport.setOnAction(actionEvent -> {
            File f = fileChooser.showSaveDialog(null);
            ErrorCode e = model.exportCSV(f.getAbsolutePath());
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("Problemas na Exportação do CSV","as", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }

        });
        btnGestaoAlunos.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
        });

        btnExeEditAluno.setOnAction(actionEvent -> {
            String nome = tfNome.getText();
            Long nAluno;
            Double classificacao;
            try {
                nAluno = Long.parseLong(txNumero.getText());
                classificacao = Double.parseDouble(tfClass.getText());

            }catch (NumberFormatException e){
                System.out.println("Numero de aluno ou classificacao invalido");
                return;
            }
            String email = tfEmail.getText();
            Boolean isPossible = possibilidade.isSelected();
            String curso = this.curso.getValue();
            String ramo = this.ramo.getValue();
            if(nome.equals("") || curso.equals("") || ramo.equals("")){
                return;
            }
            ErrorCode e = model.editAluno(email,nome, nAluno,curso,ramo,classificacao,isPossible);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Edição do Aluno", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
            hboxInsereAluno.setVisible(false);
        });
    }

    private void preparaTable() {


        tableView = new TableAlunos(model);

        preparaEditarRemover();

    }
    private void preparaEditarRemover() {
        TableColumn<Aluno, Button> colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(alunoButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                hboxInsereAluno.setVisible(true);
                txNumero.setText(Long.toString(alunoButtonCellDataFeatures.getValue().getNumeroAluno()));
                txNumero.setDisable(true);
                tfNome.setText(alunoButtonCellDataFeatures.getValue().getNome());
                tfEmail.setText(alunoButtonCellDataFeatures.getValue().getEmail());
                tfEmail.setDisable(true);
                tfClass.setText(Double.toString(alunoButtonCellDataFeatures.getValue().getClassificacao()));
                curso.setValue(alunoButtonCellDataFeatures.getValue().getSiglaCurso());
                ramo.setValue(alunoButtonCellDataFeatures.getValue().getSiglaRamo());
                possibilidade.setSelected(alunoButtonCellDataFeatures.getValue().isPossibilidade());

                hBtn.getChildren().clear();
                hBtn.getChildren().add(hBtnEdit);
            });
            editar.setId("button_editar");
            CSSManager.applyCSS(editar,"button_editar.css");
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        TableColumn<Aluno, Button> colButton = new TableColumn<>("Remove");
        colButton.setCellValueFactory(alunoButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                model.removeAluno(alunoButtonCellDataFeatures.getValue().getNumeroAluno());
            });
            remover.setId("button_delete");
            CSSManager.applyCSS(remover,"buttonDelete.css");
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);

        tableView.addColButton(colEditar);
        tableView.addColButton(colButton);
    }

    private void clearFields(){
        txNumero.setDisable(false);
        tfEmail.setDisable(false);
        txNumero.clear();
        tfNome.clear();
        tfEmail.clear();
        tfClass.clear();
        curso.setValue("");
        ramo.setValue("");
        possibilidade.setSelected(false);
    }
    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_ALUNOS);
        updateTable();
    }

    private void updateTable(){
        if(model.getState() != null && model.getState() == EnumState.GESTAO_ALUNOS){
            tableView.getItems().clear();
            tableView.getItems().addAll(model.getAlunos());
        }
    }
    private void updateClose() {
        if(model.getCloseState(EnumState.CONFIG_OPTIONS)){
            nodeShow.forEach(n -> n.setVisible(false));
            tableView.removeCols("Editar", "Remove");
            menu.getChildren().removeAll(btnInsereManual, btnRemoveAll);
        }else{
            if(!menu.getChildren().contains(btnInsereManual)){
                menu.getChildren().add(1,btnInsereManual);
                menu.getChildren().add(3,btnRemoveAll);
                preparaEditarRemover();
            }
        }
        update();
    }
}
