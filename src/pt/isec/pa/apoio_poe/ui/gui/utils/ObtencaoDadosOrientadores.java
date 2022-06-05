package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.ModelManager;

public class ObtencaoDadosOrientadores extends ScrollPane {

    ModelManager model;
    TableAlunos tableAlunosComOrientador, tableAlunosSemOrientador;

    public ObtencaoDadosOrientadores(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        setFitToWidth(true);
        preparaTabelaAtribuidaComOrientador();
        preparaTabelaAtribuidaSemOrientador();
        preparaLabelDados();
        VBox box = new VBox();
    }

    private void preparaTabelaAtribuidaComOrientador() {
        tableAlunosComOrientador = new TableAlunos(model);
        tableAlunosComOrientador.removeCols("");
    }

    private void preparaTabelaAtribuidaSemOrientador() {
    }

    private void preparaLabelDados() {
    }

    private void registerHandlers() {
    }

    private void update() {
    }


}
