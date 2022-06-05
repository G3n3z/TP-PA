package pt.isec.pa.apoio_poe.ui.gui;


import javafx.beans.property.ReadOnlyObjectWrapper;
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
        nodeShow.add(hboxInsereCSV);
        nodeShow.forEach(n -> n.setVisible(false));

        numAlunos = new Label();
        numLEIPL = new Label();
        numLEI = new Label("LEI: ");
        numDA = new Label("DA: ");
        numSI = new Label("SI: ");
        numRAS = new Label("RAS: ");
        medClassificacao = new Label("Classificacao media : ");
        numPossibilidade = new Label("C/ Possibilidade de estagio : ");

        HBox statsFooter = new HBox();
        statsFooter.getChildren().addAll(numAlunos, numLEIPL, numLEI, numDA, numSI, numRAS, medClassificacao, numPossibilidade);
        statsFooter.setAlignment(Pos.BASELINE_CENTER);
        statsFooter.setSpacing(20.0);

        container.getChildren().addAll(titulo, tableView, hboxInsereAluno, hboxInsereCSV, statsFooter);


        setCenter(container);

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
    }

    private void preparaInsereCSV() {
        hboxInsereCSV = new VBox();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Ficheiro CSV");
        btnExeInsereCSV = new Button("Abrir Ficheiro");
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
        MenuVertical menu = new MenuVertical(btnGestaoAlunos, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {
            updateTable();
        });

        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {
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

        btnCancelEdit.setOnAction(actionEvent -> {
            clearFields();
            nodeShow.forEach(n -> n.setVisible(false));
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
            if(model.editAluno(email,nome, nAluno,curso,ramo,classificacao,isPossible) != ErrorCode.E0){
                System.out.println("Não foi possivel editar");

            }
            hboxInsereAluno.setVisible(false);
        });
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

        tableView = new TableAlunos(model);


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
