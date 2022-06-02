package pt.isec.pa.apoio_poe.ui.gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TableAlunos;
import pt.isec.pa.apoio_poe.utils.Constantes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class GestaoAlunosUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoAlunos, btnInsereManual, btnExport, btnEdit, btnRemove, btnRemoveAll,btnRecuar;

    Label title;
    TableView<Aluno> tableView;
    Aluno a;
    PieChart graphPie;
    Pane insereAluno;
    HBox hboxInsereAluno, hboxEditAluno; VBox hboxInsereCSV;
    Button btnExeInsereAluno, btnExeInsereCSV, btnExeEditAluno,  btnInsereCSV;
    TextField txNumero, tfNome,tfEmail, tfClass, txNumeroEdit, tfNomeEdit,tfEmailEdit, tfClassEdit;
    ChoiceBox<String> curso, ramo, cursoEdit, ramoEdit;
    CheckBox possibilidade, possibilidadeEdit;
   List<Node> nodeShow;
   FileChooser fileChooser;
    VBox boxRight;
    HBox hBtn; HBox hBtnEdit;
    public GestaoAlunosUI(ModelManager model) {
        this.model = model;
        a = new Aluno("asd","Daniel", 123L,"LEI","DA",1.0,true);
        createViews();
        registerHandlers();
        update();

    }

    private void createViews() {
        createMenu();
        title = new Label("Gestao de Alunos");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(30,0,30,0));
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);
        VBox container = new VBox();
        preparaTable();
        preparaInsereAluno();
        preparaInsereCSV();
        //graphPie = new PieChart();
        //container.getChildren().addAll(titulo, tableView, hboxInsereAluno);
        nodeShow = new ArrayList<>();
        nodeShow.add(hboxInsereAluno);
        nodeShow.add(hboxEditAluno);
        nodeShow.add(hboxInsereCSV);
        nodeShow.forEach(n -> n.setVisible(false));
        container.getChildren().addAll(titulo, tableView, hboxInsereAluno, hboxEditAluno, hboxInsereCSV);

        setCenter(container);

    }

    private void preparaInsereCSV() {
        hboxInsereCSV = new VBox();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Ficheiro CSV");
        btnExeInsereCSV = new Button("Abrir Ficheiro");
        hboxInsereCSV.getChildren().addAll(btnExeInsereCSV);

    }

    private void preparaInsereAluno() {
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
        HBox.setMargin(boxInputText, new Insets(30,0,0,0));
        HBox.setMargin(boxRight, new Insets(30,0,0,0));

        /* EDITAR ALUNO */

        Label nAlunoEdit = new Label("N.Aluno");
        nAluno.setPrefWidth(50);
        txNumeroEdit = new TextField();
        txNumeroEdit.setPrefWidth(300);
        Label emailEdit = new Label("Email:");
        emailEdit.setPrefWidth(50);
        tfEmailEdit = new TextField();
        tfEmailEdit.setPrefWidth(300);


        Label nomeEdit = new Label("Nome:");
        nomeEdit.setPrefWidth(50);
        tfNomeEdit = new TextField();
        tfNomeEdit.setPrefWidth(300);

        Label lcursoEdit = new Label("Curso");
        lcursoEdit.setPrefWidth(75);
        cursoEdit = new ChoiceBox<>();
        cursoEdit.getItems().addAll(Constantes.getCursos());
        cursoEdit.setPrefWidth(90);

        Label lRamoEdit = new Label("Ramo");
        lRamoEdit.setPrefWidth(40);
        ramoEdit = new ChoiceBox<>();
        ramoEdit.getItems().addAll(Constantes.getRamos());
        ramoEdit.setPrefWidth(90);

        Label lclassEdit = new Label("Classificação");
        lclassEdit.setPrefWidth(75);
        tfClassEdit = new TextField();
        tfClassEdit.setPrefWidth(60);
        possibilidadeEdit = new CheckBox("Possibilidade de Estágio");


        btnExeEditAluno = new Button("Atualizar Aluno");

        HBox boxNumEdit = new HBox();
        boxNumEdit.getChildren().addAll(nAlunoEdit, txNumeroEdit);
        boxNumEdit.setSpacing(30);
        HBox boxEmailEdit = new HBox();
        boxEmailEdit.getChildren().addAll(emailEdit, tfEmailEdit);
        boxEmailEdit.setSpacing(30);
        HBox boxNomeEdit = new HBox();
        boxNomeEdit.getChildren().addAll(nomeEdit, tfNomeEdit);
        boxNomeEdit.setSpacing(30);
        VBox boxInputTextEdit = new VBox();
        boxInputTextEdit.getChildren().addAll(boxNumEdit,boxEmailEdit, boxNomeEdit);
        boxInputTextEdit.setSpacing(30);

        HBox boxClassEdit = new HBox();
        boxClassEdit.getChildren().addAll(lclassEdit,tfClassEdit, possibilidadeEdit);
        boxClassEdit.setSpacing(30);
        HBox choicesEdit = new HBox();
        choicesEdit.getChildren().addAll(lcursoEdit,cursoEdit,lRamoEdit, ramoEdit);
        choicesEdit.setSpacing(30);
        VBox boxRightEdit = new VBox();
        hBtnEdit = new HBox();
        hBtnEdit.getChildren().add(btnExeEditAluno);
        hBtnEdit.setAlignment(Pos.CENTER);
        hBtnEdit.setSpacing(30);
        boxRightEdit.getChildren().addAll(boxClassEdit,choicesEdit,hBtnEdit);
        boxRightEdit.setSpacing(30);

        hboxEditAluno = new HBox();
        hboxEditAluno.getChildren().addAll(boxInputTextEdit,boxRightEdit);
        hboxEditAluno.setSpacing(100);
        hboxEditAluno.setAlignment(Pos.CENTER);
        HBox.setMargin(boxInputTextEdit, new Insets(-135,0,0,0));
        HBox.setMargin(boxRightEdit, new Insets(-135,0,0,0));

    }





    private void createMenu() {
        btnGestaoAlunos = new ButtonMenu("Gestão de Alunos");
        btnInsereManual= new ButtonMenu("Insere Aluno");
        btnExport= new ButtonMenu("Exportar Alunos Para CSV");
        btnRemoveAll= new ButtonMenu("Remover Todos");
        btnRecuar= new ButtonMenu("Voltar");
        MenuVertical menu = new MenuVertical(btnGestaoAlunos, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
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
                model.importAlunos(f.getAbsolutePath());
            } catch (CollectionBaseException e) {
                System.out.println(e.getMessageOfExceptions());
            }
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
            }
        });
        btnRemoveAll.setOnAction(actionEvent -> {
            model.removeAllAlunos();
            update();
            System.out.println("Aqui");
        });
        btnExeInsereAluno.setOnAction(actionEvent -> {
            String nome = tfNome.getText();
            Long nAluno;
            Double classificacao;
            try {
                nAluno = Long.parseLong(txNumero.getText());
                classificacao = Double.parseDouble(tfClass.getText());

            }catch (NumberFormatException e){
                System.out.println("Numero de aluno invalido");
                return;
            }
            String email = tfEmail.getText();
            Boolean isPossible = possibilidade.isSelected();
            String curso = this.curso.getValue();
            String ramo = this.ramo.getValue();
            if(nome == null || email == null || curso == null || ramo == null){
                return;
            }
            Aluno a = new Aluno(email,nome, nAluno,curso,ramo,classificacao,isPossible);
            if(model.insereAluno(a)!= ErrorCode.E0){
                System.out.println("Correu algo mal");
            }
            clearFields();

        });
        btnExport.setOnAction(actionEvent -> {
            File f = fileChooser.showSaveDialog(null);

            if(model.exportCSV(f.getAbsolutePath())!= ErrorCode.E0){
                System.out.println("Problema na exportacao");
            }

        });
        btnGestaoAlunos.setOnAction(actionEvent -> {
            nodeShow.forEach(n -> n.setVisible(false));
        });

        btnExeEditAluno.setOnAction(actionEvent -> {
            hboxInsereAluno.setVisible(false);
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_ALUNOS);
    }



    private void preparaTable() {
        Consumer<Aluno> edit = (a) -> {
          hboxInsereAluno.setVisible(true);
          txNumero.setText(Long.toString(a.getNumeroAluno()));
          txNumero.setDisable(true);
          tfNome.setText(a.getNome());
          tfEmail.setText(a.getEmail());
          tfEmail.setDisable(true);
          tfClass.setText(Double.toString(a.getClassificacao()));
          curso.setValue(a.getSiglaCurso());
          ramo.setValue(a.getSiglaRamo());
          possibilidade.setSelected(a.isPossibilidade());

          hBtn.getChildren().clear();
          hBtn.getChildren().add(hBtnEdit);
        };

        tableView = new TableAlunos(model, edit);


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
}
