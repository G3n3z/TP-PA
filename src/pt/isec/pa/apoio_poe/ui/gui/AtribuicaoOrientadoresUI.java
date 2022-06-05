package pt.isec.pa.apoio_poe.ui.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TablePropostas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AtribuicaoOrientadoresUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnAtribuirOrientadores, btnAtribuirAutomatico, btnGestaoOrientadores, btnExportarCSV, btnListaDeOrientadores, btnRecuar, btnFecharEAvancar;
    MenuVertical menu;
    TablePropostas tablePropostas;
    List<Node> nodesShow;
    VBox center, vBoxTable;
    public AtribuicaoOrientadoresUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaMenu();
        preparaTabela();
        nodesShow = new ArrayList<>();
        nodesShow.add(vBoxTable);
        center = new VBox();
        setCenter(center);
    }

    private void preparaTabela() {
        tablePropostas = new TablePropostas(model, null);
        tablePropostas.removeCols("Docente", "Entidade", "N.Aluno");
        TableColumn<Proposta, String> colEmailDocente = new TableColumn<>("Email Orientador");
        colEmailDocente.setCellValueFactory(propostaStringCellDataFeatures -> {
            Docente orientador = propostaStringCellDataFeatures.getValue().getOrientador();
            if(orientador == null){
                return new ReadOnlyObjectWrapper<>("");
            }
            return new ReadOnlyObjectWrapper<>(orientador.getEmail());
        });
        TableColumn<Proposta, String> colNome = new TableColumn<>("Nome Orientador");
        colNome.setCellValueFactory(propostaStringCellDataFeatures -> {
            Docente orientador = propostaStringCellDataFeatures.getValue().getOrientador();
            if(orientador == null){
                return new ReadOnlyObjectWrapper<>("");
            }
            return new ReadOnlyObjectWrapper<>(orientador.getNome());
        });
        tablePropostas.addCols(colEmailDocente);
        tablePropostas.addCols(colNome);

        Label lTable = new Label("Propostas com Orientador");
        lTable.setFont(new Font(18));
        HBox titleTable = new HBox(lTable);
        titleTable.setAlignment(Pos.CENTER);
        vBoxTable = new VBox(titleTable,tablePropostas);
        VBox.setMargin(titleTable, new Insets(50,0,30,0));
    }

    private void preparaMenu() {
        btnAtribuirOrientadores = new ButtonMenu("Orientadores");
        btnAtribuirAutomatico = new ButtonMenu("Atribuicao Automática");
        btnGestaoOrientadores = new ButtonMenu("Gestão Orientadores");
        btnExportarCSV = new ButtonMenu("Exportar Para CSV");
        btnListaDeOrientadores =new ButtonMenu("Listas de Orientadores");
        btnRecuar = new ButtonMenu("Recuar Fase");
        btnFecharEAvancar = new ButtonMenu("Fechar/Avançar Fase");
        menu = new MenuVertical(btnAtribuirOrientadores,btnAtribuirAutomatico,btnGestaoOrientadores,btnExportarCSV,btnListaDeOrientadores,btnRecuar,btnFecharEAvancar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS , evt -> atualizaTabela());
        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES , evt -> atualizaTabela());
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS , evt -> atualizaTabela());
        btnAtribuirOrientadores.setOnAction(actionEvent -> {
            nodesShow.clear();
            nodesShow.add(vBoxTable);
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
        btnRecuar.setOnAction(actionEvent -> model.recuarFase());
        btnFecharEAvancar.setOnAction(actionEvent -> {
            model.fecharFase();
        });
        btnAtribuirAutomatico.setOnAction(actionEvent -> {
            model.associacaoAutomaticaDeDocentesAPropostas();
        });
    }

    private void atualizaTabela() {
        if(model != null && model.getState() == EnumState.ATRIBUICAO_ORIENTADORES){
            tablePropostas.getItems().clear();
            tablePropostas.getItems().addAll(model.getPropostasComOrientador());
        }
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.ATRIBUICAO_ORIENTADORES);
        atualizaTabela();
        center.getChildren().clear();
        center.getChildren().addAll(nodesShow);
    }


}
