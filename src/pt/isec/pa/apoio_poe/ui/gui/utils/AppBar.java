package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.ModelManager;

import java.io.File;
import java.io.IOException;


public class AppBar extends MenuBar {

    ModelManager model;
    Menu file, edit;
    MenuItem mnLoad, mnSave, mnUndo, mnRedo, mnExit;
    FileChooser fileChooser;
    public AppBar(ModelManager model) {
        this.model = model;
        fileChooser = new FileChooser();
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
            model.sair();
        });
        mnSave.setOnAction(actionEvent -> {

            try {
                model.save();
            } catch (IOException e) {
                System.out.println("Nao foi possivel guardar");
            }
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
    }

    private void update() {
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
