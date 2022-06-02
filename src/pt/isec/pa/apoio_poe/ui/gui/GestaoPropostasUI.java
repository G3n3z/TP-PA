package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TablePropostas;

public class GestaoPropostasUI extends BorderPane {
    ModelManager model;
    ButtonMenu btnGestaoPropostas,btnInsereManual,btnExport,btnRemoveAll,btnRecuar;
    TableView<Proposta> tableView;
    VBox center;
    public GestaoPropostasUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }



    private void createViews() {
        createMenu();
        preparaTable();
        center = new VBox();
        center.getChildren().addAll(tableView);
        setCenter(center);

    }

    private void preparaTable() {
        tableView = new TablePropostas(model, null);
    }

    private void createMenu() {
        btnGestaoPropostas = new ButtonMenu("Gestão de Propostas");
        btnInsereManual = new ButtonMenu("Insere Propostas");
        btnExport= new ButtonMenu("Exportar Propostas Para CSV");
        btnRemoveAll= new ButtonMenu("Remover Todos");
        btnRecuar= new ButtonMenu("Voltar");
        MenuVertical menu = new MenuVertical(btnGestaoPropostas, btnInsereManual, btnExport, btnRemoveAll, btnRecuar);
        setLeft(menu);
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });

    }
    private void update() {

        this.setVisible(model != null && model.getState() == EnumState.GESTAO_PROPOSTAS);
    }
}
