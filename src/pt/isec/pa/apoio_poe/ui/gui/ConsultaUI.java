package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;

import java.util.List;
import java.util.Map;

public class ConsultaUI extends BorderPane {

    ModelManager model;
    MenuVertical menu;
    ButtonMenu btnConsultaUI, btnListaAlunos, btnListaDePropostas, btnOrientacoesPorDocentes;
    PieChart pieRamos, piePercProp, pieAbsProp;
    BorderPane bp; ScrollPane scrollPane; VBox container;
    public ConsultaUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaMenu();
        preparaGraficos();
        container = new VBox(50);
        scrollPane = new ScrollPane();
        HBox hBox1 = new HBox(pieRamos, pieAbsProp);
        HBox hBox2 = new HBox(piePercProp);
        container.getChildren().add(hBox1);
        container.getChildren().add( hBox2);
        container.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        bp = new BorderPane();
        bp.setCenter(scrollPane);
        setCenter(bp);
    }

    private void preparaGraficos() {
        pieRamos = new PieChart();
        piePercProp = new PieChart();
        pieAbsProp = new PieChart();
        pieRamos.setTitle("Propostas Por Ramos");
        piePercProp.setTitle("Percentagem das Propostas");
        pieAbsProp.setTitle("Quantidade das Propostas Atribuidas e Não Atribuidas");

        pieRamos.setLabelsVisible(true);
        piePercProp.setLabelsVisible(true);
        pieAbsProp.setLabelsVisible(true);

        pieRamos.setLegendVisible(true);
        piePercProp.setLegendVisible(true);
        pieAbsProp.setLegendVisible(true);
    }



    private void preparaMenu() {
        btnConsultaUI = new ButtonMenu("Consulta");
        btnListaAlunos = new ButtonMenu("Lista de Alunos");
        btnListaDePropostas = new ButtonMenu("Lista de Propostas");
        btnOrientacoesPorDocentes = new ButtonMenu("Lista de Orientadores");
        menu = new MenuVertical(btnConsultaUI, btnListaAlunos, btnListaDePropostas,btnOrientacoesPorDocentes);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());

    }
    private void updateGraficos() {
        if(model.getState() != EnumState.CONSULTA) {
            return;
        }
        Map<String, Integer> dados = model.getPropostasPorRamos();
        for(Map.Entry<String, Integer> set : dados.entrySet()){
            pieRamos.getData().add(new PieChart.Data(set.getKey(),set.getValue()));
        }
        System.out.println(dados);
        List<Double> dadosAtribuicoes = model.getDadosAtribuicoes();
        pieAbsProp.getData().add(new PieChart.Data("Atribuida", dadosAtribuicoes.get(0)));
        pieAbsProp.getData().add(new PieChart.Data("Não Atribuida", dadosAtribuicoes.get(1)));

        piePercProp.getData().add(new PieChart.Data("Atribuida", dadosAtribuicoes.get(2)));
        piePercProp.getData().add(new PieChart.Data("Não Atribuida", dadosAtribuicoes.get(3)));
        System.out.println(dadosAtribuicoes);

    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.CONSULTA);
        updateGraficos();
    }
}
