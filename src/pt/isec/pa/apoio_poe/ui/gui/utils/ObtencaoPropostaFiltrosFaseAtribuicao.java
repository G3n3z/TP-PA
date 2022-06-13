package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

import java.util.ArrayList;
import java.util.List;

public class ObtencaoPropostaFiltrosFaseAtribuicao extends VBox {
    ModelManager model;
    TableView<Proposta> tableView;
    CheckBox ckAutoProp, ckPropDocentes, ckPropCandidaturas, ckPropSemCandidatura;
    HBox box2Filtros, box1Filtros;
    Button btnVisualizarComFiltros;
    List<CheckBox> filtros;
    public ObtencaoPropostaFiltrosFaseAtribuicao(ModelManager model) {
        this.model = model;
        createViews();
    }

    private void createViews() {

        preparaTabela();

        preparaFiltros();
        Label lP = new Label("Propostas");
        lP.setFont(new Font(18));
        HBox title = new HBox(lP);
        title.setAlignment(Pos.CENTER);
        btnVisualizarComFiltros = new Button("Visualizar");
        HBox hBoxBtnVisualizar = new HBox(btnVisualizarComFiltros);
        hBoxBtnVisualizar.setAlignment(Pos.CENTER);
        getChildren().addAll(lP, tableView, box1Filtros, box2Filtros, hBoxBtnVisualizar);
        VBox.setMargin(lP, new Insets(20,0,20,20));
        VBox.setMargin(tableView, new Insets(0,0,20,0));
        VBox.setMargin(box1Filtros, new Insets(0,0,20,0));
        box1Filtros.setSpacing(40);
        VBox.setMargin(box2Filtros, new Insets(0,0,20,0));
        box2Filtros.setSpacing(40);
        registerHandlers();
    }



    private void preparaFiltros() {
        ckAutoProp = new CheckBox("AutoPropostas de Alunos");
        ckAutoProp.setMinWidth(100);
        ckPropDocentes = new CheckBox("Propostas de Docentes");
        ckPropDocentes.setMinWidth(100);
        ckPropCandidaturas = new CheckBox("Propostas Disponiveis");
        ckPropCandidaturas.setMinWidth(100);
        ckPropSemCandidatura = new CheckBox("Propostas Atribuidas");
        ckPropSemCandidatura.setMinWidth(100);
        box1Filtros = new HBox(ckAutoProp,ckPropDocentes);
        box1Filtros.setAlignment(Pos.CENTER);
        box2Filtros = new HBox(ckPropCandidaturas,ckPropSemCandidatura);
        box2Filtros.setAlignment(Pos.CENTER);
        filtros = new ArrayList<>();
        filtros.addAll(List.of(ckAutoProp,ckPropDocentes,ckPropCandidaturas,ckPropSemCandidatura));
    }

    private void preparaTabela() {
        tableView = new TableView<>();
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
        tableView.getColumns().addAll(colId,colTipo,colRamo, coltitulo,colDocente, colEntidade,colAluno);
        tableView.getStylesheets().add("css/table1.css");
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {
            update();
        });
        btnVisualizarComFiltros.setOnAction(actionEvent -> {
            update();
        });
    }
    private void update(){
        int[] filtros = getFiltros();
        if(model.getState() == EnumState.ATRIBUICAO_PROPOSTAS){
            tableView.getItems().clear();
            tableView.getItems().addAll(model.getPropostasWithFilters(filtros));
        }
    }

    private int[] getFiltros() {
        int []opcoes = new int[filtros.size()];
        for (int o : opcoes) {
            o = 0;
        }
        for (int i = 0; i < filtros.size(); i++) {
            if(filtros.get(i).isSelected()){
                opcoes[i] = i+1;
            }

        }
        return opcoes;
    }
}
