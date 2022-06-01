package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.ui.gui.GestaoAlunosUI;

import java.util.function.Consumer;

public class TableAlunos extends TableView<Aluno> {
    ModelManager model;
    Consumer<Aluno> consumerEdit;
    public TableAlunos(ModelManager model, Consumer<Aluno> consumerEdit) {
        this.model = model;
        this.consumerEdit = consumerEdit;
        createViews();
        registerHandler();
        update();
    }

    private void createViews() {
        TableColumn<Aluno, Long> colnumEstudante = new TableColumn<>("Num.Aluno");
        colnumEstudante.setCellValueFactory(new PropertyValueFactory<>("numeroEstudante"));
        TableColumn<Aluno, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Aluno, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Aluno, String> colCurso = new TableColumn<>("Curso");
        colCurso.setCellValueFactory(new PropertyValueFactory<>("siglaCurso"));
        TableColumn<Aluno, String> colRamo = new TableColumn<>("Ramo");
        colRamo.setCellValueFactory(new PropertyValueFactory<>("siglaRamo"));
        TableColumn<Aluno, Double> colClass = new TableColumn<>("Classificacao");
        colClass.setCellValueFactory(new PropertyValueFactory<>("classificacao"));
        TableColumn<Aluno, Boolean> colPossibilidade = new TableColumn<>("Possibilidade Estagio");
        colPossibilidade.setCellValueFactory(new PropertyValueFactory<>("possibilidade"));
        colnumEstudante.setPrefWidth(120); colEmail.setPrefWidth(200); colNome.setPrefWidth(150);
        colCurso.setPrefWidth(120); colRamo.setPrefWidth(120); colClass.setPrefWidth(120); colPossibilidade.setPrefWidth(120);
        TableColumn<Aluno, Button> colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(alunoButtonCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                System.out.println(alunoButtonCellDataFeatures.getValue());
                consumerEdit.accept(alunoButtonCellDataFeatures.getValue());
            });
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(120);
        TableColumn<Aluno, Button> colButton = new TableColumn<>("Remover");
        colButton.setCellValueFactory(alunoButtonCellDataFeatures -> {
            Button remover = new Button("Remover");
            remover.setOnAction(actionEvent -> {
                System.out.println(alunoButtonCellDataFeatures.getValue());
                model.removeAluno(alunoButtonCellDataFeatures.getValue().getNumeroAluno());
            });
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colButton.setPrefWidth(120);
        setFixedCellSize(50);
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colnumEstudante,colEmail,colNome, colCurso, colRamo, colClass, colPossibilidade, colEditar,colButton);
        setPrefHeight(400);
    }

    private void registerHandler() {
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {
            update();
        });
    }
    private void update() {
        System.out.println("Update" + model.getAlunos().size());
        getItems().clear();
        getItems().addAll(model.getAlunos());
    }
}
