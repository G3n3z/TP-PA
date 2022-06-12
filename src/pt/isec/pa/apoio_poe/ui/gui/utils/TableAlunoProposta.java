package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

import java.util.List;

public class TableAlunoProposta extends TableView<Aluno> {
    ModelManager modelManager;
    EnumState state;
    public TableAlunoProposta(ModelManager modelManager, EnumState estado) {
        this.modelManager = modelManager;
        state = estado;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        TableColumn<Aluno, Long> colnumEstudante = new TableColumn<>("Num.Aluno");
        colnumEstudante.setCellValueFactory(new PropertyValueFactory<>("numeroEstudante"));
        TableColumn<Aluno, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Aluno, String> colIdProposta = new TableColumn<>("ID");
        colIdProposta.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getId());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(alunoStringCellDataFeatures -> {
            if(alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getTipo());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colRamo = new TableColumn<>("Ramo");
        colRamo.setCellValueFactory( alunoStringCellDataFeatures -> {
            if(alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                List<String> ramos = alunoStringCellDataFeatures.getValue().getProposta().getRamos();
                if(ramos == null){
                    return new ReadOnlyObjectWrapper<>("n/a");
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < ramos.size(); i++) {
                    sb.append(ramos.get(i));
                    if(i != ramos.size()-1){
                        sb.append(", ");
                    }
                }
                return new ReadOnlyObjectWrapper<>(sb.toString());
            }
            return new ReadOnlyObjectWrapper<>("");

        });
        TableColumn<Aluno, String> colTitulo = new TableColumn<>("Titulo");
        colTitulo.setCellValueFactory( alunoStringCellDataFeatures -> {
            if(alunoStringCellDataFeatures.getValue().temPropostaConfirmada()){
                return new ReadOnlyObjectWrapper<>(alunoStringCellDataFeatures.getValue().getProposta().getTitulo());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colnumEstudante,colNome,colIdProposta, colTipo,colRamo,colTitulo);
        setFixedCellSize(50);
        setPrefHeight(400);
        colnumEstudante.setPrefWidth(150);
        colNome.setPrefWidth(150);
        colRamo.setPrefWidth(150);
        colTitulo.setPrefWidth(250);

    }

    private void registerHandlers() {
//        modelManager.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS , evt -> update());
//        modelManager.addPropertyChangeListener(ModelManager.PROP_ALUNOS , evt -> update());
    }
    public void update() {

//        if(modelManager.getState() == state) {
//            getItems().clear();
//            getItems().addAll(modelManager.getAlunosComPropostaConfirmada());
//
//        }
    }
    public void removeCols(String ...names){
        for (String n : names) {
            getColumns().removeIf(col -> col.getText().equals(n));
        }
    }
    public boolean addCols(TableColumn<Aluno,String> tableCol){
        getColumns().add(tableCol);
        return true;
    }
    public boolean addColButton(TableColumn<Aluno, ?> tableCol){
        getColumns().add(tableCol);
        return true;
    }
    public void setPrefWidth(String name, int width){
        for (TableColumn<Aluno, ?> column : getColumns()) {
            if(column.getText().equalsIgnoreCase(name)){
                column.setPrefWidth(width);
            }
        }
    }
}
