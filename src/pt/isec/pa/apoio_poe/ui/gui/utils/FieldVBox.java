package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.scene.control.Control;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FieldVBox extends VBox {

    List<Control> nodes;
    public FieldVBox(Control...nodes) {
        this.nodes = new ArrayList<>();
        this.nodes.addAll(List.of(nodes));
        getChildren().addAll(nodes);
        createViews();
    }

    private void createViews() {
        
    }
}
