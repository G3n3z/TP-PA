package pt.isec.pa.apoio_poe.ui.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;
import pt.isec.pa.apoio_poe.ui.resource.CSSManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestaoPropostasUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoPropostas,btnInsereManual,btnExport,btnRemoveAll,btnRecuar;
    MenuVertical menu;
    TablePropostas tableView;
    VBox center;
    FileChooser fileChooser;
    HBox hBoxBtnInserirProposta;
    TextField tFId, tfTitulo, tfDocente, tfEntidade, tfNAluno;
    ChoiceBox<String> choiceTipo;
    CheckBox ckDA, ckSI, ckRas;
    VBox vID, vTitulo,vDocente,vEntidade, vAluno, vChoice;
    HBox hRamos, hBoxInput;
    VBox boxLeft;
    VBox boxRight;
    Button btnInsereProposta, btnInsereCSV, btnEditProposta, btnCancelEdit;
    long nPropostas, nT1, nT2, nT3, nDA, nSI, nRAS;
    List<Long> stats = new ArrayList<>();
    Label numPropostas, numT1, numT2, numT3, numDA, numSI, numRAS, title;

    boolean visible = false;
    private TableColumn<Proposta, Button> colButton, colEditar;

    public GestaoPropostasUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }



    private void createViews() {
        fileChooser = new FileChooser();
        createMenu();
        preparaTable();
        preparaInserir();
        center = new VBox();
        hBoxInput = new HBox(boxLeft);
        hBoxInput.setAlignment(Pos.CENTER);
        title = new Label("Gestão de Propostas");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(25,0,25,0));
        titulo.setPrefHeight(50);
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);

        HBox statsFooter = new HBox();
        numPropostas = new Label();
        numT1 = new Label();
        numT2 = new Label();
        numT3 = new Label();
        numDA = new Label();
        numSI = new Label();
        numRAS = new Label();

        statsFooter.getChildren().addAll(numPropostas, numT1, numT2, numT3, numDA, numSI, numRAS);
        statsFooter.setAlignment(Pos.BASELINE_CENTER);
        statsFooter.setSpacing(20.0);
        statsFooter.setPadding(new Insets(25));
        statsFooter.setPrefHeight(50);
        statsFooter.setBackground(new Background(new BackgroundFill(Color.web("#37304a"),CornerRadii.EMPTY,Insets.EMPTY)));
        tableView.setPrefHeight(400);
        hBoxInput.setPrefHeight(200);
        hBoxInput.setPadding(new Insets(-20,0,25,0));
        BorderPane container = new BorderPane();

        center.getChildren().addAll(titulo,tableView, hBoxInput, statsFooter);
        container.setTop(titulo);
        container.setCenter(center);
        container.setBottom(statsFooter);

        setCenter(container);

    }

    private void formatLabelFooter(Label label){
        label.setFont(new Font(14));
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold");
    }

    public void atualizaStats(){

        stats = model.getStatsPropostas();
        nPropostas = stats.get(0);
        nT1 = stats.get(1);
        nT2 = stats.get(2);
        nT3 = stats.get(3);
        nDA = stats.get(4);
        nSI = stats.get(5);
        nRAS = stats.get(6);
        numPropostas.setText("Propostas: "+nPropostas);
        numT1.setText("Estagios: "+nT1);
        numT2.setText("Projetos: "+nT2);
        numT3.setText("Autopropostos: "+nT3);
        numDA.setText("DA: "+nDA);
        numSI.setText("SI: "+nSI);
        numRAS.setText("RAS: "+nRAS);
        formatLabelFooter(numPropostas);
        formatLabelFooter(numT1);
        formatLabelFooter(numT2);
        formatLabelFooter(numT3);
        formatLabelFooter(numDA);
        formatLabelFooter(numSI);
        formatLabelFooter(numRAS);
    }

    private void preparaInserir() {
        Label lId = new Label("ID");
        tFId = new TextField();
        Label lTitulo = new Label("Titulo");
        tfTitulo = new TextField();
        Label lDocente = new Label("Docente");
        tfDocente = new TextField();
        Label lEntidade = new Label("Entidade");
        tfEntidade = new TextField();
        Label lNalunos = new Label("N.Aluno");
        tfNAluno = new TextField();

        Label lChoice = new Label("Tipo");
        choiceTipo = new ChoiceBox<>();
        choiceTipo.getItems().addAll("T1", "T2", "T3");
        ckDA = new CheckBox("DA"); ckSI = new CheckBox("SI"); ckRas = new CheckBox("RAS");

        vID= new VBox(lId, tFId);
        vTitulo= new VBox(lTitulo, tfTitulo);
        vTitulo.setPrefWidth(300);
        vDocente= new VBox(lDocente,tfDocente);
        vDocente.setPrefWidth(300);
        vEntidade= new VBox(lEntidade, tfEntidade);
        vEntidade.setPrefWidth(300);
        vAluno= new VBox(lNalunos, tfNAluno);
        vAluno.setPrefWidth(300);
        hRamos = new HBox(ckDA, ckRas, ckSI);
        hRamos.setSpacing(20);
        Label lRamos = new Label("Ramos:");
        VBox vRamos = new VBox(lRamos,hRamos);
        vRamos.setPrefWidth(300);
        vChoice = new VBox(lChoice, choiceTipo);
        HBox hBoxID = new HBox(vID, vChoice);
        hBoxID.setPrefWidth(300);
        hBoxID.setSpacing(30);

        HBox hBox1 = new HBox(hBoxID,vDocente);
        HBox hBox2 = new HBox(vRamos,vEntidade);
        HBox hBox3 = new HBox(vTitulo,vAluno);
        hBox1.setSpacing(30);
        hBox2.setSpacing(30);
        hBox3.setSpacing(30);

        btnInsereProposta = new Button("Insere Proposta");
        btnInsereCSV = new Button("Importar CSV");
        btnEditProposta = new Button("Editar Proposta");
        btnCancelEdit = new Button("Cancelar");
        hBoxBtnInserirProposta = new HBox(btnInsereProposta, btnInsereCSV);
        hBoxBtnInserirProposta.setSpacing(50);
        hBoxBtnInserirProposta.setAlignment(Pos.CENTER);

        boxLeft = new VBox(hBox1,hBox2,hBox3, hBoxBtnInserirProposta);
        boxLeft.setSpacing(30);
        boxLeft.setAlignment(Pos.CENTER);
        VBox.setMargin(hBox1, new Insets(40,0,0,0));
        btnInsereProposta.setId("button_submit");
        CSSManager.applyCSS(btnInsereProposta, "button_SUBMIT.css");
        btnInsereCSV.setId("button_submit");
        CSSManager.applyCSS(btnInsereCSV, "button_SUBMIT.css");
        btnEditProposta.setId("button_submit");
        CSSManager.applyCSS(btnEditProposta, "button_SUBMIT.css");
        btnCancelEdit.setId("button_submit");
        CSSManager.applyCSS(btnCancelEdit, "button_SUBMIT.css");
//        boxLeft = new VBox(hBoxID, vRamos,vTitulo);
//        boxRight = new VBox(vDocente, vEntidade, vAluno);
//        boxLeft.setSpacing(40);
//        boxRight.setSpacing(40);

    }

    private void preparaTable() {

        tableView = new TablePropostas(model, null);
        colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(propostaButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                System.out.println(propostaButtonCellDataFeatures.getValue());
                updateEditFields(propostaButtonCellDataFeatures.getValue());
            });
            editar.setId("button_editar");
            CSSManager.applyCSS(editar,"button_editar.css");
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        colButton = new TableColumn<>("Remover");
        colButton.setCellValueFactory(propostaButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                System.out.println(propostaButtonCellDataFeatures.getValue());
                model.removeProposta(propostaButtonCellDataFeatures.getValue().getId());
            });
            remover.setId("button_delete");
            CSSManager.applyCSS(remover,"buttonDelete.css");
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);
        //tableView.setPrefWidth("Entidade");
        tableView.addColButton(colEditar);
        tableView.addColButton(colButton);
    }

    private void updateEditFields(Proposta proposta) {
        visible = true;
        update();
        hBoxBtnInserirProposta.getChildren().clear();
        hBoxBtnInserirProposta.getChildren().addAll(btnEditProposta,btnCancelEdit);
        tFId.setText(proposta.getId());
        tFId.setDisable(true);
        choiceTipo.setValue(proposta.getTipo());
        choiceTipo.setDisable(true);
        changeEditables();
        List<String> ramos = proposta.getRamos();
        if (ramos != null){
            for (String r : ramos){
                if (r.equals(ckDA.getText())){
                    ckDA.setSelected(true);
                } else if (r.equals(ckRas.getText())) {
                    ckRas.setSelected(true);
                } else if (r.equals(ckSI.getText())) {
                    ckSI.setSelected(true);
                }
            }
        }

        tfDocente.setText(proposta.getEmailDocente());
        tfEntidade.setText(proposta.getEntidade());
        Long naluno = proposta.getNumAluno();
        if(naluno != null){
            tfNAluno.setText(Long.toString(proposta.getNumAluno()));
        }
        tfTitulo.setText(proposta.getTitulo());

    }

    private void changeEditables(){

        tfDocente.setDisable(false);
        tfEntidade.setDisable(false);
        ckDA.setDisable(false);
        ckRas.setDisable(false);
        ckSI.setDisable(false);
        if (choiceTipo.getValue() == null){
            return;
        }
        switch (choiceTipo.getValue()){
            case "T1" -> {tfDocente.setDisable(true);}
            case "T2" -> {tfEntidade.setDisable(true);}
            case "T3" ->{
                tfDocente.setDisable(true);
                tfEntidade.setDisable(true);
                ckDA.setDisable(true);
                ckRas.setDisable(true);
                ckSI.setDisable(true);
            }
        }
    }

    private void createMenu() {
        btnGestaoPropostas = new ButtonMenu("Gestão de Propostas");
        btnInsereManual = new ButtonMenu("Insere Propostas");
        btnExport= new ButtonMenu("Exportar Propostas Para CSV");
        btnRemoveAll= new ButtonMenu("Remover Todos");
        btnRecuar= new ButtonMenu("Voltar");
        menu = new MenuVertical(btnGestaoPropostas, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
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

        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {
            atualizaStats();
        });

        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {
            updateTables();
        });
        btnRecuar.setOnAction(actionEvent -> {
            visible = false;
            model.recuarFase();
        });
        
        btnRemoveAll.setOnAction(actionEvent -> {
            visible = false;
            model.removeAllAlunos();
            update();
        });
        btnExport.setOnAction(actionEvent -> {
            visible = false;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Drawing (*.csv)", "*.csv")
            );
            File f = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(f == null){
                return;
            }
            ErrorCode error = model.exportCSV(f.getAbsolutePath());
            if(error != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Exportação dos dados das Propostas", "");
                AlertSingleton.getInstanceWarning().showAndWait();
            }

        });
        choiceTipo.setOnAction(actionEvent -> {
            changeEditables();
        });
        btnInsereManual.setOnAction(actionEvent -> {
            visible = true;
            update();
        });
        btnInsereCSV.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Drawing (*.csv)", "*.csv")
            );
            File f = fileChooser.showOpenDialog(this.getScene().getWindow());
            if(f == null){
                return;
            }
            boolean error = true;
            try {
                 error = model.importCSV(f.getAbsolutePath());
            } catch (CollectionBaseException e) {
                System.out.println(e.getMessageOfExceptions());
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas na Importação dos dados das Propostas", e.getMessageOfExceptions());
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnGestaoPropostas.setOnAction(actionEvent -> {
            visible = false;
            update();
        });
        btnInsereProposta.setOnAction(actionEvent -> {
            if(choiceTipo.getValue() == null){
                return;
            }
            if(tFId == null || tFId.getText().equals(""))
                return;

            List<String> ramos = new ArrayList<>();
            if(ckDA.isSelected()) ramos.add(ckDA.getText());
            if(ckRas.isSelected()) ramos.add(ckRas.getText());
            if(ckSI.isSelected()) ramos.add(ckSI.getText());
            System.out.println(ckDA.getText());

            ErrorCode e = model.insereProposta(choiceTipo.getValue(), tFId.getText(),ramos, tfTitulo.getText(), tfDocente.getText(), tfEntidade.getText(), tfNAluno.getText());
            if(e == ErrorCode.E0){
                update();
            }else{
                System.out.println(e.toString());
                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Introdução dos dados das Propostas", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnEditProposta.setOnAction(actionEvent -> {
            List<String> ramos = new ArrayList<>();
            if(ckDA.isSelected()) ramos.add(ckDA.getText());
            if(ckRas.isSelected()) ramos.add(ckRas.getText());
            if(ckSI.isSelected()) ramos.add(ckSI.getText());
            ErrorCode e = model.editProposta(choiceTipo.getValue(), tFId.getText(),ramos, tfTitulo.getText(), tfDocente.getText(), tfEntidade.getText(), tfNAluno.getText());
            if(e == ErrorCode.E0){
                update();
            }else{
                System.out.println(e.toString());
                AlertSingleton.getInstanceWarning().setAlertText("","Problemas Na edição do aluno", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });

        btnCancelEdit.setOnAction(actionEvent -> {
            visible = false;
            update();
        });

    }

    private void updateClose() {
        if(model.getCloseState(EnumState.CONFIG_OPTIONS)){
            visible = false;
            tableView.removeCols("Editar", "Remover");
            menu.getChildren().removeAll(btnInsereManual, btnRemoveAll);
            update();
        }
        else{
            if(!menu.getChildren().contains(btnInsereManual)) {
                tableView.addCols(colEditar);
                tableView.addCols(colButton);
                menu.getChildren().add(1,btnInsereManual);
                menu.getChildren().add(3,btnRemoveAll);

            }
        }
    }

    private void updateTables() {
        System.out.println("Update propostas" + model.getPropostas().size());
        tableView.getItems().clear();
        tableView.getItems().addAll(model.getPropostas());
    }

    private void update() {
        hBoxInput.setVisible(visible);
        clearFieldsInput();
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_PROPOSTAS);
        updateTables();

    }

    private void clearFieldsInput() {
        changeEditables();
        tFId.setText("");
        tFId.setDisable(false);
        choiceTipo.setDisable(false);
        choiceTipo.setValue("");
        ckDA.setSelected(false);
        ckSI.setSelected(false);
        ckRas.setSelected(false);
        tfTitulo.setText("");
        tfDocente.setText("");
        tfNAluno.setText("");
        tfEntidade.setText("");
        hBoxBtnInserirProposta.getChildren().clear();
        hBoxBtnInserirProposta.getChildren().addAll(btnInsereProposta, btnInsereCSV);
    }

    private void closedFase() {
        if(model == null){
            return;
        }
        if(model.getState() != EnumState.GESTAO_PROPOSTAS){
            return;
        }
        if (model.isClosed()){
            fechaFase();
        }
    }

    private void fechaFase() {

        visible = false;
        tableView.removeCols("Editar", "Remover");
        menu.getChildren().removeAll(btnInsereManual, btnRemoveAll);
    }
}
