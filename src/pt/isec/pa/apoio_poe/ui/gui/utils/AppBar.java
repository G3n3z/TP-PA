package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;

import java.io.File;
import java.io.IOException;


public class AppBar extends MenuBar {

    ModelManager model;
    Menu file, edit;
    MenuItem mnLoad, mnSave, mnUndo, mnRedo, mnExit;
    FileChooser fileChooser;
    Stage stage;
    public AppBar(ModelManager model, Stage stage) {
        this.model = model;
        fileChooser = new FileChooser();
        this.stage = stage;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        file = new Menu("File");
        edit = new Menu("Edit");
        mnLoad = new MenuItem("Load");
        mnSave = new MenuItem("Save");
        mnUndo = new MenuItem("Undo");
        mnRedo = new MenuItem("Redo");
        mnExit = new MenuItem("Sair");
        file.getItems().addAll(mnLoad, mnSave, mnExit);
        edit.getItems().addAll(mnUndo, mnRedo);
        this.getMenus().addAll(file, edit);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_UNDO, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_REDO, evt -> update());
        mnExit.setOnAction(actionEvent -> {
            if(model.getState()!= EnumState.LOAD_STATE)
                model.sair();
            else{
                AlertSingleton.getInstanceConfirmation().setAlertText("Sair", "Pretende Sair da Aplicação?", "");
                AlertSingleton.getInstanceConfirmation().showAndWait().ifPresent(result -> {
                    if (result.getText().equalsIgnoreCase("YES")){
                        Platform.exit();
                    }
                });
            }
        });
        mnSave.setOnAction(actionEvent -> {
            AlertSingleton.getInstanceConfirmation().setAlertText("Guardar", "Pretende Guardar o estado da Aplicação?", "");
            AlertSingleton.getInstanceConfirmation().showAndWait().ifPresent(result -> {
                System.out.println(result.getText());
                if (result.getText().equalsIgnoreCase("YES")){
                    try {
                        model.save();
                    } catch (IOException e) {
                        System.out.println("Nao foi possivel guardar");
                    }
                }
            });

        });
        mnLoad.setOnAction(actionEvent -> {
            try {
                model.load();
            } catch (Exception e) {
                System.out.println("Nao foi possivel guardar");
            }

        });

        mnRedo.setOnAction(actionEvent -> model.redo());
        mnUndo.setOnAction(actionEvent -> model.undo());

        stage.setOnCloseRequest(windowEvent -> {
            AlertSingleton.getInstanceConfirmation().setAlertText("Guardar", "Pretende Guardar o estado da Aplicação?", "");
            if(model.getState() != EnumState.LOAD_STATE) {
                AlertSingleton.getInstanceConfirmation().showAndWait().ifPresent(result -> {
                    if (result.getText().equalsIgnoreCase("YES")) {
                        try {
                            model.save();
                        } catch (IOException e) {
                            AlertSingleton.getInstanceConfirmation().setAlertText("Guardar", "Nao foi possivel guardar?", "");
                            AlertSingleton.getInstanceConfirmation().showAndWait();
                        }
                    }
                });
            }
            AlertSingleton.getInstanceConfirmation().setAlertText("Sair", "Pretende Sair da Aplicação?", "");
            AlertSingleton.getInstanceConfirmation().showAndWait().ifPresent(result -> {
                if (result.getText().equalsIgnoreCase("YES")){
                    Platform.exit();
                }
                else{
                    windowEvent.consume();
                }
            });
        });
    }

    private void update() {
        if(model.getState() == EnumState.LOAD_STATE){
            mnSave.setDisable(true);
        }else
            mnSave.setDisable(false);
        if(model.hasRedo()){
           mnRedo.setDisable(false);
        }
        else{
            mnRedo.setDisable(true);
        }
        if(model.hasUndo()){
            mnUndo.setDisable(false);
        }
        else{
            mnUndo.setDisable(true);
        }

    }
}
