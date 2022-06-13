package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.ui.gui.utils.AppBar;

public class RootPane extends BorderPane {
    ModelManager model;
    Stage stage;
    public RootPane(ModelManager model, Stage stage) {
        this.model = model;
        this.stage = stage;
        //this.setMaxSize(1600,800);
        createViews();
    }

    private void createViews() {
        AppBar menubar = new AppBar(model, stage);
        setTop(menubar);
        StackPane stack = new StackPane();
        ConfigOptionsUI c = new ConfigOptionsUI(model);
        LoadUI load = new LoadUI(model);
        GestaoAlunosUI gestaoAlunosUI = new GestaoAlunosUI(model);
        GestaoDocentesUI gestaoDocentesUI = new GestaoDocentesUI(model);
        GestaoPropostasUI gestaoPropostasUI = new GestaoPropostasUI(model);
        OpcoesCandidaturaUI opcoesCandidaturaUI = new OpcoesCandidaturaUI(model);
        AtribuicaoPropostasUI atribuicaoPropostasUI = new AtribuicaoPropostasUI(model);
        AtribuicaoManualPropostasUI atribuicaoManualPropostasUI = new AtribuicaoManualPropostasUI(model);
        ConflitoAtribuicaoCandidaturaUI conflitoAtribuicaoCandidaturaUI = new ConflitoAtribuicaoCandidaturaUI(model);
        AtribuicaoOrientadoresUI atribuicaoOrientadoresUI = new AtribuicaoOrientadoresUI(model);
        GestaoOrientadoresUI gestaoOrientadoresUI = new GestaoOrientadoresUI(model);
        ConsultaUI consultaUI = new ConsultaUI(model);
        SairUI sairUI = new SairUI(model);
        //c.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        stack.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, null)));
        stack.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        stack.getChildren().add(c);
        stack.getChildren().addAll(load, gestaoAlunosUI, gestaoDocentesUI, gestaoPropostasUI, opcoesCandidaturaUI, atribuicaoPropostasUI,atribuicaoManualPropostasUI,
                conflitoAtribuicaoCandidaturaUI,atribuicaoOrientadoresUI, gestaoOrientadoresUI, consultaUI,sairUI);

        setCenter(stack);
//        stack.setMaxSize(1600,500);
//        stack.setPrefHeight(500);

    }


}
