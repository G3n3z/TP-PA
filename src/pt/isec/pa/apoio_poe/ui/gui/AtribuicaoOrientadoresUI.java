package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.layout.BorderPane;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;

public class AtribuicaoOrientadoresUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnAtribuirOrientadores;
    MenuVertical menu;
    public AtribuicaoOrientadoresUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
    }

    private void registerHandlers() {
    }

    private void update() {
    }
}
