package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AtribuicaoPropostasUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnAtribuicaoPropostas, btnAtribuirAutomaticoAuto, btnAtribuirAutomatico, btnGestao,btnExportarCSV, btnObtencoesAlunos, btnObtencoesFiltros, btnFechar, btnRecuar, btnAvancar;
    MenuVertical menu;
    TableAlunoProposta tableAlunoProposta;
    VBox center, vBoxTableAlunoProposta, container;
    Label title, numAlunosComPropAtribuida, numAlunosSemPropAtribuida, numPropNaoAtribuidas;
    List<Node> nodesShow;
    ObtencaoAlunoFaseAtribuicao obtencaoAlunoFaseAtribuicao;
    ObtencaoPropostaFiltrosFaseAtribuicao obtencaoPropostaFiltrosFaseAtribuicao;
    Integer nACPA = 0, nASPA = 0, nPNA = 0;
    boolean isClosed = false;
    public AtribuicaoPropostasUI(ModelManager model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        prepararMenu();
        preparaTabela();
        preparaListaAlunos();
        preparaListaPropostas();

        title = new Label("Atribuição de Propostas");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(25,0,25,0));
        titulo.setPrefHeight(50);
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.CENTER);

        nodesShow = new ArrayList<>();
        nodesShow.add(vBoxTableAlunoProposta);

        center = new VBox();
        ScrollPane scrollPane = new ScrollPane(center);
        scrollPane.setFitToWidth(true);
        scrollPane.setMinHeight(645);

        numAlunosComPropAtribuida = new Label();
        numAlunosSemPropAtribuida = new Label();
        numPropNaoAtribuidas = new Label();

        HBox statsFooter = new HBox();
        statsFooter.getChildren().addAll(numAlunosComPropAtribuida,numAlunosSemPropAtribuida,numPropNaoAtribuidas);
        statsFooter.setAlignment(Pos.BASELINE_CENTER);
        statsFooter.setSpacing(20.0);
        statsFooter.setPadding(new Insets(25));
        statsFooter.setPrefHeight(50);
        statsFooter.setBackground(new Background(new BackgroundFill(Color.web("#37304a"),CornerRadii.EMPTY,Insets.EMPTY)));


        container = new VBox(titulo,scrollPane,statsFooter);

        setCenter(container);
    }

    private void formatLabelFooter(Label label){
        label.setFont(new Font(14));
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold");

    }
    public void atualizaStats(){

        if(model.getState() != EnumState.ATRIBUICAO_PROPOSTAS){
            return;
        }
        //nACPA = model.getAlunosComPropostaConfirmada().size();
        numAlunosComPropAtribuida.setText("Alunos C/ Propostas Atribuída: "+nACPA);
        //nASPA = model.getAlunosSemPropostaConfirmada().size();
        numAlunosSemPropAtribuida.setText("Alunos S/ Propostas Atribuída: "+nASPA);
        nPNA = model.getPropostasWithFilters(3).size();
        numPropNaoAtribuidas.setText("Propostas não Atribuídas: "+nPNA);

        formatLabelFooter(numAlunosComPropAtribuida);
        formatLabelFooter(numAlunosSemPropAtribuida);
        formatLabelFooter(numPropNaoAtribuidas);

    }

    private void preparaListaPropostas() {
        obtencaoPropostaFiltrosFaseAtribuicao = new ObtencaoPropostaFiltrosFaseAtribuicao(model);
        obtencaoPropostaFiltrosFaseAtribuicao.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
    }

    private void preparaTabela() {
        tableAlunoProposta = new TableAlunoProposta(model, EnumState.ATRIBUICAO_PROPOSTAS);
        vBoxTableAlunoProposta = new VBox(tableAlunoProposta);

    }

    private void prepararMenu() {
        btnAtribuicaoPropostas = new ButtonMenu("Atribuição de Propostas");
        btnAtribuirAutomaticoAuto = new ButtonMenu("Atribuicao AutoPropostas");
        btnAtribuirAutomatico = new ButtonMenu("Atribuicao Autommatica");
        btnGestao = new ButtonMenu("Gestão de Propostas");
        btnExportarCSV = new ButtonMenu("Exportar CSV");
        btnObtencoesAlunos = new ButtonMenu("Lista de Alunos");
        btnObtencoesFiltros = new ButtonMenu("Lista de Propostas");
        btnFechar = new ButtonMenu("Fechar Fase");
        btnRecuar = new ButtonMenu("Recuar Fase");
        btnAvancar = new ButtonMenu("Avançar Fase");
        menu = new MenuVertical(btnAtribuicaoPropostas,btnAtribuirAutomaticoAuto,btnExportarCSV,btnObtencoesAlunos,btnObtencoesFiltros,btnRecuar,btnAvancar);
        setLeft(menu);
    }

    private void preparaListaAlunos() {
        obtencaoAlunoFaseAtribuicao = new ObtencaoAlunoFaseAtribuicao(model);
        obtencaoAlunoFaseAtribuicao.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_CLOSE_STATE, evt -> updateClose());
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS , evt -> {
            atualizaTabela();
            atualizaStats();
        });
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS , evt -> {
            atualizaTabela();
            atualizaStats();
        });
        model.addPropertyChangeListener(ModelManager.PROP_RESOLVIDO , evt -> {
            atribuicoesAutomaticasSemAtribuicaoDefinida();
            atualizaStats();
        });
        btnAvancar.setOnAction(actionEvent -> {
            model.avancarFase();
        });
        btnFechar.setOnAction(actionEvent -> {
            ErrorCode e = model.fecharFase();
            System.out.println(e);
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas no Fecho da Fase", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
        btnRecuar.setOnAction(actionEvent -> {
            model.recuarFase();
        });
        btnGestao.setOnAction(actionEvent -> {
            model.gestaoManualAtribuicoes();
        });
        btnAtribuirAutomaticoAuto.setOnAction(actionEvent -> {
            model.atribuicoesAutomaticas();
        });
        btnAtribuicaoPropostas.setOnAction(actionEvent -> {
            nodesShow.clear();
            nodesShow.add(tableAlunoProposta);
            update();
        });
        btnAtribuirAutomatico.setOnAction(actionEvent -> {
            atribuicoesAutomaticasSemAtribuicaoDefinida();
        });
        btnExportarCSV.setOnAction(actionEvent -> {
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
            model.exportCSV(f.getAbsolutePath());
        });
        btnObtencoesAlunos.setOnAction(actionEvent -> {
            nodesShow.clear();
            nodesShow.add(obtencaoAlunoFaseAtribuicao);
            update();
        });
        btnObtencoesFiltros.setOnAction(actionEvent -> {
            nodesShow.clear();
            nodesShow.add(obtencaoPropostaFiltrosFaseAtribuicao);
            update();
        });

    }



    private void atribuicoesAutomaticasSemAtribuicaoDefinida() {
        try {
            model.atribuicoesAutomaticasSemAtribuicaoDefinida();
        } catch (ConflitoAtribuicaoAutomaticaException e) {
            model.conflitoAtribuicaoCandidatura();
        }
    }


    private void update(){
        center.getChildren().clear();
        //closedFase();
        for (Node node : nodesShow) {
            center.getChildren().add(node);
        }
        this.setVisible(model != null && model.getState() == EnumState.ATRIBUICAO_PROPOSTAS);
        atualizaTabela();
    }


    private void atualizaTabela() {
        if(model.getState() == EnumState.ATRIBUICAO_PROPOSTAS) {
            tableAlunoProposta.getItems().clear();
            tableAlunoProposta.getItems().addAll(model.getAlunosComPropostaConfirmada());
            obtencaoAlunoFaseAtribuicao.updateTabelas();
        }
    }

    private void updateClose() {
        if(model.getState() == EnumState.ATRIBUICAO_PROPOSTAS){
            System.out.println("");
        }
        if(model.getCloseState(EnumState.ATRIBUICAO_PROPOSTAS)){
            menu.getChildren().removeAll(btnAtribuirAutomatico, btnGestao, btnFechar, btnAtribuirAutomaticoAuto);
            tableAlunoProposta.removeCols("Editar", "Remover");
            nodesShow.clear();
            nodesShow.add(tableAlunoProposta);
        } else if (!model.getCloseState(EnumState.OPCOES_CANDIDATURA)) {
            menu = new MenuVertical(btnAtribuicaoPropostas,btnAtribuirAutomaticoAuto,btnExportarCSV,btnObtencoesAlunos,
                    btnObtencoesFiltros,btnRecuar,btnAvancar);
            setLeft(menu);
        } else if (model.getCloseState(EnumState.OPCOES_CANDIDATURA)) {
            menu = new MenuVertical(btnAtribuicaoPropostas,btnAtribuirAutomaticoAuto,btnAtribuirAutomatico,btnGestao,btnExportarCSV,btnObtencoesAlunos,
                    btnObtencoesFiltros,btnFechar,btnRecuar,btnAvancar);
            setLeft(menu);
        }
        update();

    }

}
