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
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;
import pt.isec.pa.apoio_poe.ui.resource.CSSManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;



public class OpcoesCandidaturaUI extends BorderPane {

    ModelManager model;
    boolean isClosed = false;
    ButtonMenu btnCandidaturas, btnInserirCandidaturas, btnExportarCSV, btnRemoverAll, btnObtencoesAlunos, btnObtencoesFiltros, btnFechar, btnRecuar, btnAvancar;
    MenuVertical menu;
    List<Node> nodesVisibles;
    TableCandidatura tableView;
    TextField tfAluno, tfPropostas;
    HBox hBoxInsereCandidaturas, hBoxButtonInsereCand;
    Label propostasInseridas, title, numCandidaturas, numAutoProp, numAlunoComCand, numAlunoSemCand;
    VBox vBoxCamposInsercaoPropostas, vBoxInsereCandidaturas, camposCentro;
    Button btnInsereCandManual, btnInsereCandCSV, btnEditCandidatura,btnVoltarEdit;

    ObtencaoAlunoFaseCandidatura obtencaoAlunoFaseCandidatura;
    ObtencaoPropostaFiltrosFaseCandidatura obtencaoPropostaFiltrosFaseCandidatura;
    Integer nCandidaturas, nAP, nACC, nASC;
    TableColumn<Candidatura, Button> colRemove, colEdit;
    public OpcoesCandidaturaUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaMenu();
        preparaTabela();
        preparaCamposInserir();
        preparaObtencoes();

