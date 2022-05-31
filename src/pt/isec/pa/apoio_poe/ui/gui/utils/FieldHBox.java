package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class FieldHBox extends HBox {
    List<Control> nodes;

    public FieldHBox(Control ...nodes) {
        this.nodes = new ArrayList<>();
        this.nodes.addAll(List.of(nodes));
        getChildren().addAll(nodes);
        createViews();
    }

    private void createViews() {
        setAlignment(Pos.CENTER);
        double max = 0.0, aux;

        for (Control node : nodes) {
            if (node instanceof TextField tf) {
                tf.setStyle("-fx-border-radius: 20, 20");
            }
            HBox.setMargin(node, new Insets(0, 30, 0, 0));
            node.setPrefHeight(30);
        }
        setPrefHeight(50);
    }

    public void setMarginField(int index, Insets margin){
        if(index > nodes.size() || index <= 0)
            return;
        HBox.setMargin(nodes.get(index-1), margin);
    }
}
