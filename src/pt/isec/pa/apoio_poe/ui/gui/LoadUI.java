package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;

import java.io.IOException;

public class LoadUI extends BorderPane {
    ModelManager model;
    Label label;
    Button inicio, carregar, sair;
    VBox box;
    public LoadUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });
        inicio.setOnAction(actionEvent -> {
            model.begin();
        });
        carregar.setOnAction(actionEvent -> {
            try {
                model.load();
            } catch (IOException e) {
                System.out.println("Erro de leitura");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            update();
            System.out.println(model.getState().toString());
        });
    }

    private void createViews() {
        //this.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY)));
        //this.setWidth(300);
        this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        label = new Label("Inicio");
        HBox title = new HBox(label);
        title.setAlignment(Pos.CENTER);
        HBox.setMargin(label, new Insets(100));
        label.setFont(new Font(40));
        inicio = new ButtonMenu("Inicio");
        carregar = new ButtonMenu("Carregar Ultimo Ficheiro");
        sair = new ButtonMenu("Sair");

        //inicio.setBackground(new Background(new BackgroundFill(Color.rgb(84,84,84), CornerRadii.EMPTY, Insets.EMPTY)));
        inicio.setPrefWidth(300);
        inicio.setPrefHeight(50);
        //carregar.setBackground(new Background(new BackgroundFill(Color.rgb(84,84,84), CornerRadii.EMPTY, Insets.EMPTY)));
        carregar.setPrefWidth(300);
        carregar.setPrefHeight(50);
        //sair.setBackground(new Background(new BackgroundFill(Color.rgb(84,84,84), CornerRadii.EMPTY, Insets.EMPTY)));
        sair.setPrefWidth(300);
        sair.setPrefHeight(50);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(4);
        dropShadow.setOffsetY(6);
        box = new VBox();
        box.setEffect(dropShadow);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(50);

        VBox shape = new VBox();
        shape.getChildren().addAll(title, box);
        shape.setAlignment(Pos.TOP_CENTER);
        shape.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        setCenter(shape);

    }

    void update(){
        box.getChildren().removeAll(inicio, carregar, sair);
        if(model.existsFileBin()){
            box.getChildren().addAll(inicio, carregar, sair);
        }
        else {
            box.getChildren().addAll(inicio, sair);
        }
        this.setVisible(model != null && model.getState() == EnumState.LOAD_STATE);
    }

}
