package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TablePropostas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GestaoPropostasUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoPropostas,btnInsereManual,btnExport,btnRemoveAll,btnRecuar;
    TableView<Proposta> tableView;
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
    Button btnInsereProposta, btnInsereCSV, btnEditProposta;
    boolean visible = false;

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

        center.getChildren().addAll(tableView, hBoxInput);
        setCenter(center);

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
        hBoxBtnInserirProposta = new HBox(btnInsereProposta, btnInsereCSV);
        hBoxBtnInserirProposta.setSpacing(50);
        hBoxBtnInserirProposta.setAlignment(Pos.CENTER);

        boxLeft = new VBox(hBox1,hBox2,hBox3, hBoxBtnInserirProposta);
        boxLeft.setSpacing(30);
        boxLeft.setAlignment(Pos.CENTER);
        VBox.setMargin(hBox1, new Insets(40,0,0,0));

//        boxLeft = new VBox(hBoxID, vRamos,vTitulo);
//        boxRight = new VBox(vDocente, vEntidade, vAluno);
//        boxLeft.setSpacing(40);
//        boxRight.setSpacing(40);

    }

    private void preparaTable() {
        Consumer<Proposta> consumerEdit = this::updateEditFields;
        tableView = new TablePropostas(model, consumerEdit);
    }

    private void updateEditFields(Proposta proposta) {
        visible = true;
        update();
        hBoxBtnInserirProposta.getChildren().clear();
        hBoxBtnInserirProposta.getChildren().add(btnEditProposta);
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
        MenuVertical menu = new MenuVertical(btnGestaoPropostas, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
        setLeft(menu);
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
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
            }
        });

    }
    private void update() {
        hBoxInput.setVisible(visible);
        clearFieldsInput();
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_PROPOSTAS);
    }

    private void clearFieldsInput() {
        changeEditables();
        tFId.setText("");
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
}
