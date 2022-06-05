package pt.isec.pa.apoio_poe.ui.gui;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TableDocentes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class GestaoDocentesUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoDocentes, btnInsereManual, btnExport, btnRemoveAll,btnRecuar;

    Label title, numDocentes;
    TableDocentes tableView;

    HBox vboxInsereDocente, hBtn;
    Button btnExeInsereDocente,  btnInsereCSV, btnExeEditarDocente, btnCancelEdit;
    TextField  tfNome,tfEmail;
    List<Node> nodeShow;
    FileChooser fileChooser;
    Integer nDocentes;

    public GestaoDocentesUI(ModelManager model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();

    }

    private void createViews() {
        createMenu();
        title = new Label("Gestao de Docentes");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(30,0,30,0));
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);
        VBox container = new VBox();
        preparaTable();
        preparaInsereDocente();
        fileChooser = new FileChooser();
        //graphPie = new PieChart();
        //container.getChildren().addAll(titulo, tableView, hboxInsereAluno);
        nodeShow = new ArrayList<>();
        nodeShow.add(vboxInsereDocente);

        numDocentes = new Label("Docentes: ");

        HBox stats = new HBox();
        stats.getChildren().add(numDocentes);
        stats.setAlignment(Pos.BASELINE_LEFT);

        nodeShow.forEach(n -> n.setVisible(false));
        container.getChildren().addAll(titulo, tableView,vboxInsereDocente, stats);

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
        btnInsereCSV= new Button("Inserir Por CSV");

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
        MenuVertical menu = new MenuVertical(btnGestaoDocentes, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });
        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> {
            updateTable();
        });

        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> {
            atualizaStats();
        });

        btnRecuar.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
            model.recuarFase();
        });

        btnInsereCSV.setOnAction(actionEvent -> {
//            nodeShow.forEach(n -> n.setVisible(false));
//            hboxInsereCSV.setVisible(true);
            File f = fileChooser.showOpenDialog(null);
            try {
                model.importDocentes(f.getAbsolutePath());
            } catch (CollectionBaseException e) {
                System.out.println(e.getMessageOfExceptions());
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
                return;
            }
            if(model.insereDocente(email,nome)!= ErrorCode.E0){
                System.out.println("Correu algo mal");
            }
            clearFields();
            atualizaStats();
        });

        btnExeEditarDocente.setOnAction(actionEvent -> {
            String nome = tfNome.getText();
            String email = tfEmail.getText();

            if(nome.equals("") || email.equals("") ){
                return;
            }
            if(model.editDocente(email,nome) != ErrorCode.E0){
                System.out.println("Correu algo mal");
            }
            vboxInsereDocente.setVisible(false);
        });

        btnCancelEdit.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
        });

        btnExport.setOnAction(actionEvent -> {
            File f = fileChooser.showSaveDialog(null);

            if(model.exportCSV(f.getAbsolutePath())!= ErrorCode.E0){
                System.out.println("Problema na exportacao");
            }

        });
        btnGestaoDocentes.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_DOCENTES);
        updateTable();
    }



    private void preparaTable() {
        Consumer<Docente> edit = (d) -> {
            tfNome.setText(d.getNome());
            tfEmail.setText(d.getEmail());
            tfEmail.setDisable(true);
            hBtn.getChildren().clear();
            hBtn.getChildren().addAll(btnExeEditarDocente,btnCancelEdit);

            vboxInsereDocente.setVisible(true);
        };
        tableView = new TableDocentes(model, edit);
        TableColumn<Docente, Button> colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(docenteButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                System.out.println(docenteButtonCellDataFeatures.getValue());
                tfNome.setText(docenteButtonCellDataFeatures.getValue().getNome());
                tfEmail.setText(docenteButtonCellDataFeatures.getValue().getEmail());

                vboxInsereDocente.setVisible(true);
                //TODO editar docente
            });
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        TableColumn<Docente, Button> colButton = new TableColumn<>("Remover");
        colButton.setCellValueFactory(docenteButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                System.out.println(docenteButtonCellDataFeatures.getValue());
                //TODO remover docente

            });
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);
        tableView.addColButton(colEditar);
        tableView.addColButton(colButton);
    }
    void updateTable(){
        if(model.getState()!=null && model.getState() == EnumState.GESTAO_DOCENTES){
            System.out.println("Update docentes" + model.getDocentes().size());
            tableView.getItems().clear();
            tableView.getItems().addAll(model.getDocentes());
        }
    }

    private void clearFields(){
        tfNome.clear();
        tfEmail.clear();
        tfEmail.setDisable(false);

    }
}
