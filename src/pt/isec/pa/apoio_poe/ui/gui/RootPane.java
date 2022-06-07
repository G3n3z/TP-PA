package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.ui.gui.utils.AppBar;

public class RootPane extends BorderPane {
    ModelManager model;

    public RootPane(ModelManager model) {
        this.model = model;
        createViews();
    }

    private void createViews() {
        AppBar menubar = new AppBar(model);
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
        //c.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        //stack.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        stack.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        stack.getChildren().add(c);
        stack.getChildren().addAll(load, gestaoAlunosUI, gestaoDocentesUI, gestaoPropostasUI, opcoesCandidaturaUI, atribuicaoPropostasUI,atribuicaoManualPropostasUI,
                conflitoAtribuicaoCandidaturaUI,atribuicaoOrientadoresUI, gestaoOrientadoresUI, consultaUI);
        setCenter(stack);


    }


}
