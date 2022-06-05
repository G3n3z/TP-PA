package pt.isec.pa.apoio_poe.ui.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.TableAlunos;
import pt.isec.pa.apoio_poe.ui.gui.utils.TablePropostas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConflitoAtribuicaoCandidaturaUI extends BorderPane {
    ModelManager model;
    TableAlunos tableAlunos;
    TablePropostas tablePropostas;
    public ConflitoAtribuicaoCandidaturaUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaTabelas();
        Label lPropostas = new Label("Proposta em Conflito");
        Label lAlunos = new Label("Alunos em Conflito");
        VBox box = new VBox(lPropostas,tableAlunos,lAlunos,tablePropostas);
        setCenter(box);

    }

    private void preparaTabelas() {
        tableAlunos = new TableAlunos(model);
        tablePropostas = new TablePropostas(model, null);
        TableColumn<Aluno, Button> colResolve = new TableColumn<>("Resolver");
        colResolve.setCellValueFactory(alunoButtonCellDataFeatures -> {
            Button button = new Button("Resolver");
            button.setOnAction(actionEvent -> {
                ErrorCode e = model.resolveConflito(alunoButtonCellDataFeatures.getValue().getNumeroAluno());
                if(e == ErrorCode.E0){
                    conflitoResolvido();
                }
            });
            return new ReadOnlyObjectWrapper<>(button);
        });
        tableAlunos.addColButton(colResolve);
    }

    private void conflitoResolvido() {
        model.recuarFase();
        model.conflitoResolvido();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA);
        if(model != null && model.getState() == EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA){
            Map<Proposta, ArrayList<Aluno>> proposta_aluno =  model.getConflito();
            List<Proposta> prop = new ArrayList<>();
            List<Aluno> alunos = new ArrayList<>();
            for(Map.Entry<Proposta, ArrayList<Aluno>> set : proposta_aluno.entrySet()){
                prop.add(set.getKey());
                alunos.addAll(set.getValue());
            }
            tablePropostas.getItems().clear();
            tablePropostas.getItems().addAll(prop);
            tableAlunos.getItems().clear();
            tableAlunos.getItems().addAll(alunos);
        }
    }
}
