package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;

public class TableAlunos extends TableView<Aluno> {
    ModelManager model;
    public TableAlunos(ModelManager model) {
        this.model = model;
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
        colnumEstudante.setPrefWidth(140); colEmail.setPrefWidth(220); colNome.setPrefWidth(180);
        colCurso.setPrefWidth(100); colRamo.setPrefWidth(100); colClass.setPrefWidth(100); colPossibilidade.setPrefWidth(120);

        setFixedCellSize(50);
        getStylesheets().add("css/table1.css");
        getColumns().addAll(colnumEstudante,colEmail,colNome, colCurso, colRamo, colClass, colPossibilidade);
        setHeight(400);
    }

    private void registerHandler() {
//        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {
//            update();
//        });
    }
    public void removeCols(String ...names){
        for (String n : names) {
            getColumns().removeIf(col -> col.getText().equals(n));
        }
    }
    public boolean addCols(TableColumn<Aluno,?> ...tableCol){
        getColumns().addAll(tableCol);
        return true;
    }
    public boolean addColButton(TableColumn<Aluno,Button> tableCol){
        getColumns().add(tableCol);
        return true;
    }
    private void update() {

    }
    public void setPrefWidth(String name, int width){
        for (TableColumn<Aluno, ?> column : getColumns()) {
            if(column.getText().equalsIgnoreCase(name)){
                column.setPrefWidth(width);
            }
        }
    }

}
