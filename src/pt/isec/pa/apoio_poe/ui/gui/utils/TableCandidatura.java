package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Candidatura;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TableCandidatura extends TableView<Candidatura> {
    ModelManager modelManager;
    Consumer<Candidatura> consumerEdit;

    public TableCandidatura(ModelManager modelManager, Consumer<Candidatura> consumerEdit) {
        this.modelManager = modelManager;
        this.consumerEdit = consumerEdit;
        createViews();
        registerHandlers();
        update();
    }

    public void setConsumerEdit(Consumer<Candidatura> consumerEdit) {
        this.consumerEdit = consumerEdit;
    }

    private void createViews() {
        TableColumn<Candidatura, Long> colAluno= new TableColumn<>("N.Aluno");
        colAluno.setCellValueFactory(new PropertyValueFactory<>("numAluno"));
        TableColumn<Candidatura, String> colPropostas= new TableColumn<>("Propostas");
        colPropostas.setCellValueFactory(candidaturaLongCellDataFeatures -> {
            StringBuilder sb = new StringBuilder();
            List<String> ids = candidaturaLongCellDataFeatures.getValue().getIdProposta();
            for (int i = 0; i < ids.size(); i++) {
                sb.append(ids.get(i));
                if(i < ids.size()-1){
                    sb.append(",");
                }
            }
            if(ids.size() == 0){
                return new ReadOnlyObjectWrapper<>("n/a");
            }
            return new ReadOnlyObjectWrapper<>(sb.toString());
        } );
        TableColumn<Candidatura, Button> colEdit= new TableColumn<>("Editar");
        colEdit.setCellValueFactory(candidaturaLongCellDataFeatures -> {
            Button edit = new Button("Editar");
            edit.setOnAction(actionEvent -> {
                consumerEdit.accept(candidaturaLongCellDataFeatures.getValue());
            });
            return new ReadOnlyObjectWrapper<>(edit);
        } );
        TableColumn<Candidatura, Button> colRemove= new TableColumn<>("Remover");
        colRemove.setCellValueFactory(candidaturaLongCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                modelManager.removeCandidatura(candidaturaLongCellDataFeatures);
            });
            return new ReadOnlyObjectWrapper<>(remover);
        } );
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colAluno,colPropostas, colEdit,colRemove);
        setFixedCellSize(50);
        setPrefHeight(400);
    }

    private void registerHandlers() {
        modelManager.addPropertyChangeListener(ModelManager.PROP_CANDIDATURAS, evt -> {update();});

    }
    private void update() {
        System.out.println("Update" + modelManager.getCandidaturas().size());
        getItems().clear();
        getItems().addAll(modelManager.getCandidaturas());
    }

}
