package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Docente;

import java.util.function.Consumer;

public class TableDocentes extends TableView<Docente> {
    ModelManager model;
    Consumer<Docente> consumerEdit;
    public TableDocentes(ModelManager model, Consumer<Docente> consumerEdit) {
        this.model = model;
        this.consumerEdit = consumerEdit;
        createViews();
        registerHandler();
        update();
    }

    private void createViews() {

        TableColumn<Docente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Docente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

       colEmail.setPrefWidth(200); colNome.setPrefWidth(150);

        TableColumn<Docente, Button> colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(docenteButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                System.out.println(docenteButtonCellDataFeatures.getValue());
                consumerEdit.accept(docenteButtonCellDataFeatures.getValue());
            });
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        TableColumn<Docente, Button> colButton = new TableColumn<>("Remover");
        colButton.setCellValueFactory(docenteButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                System.out.println(docenteButtonCellDataFeatures.getValue());
                model.removeDocente(docenteButtonCellDataFeatures.getValue().getEmail());
            });
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);
        setFixedCellSize(50);
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colEmail,colNome, colEditar,colButton);
        setPrefHeight(400);
    }

    private void registerHandler() {
        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> {
            update();
        });
    }
    private void update() {
        System.out.println("Update docentes" + model.getDocentes().size());
        getItems().clear();
        getItems().addAll(model.getDocentes());
    }
}
