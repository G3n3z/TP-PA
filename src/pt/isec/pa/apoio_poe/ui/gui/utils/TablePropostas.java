package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TablePropostas extends TableView<Proposta> {
    ModelManager model;
    Consumer<Proposta> consumerEdit;
    public TablePropostas(ModelManager model, Consumer<Proposta> consumerEdit) {
        this.model = model;
        this.consumerEdit = consumerEdit;
        createViews();
        registerHandler();
        update();
    }

    private void createViews() {
        TableColumn<Proposta, String> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Proposta, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Proposta, ArrayList<String>> colRamo = new TableColumn<>("Ramos");
        colRamo.setCellValueFactory(new PropertyValueFactory<>("ramos"));
        TableColumn<Proposta, String> coltitulo = new TableColumn<>("Titulo");
        coltitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<Proposta, Long> colAluno = new TableColumn<>("N.Aluno");
        colAluno.setCellValueFactory(new PropertyValueFactory<>("numAluno"));


        TableColumn<Proposta, Button> colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(propostaButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                System.out.println(propostaButtonCellDataFeatures.getValue());
                consumerEdit.accept(propostaButtonCellDataFeatures.getValue());
            });
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        TableColumn<Proposta, Button> colButton = new TableColumn<>("Remover");
        colButton.setCellValueFactory(propostaButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                System.out.println(propostaButtonCellDataFeatures.getValue());
                model.removeProposta(propostaButtonCellDataFeatures.getValue().getNumAluno());
            });
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);
        setFixedCellSize(50);
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colId,colTipo,colRamo, coltitulo, colAluno);
        setPrefHeight(400);
    }

    private void registerHandler() {
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {
            update();
        });
    }
    private void update() {
        System.out.println("Update propostas" + model.getPropostas().size());
        getItems().clear();
        getItems().addAll(model.getPropostas());
    }
}
