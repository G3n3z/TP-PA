package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.ModelManager;
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
    VBox center, vBoxTableAlunoProposta;
    List<Node> nodesShow;
    ObtencaoAlunoFaseAtribuicao obtencaoAlunoFaseAtribuicao;
    ObtencaoPropostaFiltrosFaseAtribuicao obtencaoPropostaFiltrosFaseAtribuicao;
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
        nodesShow = new ArrayList<>();
        nodesShow.add(vBoxTableAlunoProposta);

        center = new VBox();
        ScrollPane scrollPane = new ScrollPane(center);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

    }

    private void preparaListaPropostas() {
        obtencaoPropostaFiltrosFaseAtribuicao = new ObtencaoPropostaFiltrosFaseAtribuicao(model);
    }

    private void preparaTabela() {
        tableAlunoProposta = new TableAlunoProposta(model, EnumState.ATRIBUICAO_PROPOSTAS);
        Label ltitle = new Label("Propostas Atribuidas");
        ltitle.setFont(new Font(18));
        HBox title = new HBox(ltitle);
        title.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(30,0,20,0));
        vBoxTableAlunoProposta = new VBox(title,tableAlunoProposta);

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
        menu = new MenuVertical(btnAtribuicaoPropostas,btnAtribuirAutomaticoAuto,btnAtribuirAutomatico,btnGestao,btnExportarCSV,btnObtencoesAlunos,btnObtencoesFiltros,btnFechar,btnRecuar,btnAvancar);
        setLeft(menu);
    }

    private void preparaListaAlunos() {
        obtencaoAlunoFaseAtribuicao = new ObtencaoAlunoFaseAtribuicao(model);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS , evt -> atualizaTabela());
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS , evt -> atualizaTabela());
        model.addPropertyChangeListener(ModelManager.PROP_RESOLVIDO , evt -> atribuicoesAutomaticasSemAtribuicaoDefinida());
        btnAvancar.setOnAction(actionEvent -> {
            model.avancarFase();
        });
        btnFechar.setOnAction(actionEvent -> {
            System.out.println(model.fecharFase());
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


}
