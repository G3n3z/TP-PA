package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.scene.control.Button;

public class ButtonOk extends Button {

    public ButtonOk(String s) {
        super(s);
        createViews();
    }

    private void createViews() {
        this.setPrefWidth(60);
        this.setPrefHeight(40);
    }
}
