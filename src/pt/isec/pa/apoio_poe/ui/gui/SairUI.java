package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

import java.io.IOException;

public class SairUI extends BorderPane {
    private Alert alert;
    private ModelManager model;

    public SairUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        alert = new Alert(Alert.AlertType.CONFIRMATION, "Pretende Guardar o Estado da Aplicação?", ButtonType.YES, ButtonType.NO);
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
    }

    private void update(){
        this.setVisible(model != null && model.getState() == EnumState.SAIR);
        if(model != null && model.getState() == EnumState.SAIR){
            this.setVisible(true);

            alert.showAndWait().ifPresent(response -> {
                if(response.getText().equalsIgnoreCase("YES")){
                    try {
                        model.save();
                    } catch (IOException e) {
                        return;
                    }
                }
                Platform.exit();
            });
        }else {
            this.setVisible(false);
        }


    }
}
