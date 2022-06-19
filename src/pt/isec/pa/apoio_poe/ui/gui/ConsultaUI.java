package pt.isec.pa.apoio_poe.ui.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.tables.TableAlunos;
import pt.isec.pa.apoio_poe.ui.gui.tables.TableDocentes;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsultaUI extends BorderPane {

    ModelManager model;
    MenuVertical menu;
    ButtonMenu btnConsultaUI, btnListaAlunos, btnOrientacoesPorDocentes;
    PieChart pieRamos, piePercProp, pieAbsProp;
    BarChart<String, Number> barChartTop5Empresas, barChartTop5DocentesOrientadores;
    BorderPane bp; ScrollPane scrollPane; VBox container;
    XYChart.Series<String, Number> data;
    XYChart.Series<String,Number> dataOrientadores;
    HBox hBox3,  hBox2, hBox1, hBoxdadosOrientadoresMedia;
    Label lDadosOrientadoresMedia;
    TableAlunos tvAlunosComPropostaComOrientador, tvAlunosComPropostaSemOrientador;
    TableDocentes tvDocentes;
    List<Node> nodesShow;
    private VBox boxTvAlunosComOrientador, boxTvAlunosSemOrientador,boxTvDocentes;


    public ConsultaUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaMenu();
        preparaGraficos();
        preparaTabelaOrientadores();
        preparaTabelaOrientacoesPorDocent();
        Label title = new Label("Consulta");
        title.setFont(new Font(24));
        HBox boxTitle = new HBox(title);
        HBox.setMargin(title, new Insets(60));
        boxTitle.setAlignment(Pos.CENTER);
        boxTitle.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));

        container = new VBox(50);
        scrollPane = new ScrollPane();
        hBox1 = new HBox(pieRamos, pieAbsProp);
        hBox2 = new HBox(piePercProp, barChartTop5Empresas);
        hBox3 = new HBox(barChartTop5DocentesOrientadores);
        hBox3.setAlignment(Pos.CENTER);
        container.getChildren().add(hBox1);
        container.getChildren().add( hBox2);
        container.getChildren().add( hBox3);
        VBox.setMargin(hBox3, new Insets(50));
        container.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
        bp = new BorderPane();
        bp.setCenter(container);
        bp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
        scrollPane = new ScrollPane(bp);
        setCenter(scrollPane);
        scrollPane.setFitToWidth(true);
        nodesShow = new ArrayList<>();
        nodesShow.addAll(List.of(hBox1,hBox2,hBox3));

        bp.setTop(boxTitle);
    }

    private void preparaTabelaOrientacoesPorDocent() {
        Label lTvDocentes= new Label("Numero de Orientações Por Docentes");
        lTvDocentes.setFont(new Font(18));
        tvDocentes = new TableDocentes(model);

        lDadosOrientadoresMedia = new Label("");
        hBoxdadosOrientadoresMedia = new HBox(lDadosOrientadoresMedia);
        hBoxdadosOrientadoresMedia.setAlignment(Pos.CENTER);

        boxTvDocentes = new VBox(lTvDocentes,tvDocentes, hBoxdadosOrientadoresMedia);
        boxTvDocentes.setAlignment(Pos.CENTER);
        VBox.setMargin(lTvDocentes, new Insets(0,0,30,0));
        VBox.setMargin(tvDocentes, new Insets(0,0,60,0));

    }

    private void preparaTabelaOrientadores() {
        tvAlunosComPropostaComOrientador = new TableAlunos(model);
        tvAlunosComPropostaComOrientador.removeCols("Possibilidade Estagio", "Classificacao");
        TableColumn<Aluno, String> colPropId = new TableColumn<>("Proposta");

        colPropId.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getId());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colPropTipo = new TableColumn<>("Tipo");
        colPropTipo.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getTipo());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colPropTitulo = new TableColumn<>("Titulo");
        colPropTitulo.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getTitulo());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colPropOri = new TableColumn<>("Email Orientador");
        colPropOri.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                if(aluno.getValue().getProposta().temDocenteOrientador())
                    return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getEmailOrientador());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colPropOriNome = new TableColumn<>("Nome Orientador");
        colPropOriNome.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                if(aluno.getValue().getProposta().temDocenteOrientador())
                    return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getOrientador().getNome());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        tvAlunosComPropostaComOrientador.addCols(colPropId,colPropTipo, colPropTitulo, colPropOri, colPropOriNome);

        TableColumn<Aluno, String> colPropId2 = new TableColumn<>("Proposta");
        colPropId2.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getId());
            }
            return new ReadOnlyObjectWrapper<>("");});
        TableColumn<Aluno, String> colPropTipo2 = new TableColumn<>("Tipo");
        colPropTipo2.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getTipo());
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        TableColumn<Aluno, String> colPropTitulo2 = new TableColumn<>("Titulo");
        colPropTitulo2.setCellValueFactory(aluno ->{
            if (aluno.getValue().temPropostaConfirmada()) {
                return new ReadOnlyObjectWrapper<>(aluno.getValue().getProposta().getTitulo());
            }
            return new ReadOnlyObjectWrapper<>("");
        });

        tvAlunosComPropostaSemOrientador = new TableAlunos(model);
        tvAlunosComPropostaSemOrientador.removeCols("Possibilidade Estagio", "Classificacao");
        tvAlunosComPropostaSemOrientador.addCols(colPropId2,colPropTipo2,colPropTitulo2);

        Label lAO = new Label("Alunos com proposta atribuída e com orientador associado");
        lAO.setFont(new Font(18));
        Label lASO = new Label("Alunos com proposta atribuída mas sem orientador associado");
        lASO.setFont(new Font(18));
        boxTvAlunosComOrientador = new VBox(lAO, tvAlunosComPropostaComOrientador);
        boxTvAlunosComOrientador.setAlignment(Pos.CENTER);
        VBox.setMargin(lAO, new Insets(0,0,30,0));
        boxTvAlunosSemOrientador = new VBox(lASO, tvAlunosComPropostaSemOrientador);
        boxTvAlunosSemOrientador.setAlignment(Pos.CENTER);
        VBox.setMargin(lASO, new Insets(50,0,30,0));

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
        CategoryAxis xAxisEmpresas = new CategoryAxis();
        NumberAxis yAxisQtdEmpresas = new NumberAxis();
        barChartTop5Empresas = new BarChart<>(xAxisEmpresas, yAxisQtdEmpresas);
        pieRamos.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", data.pieValueProperty()
                        )
                )
        );


        CategoryAxis xAxisOrientadores = new CategoryAxis();
        NumberAxis yAxisQtdOrientadores = new NumberAxis();
        barChartTop5DocentesOrientadores = new BarChart<>(xAxisOrientadores, yAxisQtdOrientadores);
        xAxisOrientadores.setLabel("Orientadores");
        yAxisQtdOrientadores.setLabel("Nº de Orientações");

    }



    private void preparaMenu() {
        btnConsultaUI = new ButtonMenu("Consulta");
        btnListaAlunos = new ButtonMenu("Lista de Alunos");
        btnOrientacoesPorDocentes = new ButtonMenu("Lista de Orientadores");
        menu = new MenuVertical(btnConsultaUI, btnListaAlunos,btnOrientacoesPorDocentes);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
        btnConsultaUI.setOnAction(actionEvent -> {

            nodesShow.clear();
            nodesShow.addAll(List.of(hBox1, hBox2, hBox3));
            updateViews();
            update();
        });
        btnListaAlunos.setOnAction(actionEvent -> {
            nodesShow.clear();
            nodesShow.addAll(List.of(boxTvAlunosComOrientador, boxTvAlunosSemOrientador));
            updateViews();
        });
        btnOrientacoesPorDocentes.setOnAction(actionEvent -> {
            nodesShow.clear();
            nodesShow.add(boxTvDocentes);
            updateViews();
            //bp.setBottom(hBoxdadosOrientadoresMedia);
        });

    }



    private void updateGraficosPie() {
        if(model.getState() != EnumState.CONSULTA) {
            return;
        }
        hBox2.getChildren().remove(barChartTop5Empresas);
        pieRamos.getData().clear();
        Map<String, Integer> dados = model.getPropostasPorRamos();
        for(Map.Entry<String, Integer> set : dados.entrySet()){
            pieRamos.getData().add(new PieChart.Data(set.getKey(),set.getValue()));
        }


        pieAbsProp.getData().clear();
        List<Double> dadosAtribuicoes = model.getDadosAtribuicoes();
        pieAbsProp.getData().add(new PieChart.Data("Atribuida", dadosAtribuicoes.get(0)));
        pieAbsProp.getData().add(new PieChart.Data("Não Atribuida", dadosAtribuicoes.get(1)));

        piePercProp.getData().clear();
        piePercProp.getData().add(new PieChart.Data("Atribuida", dadosAtribuicoes.get(2)));
        piePercProp.getData().add(new PieChart.Data("Não Atribuida", dadosAtribuicoes.get(3)));

        pieRamos.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " - ", data.pieValueProperty().intValue()
                        )
                )
        );
        pieAbsProp.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " - ",  data.pieValueProperty().intValue()
                        )
                )
        );
        piePercProp.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " - ",  String.format("%.0f", data.getPieValue()), "%"
                        )
                )
        );
    }
    void updateBarChart(){
        CategoryAxis xAxisEmpresas = new CategoryAxis();
        NumberAxis yAxisQtdEmpresas = new NumberAxis();
        barChartTop5Empresas = new BarChart<>(xAxisEmpresas, yAxisQtdEmpresas);
        barChartTop5Empresas.setTitle("Top 5 Empresas com mais estágios");
        xAxisEmpresas.setLabel("Empresas");
        data = new XYChart.Series<>();

        Map<String,Integer> dadosBar = model.getTop5Empresas();
        for(Map.Entry<String,Integer> set : dadosBar.entrySet()){
            data.getData().add(new XYChart.Data<>( set.getKey(), set.getValue()));
        }
        barChartTop5Empresas.getData().clear();
        barChartTop5Empresas.getData().add(data);
        hBox2.getChildren().add(barChartTop5Empresas);



        CategoryAxis xAxisOrientadores = new CategoryAxis();
        NumberAxis yAxisQtdOrientadores = new NumberAxis();

        barChartTop5DocentesOrientadores = new BarChart<>(xAxisOrientadores, yAxisQtdOrientadores);
        barChartTop5DocentesOrientadores.setTitle("Top 5 Docentes com mais Orientações");
        xAxisOrientadores.setLabel("Orientadores");
        yAxisQtdOrientadores.setLabel("Nº de Orientações");
        dataOrientadores = new XYChart.Series<>();
        dataOrientadores.getData().clear();
        Map<Docente, Integer> dadosOrientadores= model.getTop5Orientadores();

        for(Map.Entry<Docente, Integer> set : dadosOrientadores.entrySet()){
            dataOrientadores.getData().add(new XYChart.Data<>(set.getKey().getNome(), set.getValue()));
        }

        barChartTop5DocentesOrientadores.getData().clear();
        barChartTop5DocentesOrientadores.getData().add(dataOrientadores);
        hBox3.getChildren().clear();
        hBox3.getChildren().add(barChartTop5DocentesOrientadores);
        barChartTop5Empresas.setAnimated(true);
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.CONSULTA);
        if (model == null || model.getState() != EnumState.CONSULTA)
            return;

        updateGraficosPie();
        updateBarChart();
        updateTables();

    }


    private void updateTables() {
        tvAlunosComPropostaComOrientador.getItems().clear();
        tvAlunosComPropostaSemOrientador.getItems().clear();
        tvAlunosComPropostaComOrientador.getItems().addAll(model.getAlunosComPropostaConfirmadaEOrientador());
        tvAlunosComPropostaSemOrientador.getItems().addAll(model.getAlunosComPropostaConfirmadaESemOrientador());


        Map<Docente, Integer> docentes = model.getDocentesPorOrientacoes();
        tvDocentes.removeCols("Número de Orientações");
        TableColumn<Docente, Integer> tcOrientacoes = new TableColumn<>("Número de Orientações");
        tcOrientacoes.setCellValueFactory(d-> new ReadOnlyObjectWrapper<>( docentes.get(d.getValue())));
        tvDocentes.getItems().clear();
        tvDocentes.addCols(tcOrientacoes);
        tvDocentes.getItems().addAll(docentes.keySet());



        Map<String,Number> dadosLabelOrientadores = model.getDadosNumeroOrientacoes();
        List<Label> labels = new ArrayList<>();
        for(Map.Entry<String,Number> set : dadosLabelOrientadores.entrySet()){
            labels.add(new Label(translateDadosLabelOrientador(set.getKey()) + ": " + set.getValue()));
        }
        for (Label label : labels) {
            label.setFont(new Font(15));
        }
        hBoxdadosOrientadoresMedia.getChildren().clear();
        hBoxdadosOrientadoresMedia.getChildren().addAll(labels);
        hBoxdadosOrientadoresMedia.setAlignment(Pos.CENTER);
        hBoxdadosOrientadoresMedia.setSpacing(80);


    }

    private String translateDadosLabelOrientador(String key) {
        return switch (key){
            case "media" -> "Média";
            case "max" -> "Máximo";
            case "min" -> "Mínimo";
            default -> "";
        };
    }

    private void updateViews() {
        container.getChildren().clear();
        container.getChildren().addAll(nodesShow);
    }
}
