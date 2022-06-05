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
    public TableDocentes(ModelManager model) {
        this.model = model;
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


        setFixedCellSize(50);
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colEmail,colNome);
        setPrefHeight(400);
    }
    public boolean addCols(TableColumn<Docente,?> tableCol){
        getColumns().add(tableCol);
        return true;
    }
    public boolean addColButton(TableColumn<Docente,Button> tableCol){
        getColumns().add(tableCol);
        return true;
    }
    private void registerHandler() {
//        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> {
//            update();
//        });
    }
    private void update() {
//        System.out.println("Update docentes" + model.getDocentes().size());
//        getItems().clear();
//        getItems().addAll(model.getDocentes());
    }
    public void setPrefWidth(String name, int width){
        for (TableColumn<Docente, ?> column : getColumns()) {
            if(column.getText().equalsIgnoreCase(name)){
                column.setPrefWidth(width);
            }
        }
    }
}
