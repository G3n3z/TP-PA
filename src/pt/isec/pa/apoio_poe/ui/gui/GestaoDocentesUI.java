package pt.isec.pa.apoio_poe.ui.gui;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;
import pt.isec.pa.apoio_poe.ui.resource.CSSManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GestaoDocentesUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoDocentes, btnInsereManual, btnExport, btnRemoveAll,btnRecuar;
    MenuVertical menu;
    Label title, numDocentes;
    TableDocentes tableView;

    HBox vboxInsereDocente, hBtn;
    Button btnExeInsereDocente,  btnInsereCSV, btnExeEditarDocente, btnCancelEdit;
    TextField  tfNome,tfEmail;
    List<Node> nodeShow;
    FileChooser fileChooser;
    Integer nDocentes;
    TableColumn<Docente, Button> colButton, colEditar;

    public GestaoDocentesUI(ModelManager model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();

    }

    private void createViews() {
        createMenu();
        title = new Label("Gestão de Docentes");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(25,0,25,0));
        titulo.setPrefHeight(50);
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);
        BorderPane container = new BorderPane();
        preparaTable();
        preparaInsereDocente();
        fileChooser = new FileChooser();
        nodeShow = new ArrayList<>();
        nodeShow.add(vboxInsereDocente);

        numDocentes = new Label("Docentes: ");
        numDocentes.setFont(new Font(14));
        numDocentes.setTextFill(Color.WHITE);
        numDocentes.setStyle("-fx-font-weight: bold");

        HBox statsFooter = new HBox();
        statsFooter.getChildren().add(numDocentes);
        statsFooter.setAlignment(Pos.BASELINE_CENTER);
        statsFooter.setSpacing(20.0);
        statsFooter.setPadding(new Insets(25));
        statsFooter.setPrefHeight(50);
        statsFooter.setBackground(new Background(new BackgroundFill(Color.web("#37304a"),CornerRadii.EMPTY,Insets.EMPTY)));
        tableView.setMaxHeight(400);
        tableView.setMinHeight(400);

        vboxInsereDocente.setPrefHeight(250);
        vboxInsereDocente.setPadding(new Insets(25,0,25,0));

        nodeShow.forEach(n -> n.setVisible(false));
        VBox centro = new VBox(tableView,vboxInsereDocente);

        container.setTop(titulo);
        container.setCenter(centro);
        container.setBottom(statsFooter);

        setCenter(container);

    }

    public void atualizaStats(){
        nDocentes = model.getDocentes().size();
        numDocentes.setText("Docentes: "+nDocentes);
    }


    private void preparaInsereDocente() {
//        insereAluno = new Pane();
//        hboxInsereAluno = new VBox();
//        VBox vbox = new VBox();
//        Label nAluno = new Label("N.Aluno");
//        nAluno.setPrefWidth(80);
//        txNumero = new TextField();
//        txNumero.setPrefWidth(300);
//        FieldHBox fnAluno = new FieldHBox(nAluno, txNumero);
//
//        Label email = new Label("Email:");
//        email.setPrefWidth(80);
//        tfEmail = new TextField();
//        tfEmail.setPrefWidth(300);
//        FieldHBox fnEmail = new FieldHBox(email, tfEmail);
//
//        Label nome = new Label("Nome:");
//        nome.setPrefWidth(80);
//        tfNome = new TextField();
//        tfNome.setPrefWidth(300);
//        FieldHBox fnNome = new FieldHBox(nome, tfNome);
//
//        Label lcurso = new Label("Curso");
//        lcurso.setPrefWidth(80);
//        curso = new ChoiceBox<>();
//        curso.getItems().addAll(Constantes.getCursos());
//        curso.setPrefWidth(90);
//        Label lRamo = new Label("Ramo");
//        lRamo.setPrefWidth(60);
//        ramo = new ChoiceBox<>();
//        ramo.getItems().addAll(Constantes.getRamos());
//        ramo.setPrefWidth(90);
//        FieldHBox fnCurso = new FieldHBox(lcurso, curso,lRamo, ramo);
//
//        Label lclass = new Label("Classificação");
//        lclass.setPrefWidth(80);
//        tfClass = new TextField();
//        tfClass.setPrefWidth(60);
//        possibilidade = new CheckBox("Possibilidade de Estágio");
//
//        FieldHBox fnClass = new FieldHBox(lclass,tfClass,possibilidade);
//        fnClass.setMarginField(2, new Insets(0,80,0,0));
//
//        btnExeInsereAluno = new Button("Inserir Aluno");
//        HBox hBtn = new HBox();
//        hBtn.getChildren().add(btnExeInsereAluno);
//        hBtn.setAlignment(Pos.CENTER);
//        hboxInsereAluno.getChildren().addAll(fnAluno, fnEmail,fnNome, fnCurso,fnClass, hBtn);
//        hboxInsereAluno.setSpacing(30);
//
//        VBox.setMargin(fnAluno, new Insets(40,0,0,0));
        Label email = new Label("Email:");
        email.setPrefWidth(50);
        tfEmail = new TextField();
        tfEmail.setPrefWidth(300);


        Label nome = new Label("Nome:");
        nome.setPrefWidth(50);
        tfNome = new TextField();
        tfNome.setPrefWidth(300);

        btnExeInsereDocente = new Button("Inserir Docente");
        btnExeInsereDocente.setId("button_submit");
        CSSManager.applyCSS(btnExeInsereDocente, "button_SUBMIT.css");
        btnInsereCSV= new Button("Inserir Por CSV");
        btnInsereCSV.setId("button_submit");
        CSSManager.applyCSS(btnInsereCSV, "button_SUBMIT.css");
        HBox boxEmail = new HBox();
        boxEmail.getChildren().addAll(email, tfEmail);
        boxEmail.setSpacing(30);
        HBox boxNome = new HBox();
        boxNome.getChildren().addAll(nome, tfNome);
        boxNome.setSpacing(30);
        VBox boxInputText = new VBox();
        boxInputText.getChildren().addAll(boxEmail, boxNome);
        boxInputText.setSpacing(30);


        hBtn = new HBox();
        hBtn.getChildren().addAll(btnExeInsereDocente, btnInsereCSV);
        hBtn.setAlignment(Pos.CENTER);
        hBtn.setSpacing(30);

        btnExeEditarDocente = new Button("Atualizar");
        btnCancelEdit = new Button("Cancelar");
        btnExeEditarDocente.setId("button_submit");
        CSSManager.applyCSS(btnExeEditarDocente, "button_SUBMIT.css");
        btnCancelEdit.setId("button_submit");
        CSSManager.applyCSS(btnCancelEdit, "button_SUBMIT.css");
        vboxInsereDocente = new HBox();
        vboxInsereDocente.getChildren().addAll(boxInputText, hBtn);
        vboxInsereDocente.setSpacing(100);
        vboxInsereDocente.setAlignment(Pos.CENTER);
        HBox.setMargin(boxInputText, new Insets(30,0,0,0));



    }





    private void createMenu() {
        btnGestaoDocentes = new ButtonMenu("Gestão de Docentes");
        btnInsereManual= new ButtonMenu("Insere Docente");
        btnExport= new ButtonMenu("Exportar Docentes Para CSV");
        btnRemoveAll= new ButtonMenu("Remover Todos");
        btnRecuar= new ButtonMenu("Voltar");
        menu = new MenuVertical(btnGestaoDocentes, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
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
        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> {
            atualizaStats();
            updateTable();
        });


        btnRecuar.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
            model.recuarFase();
        });

        btnInsereCSV.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir ficheiro...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ficheiro de texto (*.csv)", "*.csv")
            );
            File f = fileChooser.showOpenDialog(this.getScene().getWindow());
            if(f == null){
                return;
            }

            try {
                if(!model.importDocentes(f.getAbsolutePath())){
                    AlertSingleton.getInstanceWarning().setAlertText("Informação","Problemas na Inserção do Docentes", "Não foi possivel abrir o ficheiro");
                    AlertSingleton.getInstanceWarning().showAndWait();
                }
            } catch (CollectionBaseException e) {
                AlertSingleton.getInstanceWarning().setAlertText("Informação", "Problemas na Importação dos dados dos Docentes", e.getMessageOfExceptions());
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });

        btnInsereManual.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
            vboxInsereDocente.setVisible(true);
            hBtn.getChildren().clear();
            hBtn.getChildren().addAll(btnExeInsereDocente,btnInsereCSV);
            clearFields();
            update();
        });

        btnRemoveAll.setOnAction(actionEvent -> {
            model.removeAllDocentes();
            update();

        });
        btnExeInsereDocente.setOnAction(actionEvent -> {
            String nome = tfNome.getText();
            String email = tfEmail.getText();

            if(nome.equals("") || email.equals("") ){
                AlertSingleton.getInstanceWarning().setAlertText("Informação","Problemas na Introdução dos dados dos Docente", "Nome / Email por preencher");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            ErrorCode e = model.insereDocente(email,nome);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("Informação","Problemas na Inserção do Docente", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
            clearFields();

        });

        btnExeEditarDocente.setOnAction(actionEvent -> {
            String nome = tfNome.getText();
            String email = tfEmail.getText();

            if(nome.equals("") || email.equals("") ){
                AlertSingleton.getInstanceWarning().setAlertText("Informação","Problemas na Edição dos dados dos Docente", "Nome / Email por preencher");
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            ErrorCode e = model.editDocente(email,nome);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("Informação","Problemas na Edição do Docente", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
            vboxInsereDocente.setVisible(false);
        });

        btnCancelEdit.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
        });

        btnExport.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Gravar ficheiro...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV (Separado por vírgulas) (*.csv)", "*.csv")
            );
            File f = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(f == null){
                return;
            }
            ErrorCode e = model.exportCSV(f.getAbsolutePath());
            if(e!= ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("Informação","Problemas na Exporção do CSV dos Docente", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }

        });
        btnGestaoDocentes.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
        });
    }


    private void preparaTable() {
        tableView = new TableDocentes(model);
        colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(docenteButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                tfNome.setText(docenteButtonCellDataFeatures.getValue().getNome());
                tfEmail.setText(docenteButtonCellDataFeatures.getValue().getEmail());
                tfEmail.setDisable(true);
                hBtn.getChildren().clear();
                hBtn.getChildren().addAll(btnExeEditarDocente,btnCancelEdit);
                vboxInsereDocente.setVisible(true);

            });
            editar.setId("button_editar");
            CSSManager.applyCSS(editar,"button_editar.css");
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        colButton = new TableColumn<>("Remover");
        colButton.setCellValueFactory(docenteButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                model.removeDocente(docenteButtonCellDataFeatures.getValue().getEmail());
                clearFields();
            });
            remover.setId("button_delete");
            CSSManager.applyCSS(remover,"buttonDelete.css");
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);
        tableView.addColButton(colEditar);
        tableView.addColButton(colButton);
    }
    void updateTable(){
        if(model.getState()!=null && model.getState() == EnumState.GESTAO_DOCENTES){
            tableView.getItems().clear();
            tableView.getItems().addAll(model.getDocentes());
        }
    }
    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_DOCENTES);
        updateTable();
    }
    private void clearFields(){
        tfNome.clear();
        tfEmail.clear();
        tfEmail.setDisable(false);

    }


    private void updateClose() {
        if(model.getCloseState(EnumState.CONFIG_OPTIONS)){
            nodeShow.forEach(n -> n.setVisible(false));
            tableView.removeCols("Editar", "Remover");
            menu.getChildren().removeAll(btnInsereManual, btnRemoveAll);
            update();
        }else{
            if(!menu.getChildren().contains(btnInsereManual)){
                menu.getChildren().add(1,btnInsereManual);
                menu.getChildren().add(3,btnRemoveAll);
                tableView.addCols(colEditar);
                tableView.addCols(colButton);
                update();
            }
        }

    }
}
