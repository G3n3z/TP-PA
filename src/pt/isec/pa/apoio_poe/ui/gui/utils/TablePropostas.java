package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;

import java.util.ArrayList;
import java.util.List;
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
        TableColumn<Proposta, String> colRamo = new TableColumn<>("Ramos");
        colRamo.setCellValueFactory(propostaButtonCellDataFeatures -> {
            List<String> ramos = propostaButtonCellDataFeatures.getValue().getRamos();
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
        });
        TableColumn<Proposta, String> coltitulo = new TableColumn<>("Titulo");
        coltitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Proposta, String> colDocente = new TableColumn<>("Docente");
        colDocente.setCellValueFactory(propostaButtonCellDataFeatures -> {
            if(propostaButtonCellDataFeatures.getValue() instanceof Projeto p){
                return new ReadOnlyObjectWrapper<>(p.getEmailDocente());
            }
            return new ReadOnlyObjectWrapper<>("n/a");
        });
        TableColumn<Proposta, String> colEntidade = new TableColumn<>("Entidade");
        colEntidade.setCellValueFactory(propostaButtonCellDataFeatures -> {
            if(propostaButtonCellDataFeatures.getValue() instanceof Estagio e){
                return new ReadOnlyObjectWrapper<>(e.getEntidade());
            }
            return new ReadOnlyObjectWrapper<>("n/a");
        });
        TableColumn<Proposta, Long> colAluno = new TableColumn<>("N.Aluno");
        colAluno.setCellValueFactory(new PropertyValueFactory<>("numAluno"));





        setFixedCellSize(50);
        getStylesheets().add("css/table1.css");
        colRamo.setPrefWidth(130);
        coltitulo.setPrefWidth(250); colDocente.setPrefWidth(150); colEntidade.setPrefWidth(200); colAluno.setPrefWidth(100);
        getColumns().addAll(colId,colTipo,colRamo, coltitulo,colDocente, colEntidade,colAluno);
        setPrefHeight(400);
    }

    private void registerHandler() {
//        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {
//            update();
//        });
    }
    private void update() {
//        System.out.println("Update propostas" + model.getPropostas().size());
//        getItems().clear();
//        getItems().addAll(model.getPropostas());
    }

    public boolean addCols(TableColumn<Proposta,?> tableCol){
        getColumns().add(tableCol);
        return true;
    }
    public boolean addColButton(TableColumn<Proposta,Button> tableCol){
        getColumns().add(tableCol);
        return true;
    }

    public void setPrefWidth(String name, int width){
        for (TableColumn<Proposta, ?> column : getColumns()) {
            if(column.getText().equalsIgnoreCase(name)){
                column.setPrefWidth(width);
            }
        }
    }
    public void removeCols(String ...names){
        for (String n : names) {
            getColumns().removeIf(col -> col.getText().equals(n));
        }
    }
}
