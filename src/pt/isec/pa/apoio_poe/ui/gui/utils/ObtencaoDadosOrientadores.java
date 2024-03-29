package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.tables.TableAlunos;
import pt.isec.pa.apoio_poe.ui.gui.tables.TableDocentes;

import java.util.Map;

public class ObtencaoDadosOrientadores extends VBox {

    ModelManager model;
    TableAlunos tableAlunosComOrientador, tableAlunosSemOrientador;
    HBox footer;
    Label lFooter;
    VBox box, vBoxTableOrientacoes;
    TableDocentes tvDocentes ;

    public ObtencaoDadosOrientadores(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        preparaTabelaAtribuidaComOrientador();
        preparaTabelaAtribuidaSemOrientador();
        preparaTabelaOrientadores();
        preparaLabelDados();
        Label lAlunosComOrientador = new Label("Alunos com Proposta Atribuida e Orientador");
        lAlunosComOrientador.setFont(new Font(18));
        HBox hBoxAlunosComOrientador = new HBox(lAlunosComOrientador);
        hBoxAlunosComOrientador.setAlignment(Pos.CENTER);

        Label lAlunosSemOrientador = new Label("Alunos com Proposta Atribuida Sem Orientador");
        lAlunosSemOrientador.setFont(new Font(18));
        HBox hBoxAlunosSemOrientador = new HBox(lAlunosSemOrientador);
        hBoxAlunosSemOrientador.setAlignment(Pos.CENTER);

        box = new VBox();
        box.getChildren().addAll(hBoxAlunosComOrientador,tableAlunosComOrientador, hBoxAlunosSemOrientador, tableAlunosSemOrientador, vBoxTableOrientacoes);
        VBox.setMargin(hBoxAlunosComOrientador, new Insets(50,0,30,0));
        VBox.setMargin(hBoxAlunosSemOrientador, new Insets(50,0,30,0));
        VBox.setMargin(tableAlunosSemOrientador, new Insets(0,0,100,0));
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        getChildren().add(scrollPane);

    }

    private void preparaTabelaAtribuidaComOrientador() {
        tableAlunosComOrientador = new TableAlunos(model);
        tableAlunosComOrientador.removeCols("Possibilidade Estagio", "Classificacao");
        TableColumn<Aluno, String> colIDProposta = new TableColumn<>("ID");
        colIDProposta.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(!alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>("");
            }
            return  new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getId());
        });
        TableColumn<Aluno, String> colDocente = new TableColumn<>("Orientador");
        colDocente.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(!alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>("");
            }
            if(!alunoStringCellDataFeatures.getValue().getProposta().temDocenteOrientador()){
                return new ReadOnlyObjectWrapper<>("");
            }
            return  new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getOrientador().getNome());
        });
        TableColumn<Aluno, String> colEmailDocente = new TableColumn<>("Email Orientador");
        colEmailDocente.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(!alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>("");
            }
            if(!alunoStringCellDataFeatures.getValue().getProposta().temDocenteOrientador()){
                return new ReadOnlyObjectWrapper<>("");
            }
            return  new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getOrientador().getEmail());
        });
        tableAlunosComOrientador.addCols(colIDProposta);
        tableAlunosComOrientador.addCols(colEmailDocente);
        tableAlunosComOrientador.addCols(colDocente);

    }

    private void preparaTabelaAtribuidaSemOrientador() {

        tableAlunosSemOrientador = new TableAlunos(model);
        tableAlunosSemOrientador.removeCols("Possibilidade Estagio", "Classificacao");
        TableColumn<Aluno, String> colIDProposta = new TableColumn<>("ID");
        colIDProposta.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(!alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>("");
            }
            return  new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getId());
        });
        tableAlunosSemOrientador.addCols(colIDProposta);

    }

    private void preparaTabelaOrientadores() {
        Label lTvDocentes= new Label("Numero de Orientações Por Docentes");
        lTvDocentes.setFont(new Font(18));
        tvDocentes = new TableDocentes(model);
        vBoxTableOrientacoes = new VBox(lTvDocentes,tvDocentes);
        VBox.setMargin(lTvDocentes, new Insets(20));
        vBoxTableOrientacoes.setAlignment(Pos.CENTER);
    }

    private void preparaLabelDados() {
        lFooter = new Label();

    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
    }

    private void update() {
        if(model != null && model.getState() == EnumState.ATRIBUICAO_ORIENTADORES){
            tableAlunosComOrientador.getItems().clear();
            tableAlunosComOrientador.getItems().addAll(model.getAlunosComPropostaConfirmadaEOrientador());
            tableAlunosSemOrientador.getItems().clear();
            tableAlunosSemOrientador.getItems().addAll(model.getAlunosComPropostaConfirmadaESemOrientador());
            tvDocentes.getItems().clear();
            Map<Docente, Integer> docentes = model.getDocentesPorOrientacoes();
            tvDocentes.removeCols("Número de Orientações");
            TableColumn<Docente, Integer> tcOrientacoes = new TableColumn<>("Número de Orientações");
            tcOrientacoes.setCellValueFactory(d-> new ReadOnlyObjectWrapper<>( docentes.get(d.getValue())));
            tvDocentes.addCols(tcOrientacoes);
            tvDocentes.getItems().addAll(docentes.keySet());
        }
    }


}