        title = new Label("Opções de Candidatura");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(25,0,25,0));
        titulo.setPrefHeight(50);
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);

        nodesVisibles = new ArrayList<>();
        nodesVisibles.add(tableView);
        //nodesVisibles.add(obtencaoAlunoFaseCandidatura);
        camposCentro = new VBox();
        ScrollPane scrollPane = new ScrollPane(camposCentro);
        scrollPane.setFitToWidth(true);

        //scrollPane.setMinHeight(645);
        numCandidaturas = new Label();
        numAutoProp = new Label();
        numAlunoComCand = new Label();
        numAlunoSemCand = new Label();

        HBox statsFooter = new HBox();
        statsFooter.getChildren().addAll(numCandidaturas,numAutoProp,numAlunoComCand,numAlunoSemCand);
        statsFooter.setAlignment(Pos.BASELINE_CENTER);
        statsFooter.setSpacing(20.0);
        statsFooter.setPadding(new Insets(25));
        statsFooter.setPrefHeight(50);
        statsFooter.setBackground(new Background(new BackgroundFill(Color.web("#37304a"),CornerRadii.EMPTY,Insets.EMPTY)));

        BorderPane container = new BorderPane();
        container.setTop(titulo);
        container.setCenter(scrollPane);
        container.setBottom(statsFooter);

        setCenter(container);
    }

    private void formatLabelFooter(Label label){
        label.setFont(new Font(14));
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold");

    }

    private void preparaTabela() {

        tableView = new TableCandidatura(model, null);
        colEdit= new TableColumn<>("Editar");
        colEdit.setCellValueFactory(candidaturaLongCellDataFeatures -> {
            Button edit = new Button("Editar");
            edit.setOnAction(actionEvent -> {
                hBoxButtonInsereCand.getChildren().clear();
                hBoxButtonInsereCand.getChildren().addAll(btnEditCandidatura, btnVoltarEdit);
                tfAluno.setDisable(true);
                tfAluno.setText(Long.toString(candidaturaLongCellDataFeatures.getValue().getNumAluno()));
                List<String> id = candidaturaLongCellDataFeatures.getValue().getIdProposta();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < id.size(); i++) {
                    sb.append(id.get(i));
                    if(i != id.size()-1){
                        sb.append(",");
                    }
                }
                tfPropostas.setText(sb.toString());
                if (!nodesVisibles.contains(hBoxInsereCandidaturas)) {
                    nodesVisibles.add(hBoxInsereCandidaturas);
                }
                update();

            });
            edit.setId("button_editar");
            CSSManager.applyCSS(edit,"button_editar.css");
            return new ReadOnlyObjectWrapper<>(edit);
        } );
        colRemove= new TableColumn<>("Remover");
        colRemove.setCellValueFactory(candidaturaLongCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                model.removeCandidatura(candidaturaLongCellDataFeatures);
            });
            remover.setId("button_delete");
            CSSManager.applyCSS(remover,"buttonDelete.css");
            return new ReadOnlyObjectWrapper<>(remover);
        } );
        tableView.addCols(colEdit);
        tableView.addCols(colRemove);
        colEdit.setPrefWidth(150);
        colRemove.setPrefWidth(150);
        //tableView.setMaxHeight(300);
    }

    private void preparaObtencoes() {

        obtencaoAlunoFaseCandidatura = new ObtencaoAlunoFaseCandidatura(model);
        obtencaoAlunoFaseCandidatura.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
    }

    private void preparaCamposInserir() {
        Label lAluno = new Label("Aluno");
        tfAluno = new TextField();
        tfAluno.setMinWidth(300);
        propostasInseridas = new Label("Propostas: ");
        tfPropostas = new TextField();
        tfPropostas.setMinWidth(300);
        Label help = new Label("Introduzir propostas separadas por ,");
        btnInsereCandManual = new Button("Insere Candidatura");
        btnInsereCandCSV = new Button("Importar CSV");
        btnInsereCandManual.setPrefWidth(150);
        btnInsereCandCSV.setPrefWidth(150);
        btnInsereCandManual.setId("button_submit");
        CSSManager.applyCSS(btnInsereCandManual, "button_SUBMIT.css");
        btnInsereCandCSV.setId("button_submit");
        CSSManager.applyCSS(btnInsereCandCSV, "button_SUBMIT.css");
        /*Butoes edicao*/
        btnEditCandidatura = new Button("Atualizar");
        btnVoltarEdit = new Button("Voltar");
        btnEditCandidatura.setPrefWidth(150);
        btnVoltarEdit.setPrefWidth(150);
        btnEditCandidatura.setId("button_submit");
        CSSManager.applyCSS(btnEditCandidatura, "button_SUBMIT.css");
        btnVoltarEdit.setId("button_submit");
        CSSManager.applyCSS(btnVoltarEdit, "button_SUBMIT.css");

        hBoxButtonInsereCand = new HBox(btnInsereCandManual, btnInsereCandCSV);
        hBoxButtonInsereCand.setAlignment(Pos.CENTER);
        hBoxButtonInsereCand.setSpacing(40);
        VBox vAluno =new VBox(lAluno, tfAluno);

        vBoxCamposInsercaoPropostas = new VBox(propostasInseridas, tfPropostas, help);
        vBoxInsereCandidaturas = new VBox(vAluno,vBoxCamposInsercaoPropostas, hBoxButtonInsereCand);
        vBoxInsereCandidaturas.setSpacing(20);

        hBoxInsereCandidaturas = new HBox(vBoxInsereCandidaturas);
        hBoxInsereCandidaturas.setAlignment(Pos.CENTER);
        hBoxInsereCandidaturas.setPadding(new Insets(25));
    }

    private void preparaMenu() {
        btnCandidaturas = new ButtonMenu("Candidaturas");
        btnInserirCandidaturas = new ButtonMenu("Inserir Candidaturas");
        btnExportarCSV = new ButtonMenu("Exportar CSV");
        btnRemoverAll= new ButtonMenu("Remover Todas");
        btnObtencoesAlunos = new ButtonMenu("Listas de Alunos");
        btnObtencoesFiltros = new ButtonMenu("Lista de Propostas");
        btnFechar = new ButtonMenu("Fechar Fase");
        btnRecuar = new ButtonMenu("Recuar Fase");
        btnAvancar = new ButtonMenu("Avançar Fase");
        menu = new MenuVertical(btnCandidaturas,btnInserirCandidaturas,btnExportarCSV,btnRemoverAll,btnObtencoesAlunos,btnObtencoesFiltros,btnFechar,btnRecuar,btnAvancar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
            atualizaStats();
        });
        model.addPropertyChangeListener(ModelManager.PROP_CANDIDATURAS, evt -> {
            atualizaTabela();
            atualizaStats();
        });
        model.addPropertyChangeListener(ModelManager.PROP_CLOSE_STATE, evt -> {
            updateClose();

        });
        btnAvancar.setOnAction(actionEvent -> {
            model.avancarFase();
        });
        btnFechar.setOnAction(actionEvent -> {
            ErrorCode e = model.fecharFase();
            System.out.println(e);
            if(e != ErrorCode.E0) {
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas no fecho da fase", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnRecuar.setOnAction(actionEvent -> {
            model.recuarFase();
        });
        btnInserirCandidaturas.setOnAction(actionEvent -> {
            if (!nodesVisibles.contains(hBoxInsereCandidaturas)) {
                nodesVisibles.add(hBoxInsereCandidaturas);
            }
            nodesVisibles.clear();
            nodesVisibles.add(tableView);
            nodesVisibles.add(hBoxInsereCandidaturas);

            update();
        });
        btnCandidaturas.setOnAction(actionEvent -> {
            nodesVisibles.clear();
            nodesVisibles.add(tableView);
            update();
        });
        btnExportarCSV.setOnAction(actionEvent -> {
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
            if(error != ErrorCode.E0) {
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas na Exportação dos dados das Candidaturas", MessageTranslate.translateErrorCode(error));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnInsereCandManual.setOnAction(actionEvent -> {
            List<String> ids;
            if (!checkFields())
                return;
            ids = getTextFields();
            System.out.println(ids.toString());
            System.out.println(model.insereCandidatura(tfAluno.getText(), ids));
        });
        btnInsereCandCSV.setOnAction(actionEvent -> {
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
                error = model.importCandidaturasCSV(f.getAbsolutePath());
            } catch (CollectionBaseException e) {
                System.out.println(e.getMessageOfExceptions());
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas na Importação dos dados das Candidaturas", e.getMessageOfExceptions());
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnEditCandidatura.setOnAction(actionEvent -> {
            if (!checkFields()) return;
            List<String> ids;
            if (!checkFields())
                return;
            ids = getTextFields();
            ErrorCode e = model.editCandidatura(tfAluno.getText(), ids);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("","Problemas na Introdução dos dados das Candidaturas", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
                return;
            }
            repoeBotaoInserir();
            clearFieldsEdit();
        });
        btnVoltarEdit.setOnAction(actionEvent -> {
            repoeBotaoInserir();
            clearFieldsEdit();
        });
        btnRemoverAll.setOnAction(actionEvent -> {
            model.removeAllCandidatura();
        });
        btnObtencoesAlunos.setOnAction(actionEvent -> {
            nodesVisibles.clear();
            nodesVisibles.add(obtencaoAlunoFaseCandidatura);
            update();
        });
        btnObtencoesFiltros.setOnAction(actionEvent -> {
            obtencaoPropostaFiltrosFaseCandidatura = new ObtencaoPropostaFiltrosFaseCandidatura(model);
            obtencaoPropostaFiltrosFaseCandidatura.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
            nodesVisibles.clear();
            nodesVisibles.add(obtencaoPropostaFiltrosFaseCandidatura);
            update();
        });

    }



    private List<String> getTextFields() {
        List<String> ids ;
        StringTokenizer st = new StringTokenizer(tfPropostas.getText(),",");
        ids = new ArrayList<>();
        while (st.hasMoreElements()){
            ids.add(st.nextToken());
        }
        return ids;
    }

    private boolean checkFields() {
        if(tfPropostas.getText().equals("")){
            return false;
        }
        if (tfAluno.getText().equals("")){
            return false;
        }

        return true;
    }

    private void clearFieldsEdit() {
        tfAluno.setDisable(false);
        tfAluno.setText("");
        tfPropostas.setText("");
    }

    private void repoeBotaoInserir() {
        hBoxButtonInsereCand.getChildren().clear();
        hBoxButtonInsereCand.getChildren().addAll(btnInsereCandManual, btnInsereCandCSV);
    }

    private void update() {
        camposCentro.getChildren().clear();
        //closedFase();
        for (Node nodesVisible : nodesVisibles) {
            camposCentro.getChildren().add(nodesVisible);
        }

        this.setVisible(model != null && model.getState() == EnumState.OPCOES_CANDIDATURA);
        atualizaTabela();
    }



    private void atualizaTabela(){

        System.out.println("Update Candidatura" + model.getCandidaturas().size());
        tableView.getItems().clear();
        tableView.getItems().addAll(model.getCandidaturas());
        obtencaoAlunoFaseCandidatura.updateTabelas();

    }

    private void updateClose() {
        if(model.getCloseState(EnumState.OPCOES_CANDIDATURA)){
            menu.getChildren().remove(btnInserirCandidaturas);
            menu.getChildren().remove(btnFechar);
            tableView.removeCols("Editar", "Remover");
            nodesVisibles.clear();
            nodesVisibles.add(tableView);
            update();
        }
        else {
            if(!menu.getChildren().contains(btnInserirCandidaturas)){
                menu.getChildren().add(1,btnInserirCandidaturas);
                menu.getChildren().add(6,btnFechar);
                tableView.addCols(colEdit);
                tableView.addCols(colRemove);
                nodesVisibles.clear();
                nodesVisibles.add(tableView);
                update();
            }
        }

    }


    public void atualizaStats(){
        if(model.getState() == EnumState.OPCOES_CANDIDATURA){
            nCandidaturas = model.getCandidaturas().size();
            numCandidaturas.setText("Candidaturas: " + nCandidaturas);
            nAP = model.getAlunosComAutoProposta().size();
            numAutoProp.setText("Alunos C/ Autoproposta: " + nAP);
            nACC = model.getAlunosComCandidatura().size();
            numAlunoComCand.setText("Alunos C/ Candidatura: " + nACC);
            nASC = model.getAlunosSemCandidatura().size();
            numAlunoSemCand.setText("Alunos S/ Candidatura: " + nASC);

            formatLabelFooter(numCandidaturas);
            formatLabelFooter(numAutoProp);
            formatLabelFooter(numAlunoComCand);
            formatLabelFooter(numAlunoSemCand);
        }
    }

}
