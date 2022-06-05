package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.layout.*;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;

public class ConfigOptionsUI extends BorderPane {

    ModelManager model;
    VBox menu;
    ButtonMenu bGAlunos, bGDocentes,bGPropostas, bFechar, bAvancar;
    public ConfigOptionsUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandler();
        update();
    }
    private void createViews() {
        bGAlunos = new ButtonMenu("Gestao de Alunos");
        bGDocentes = new ButtonMenu("Gestao de Docentes");
        bGPropostas = new ButtonMenu("Gestao de Propostas");
        bFechar = new ButtonMenu("Fechar Fase");
        bAvancar = new ButtonMenu("Avançar Fase");
        menu = new MenuVertical(bGAlunos, bGDocentes, bGPropostas, bFechar, bAvancar);
        menu.setPrefHeight(700);

        //menu.setPrefWidth(300);
        //menu.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        setLeft(menu);
    }
    private void registerHandler() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });
        bGAlunos.setOnAction(actionEvent -> {

            model.gerirAlunos();
        });
        bGDocentes.setOnAction(actionEvent -> {

            model.gerirDocentes();
            System.out.println(model.getState().toString());
        });
        bGPropostas.setOnAction(actionEvent -> {
            model.gerirPropostas();
            System.out.println(model.getState().toString());
        });
        bAvancar.setOnAction(actionEvent -> {
            model.avancarFase();
        });
        bFechar.setOnAction(actionEvent -> {
            System.out.println(model.fecharFase());
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.CONFIG_OPTIONS);
    }
}
