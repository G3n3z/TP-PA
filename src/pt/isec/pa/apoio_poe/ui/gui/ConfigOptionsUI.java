package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.AlertSingleton;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.MessageTranslate;

public class ConfigOptionsUI extends BorderPane {

    ModelManager model;
    VBox menu;
    ButtonMenu bGAlunos, bGDocentes,bGPropostas, bFechar, bAvancar;
    Label title;
    public ConfigOptionsUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandler();
        update();
    }
    private void createViews() {

        title = new Label("Opções de Configuração");
        title.setFont(new Font(26));
        HBox titulo = new HBox();
        HBox.setMargin(title, new Insets(25,0,25,0));
        titulo.setPrefHeight(50);
        titulo.getChildren().add(title);
        titulo.setAlignment(Pos.TOP_CENTER);

        bGAlunos = new ButtonMenu("Gestão de Alunos");
        bGDocentes = new ButtonMenu("Gestão de Docentes");
        bGPropostas = new ButtonMenu("Gestão de Propostas");
        bFechar = new ButtonMenu("Fechar Fase");
        bAvancar = new ButtonMenu("Avançar Fase");
        menu = new MenuVertical(bGAlunos, bGDocentes, bGPropostas, bFechar, bAvancar);
        menu.setPrefHeight(700);

        //menu.setPrefWidth(300);
        //menu.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        setLeft(menu);
        setCenter(titulo);
    }
    private void registerHandler() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });
        model.addPropertyChangeListener(ModelManager.PROP_CLOSE_STATE, evt -> {
            updateClose();
        });
        bGAlunos.setOnAction(actionEvent -> {

            model.gerirAlunos();
        });
        bGDocentes.setOnAction(actionEvent -> {

            model.gerirDocentes();
            System.out.println(model.getState().toString());
        });
        bGPropostas.setOnAction(actionEvent -> {
            model.gerirPropostas();
            System.out.println(model.getState().toString());
        });
        bAvancar.setOnAction(actionEvent -> {
            model.avancarFase();
        });
        bFechar.setOnAction(actionEvent -> {
            ErrorCode e = model.fecharFase();
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas nno Fecho da Fase", MessageTranslate.translateErrorCode(e));
                AlertSingleton.getInstanceWarning().showAndWait();
            }
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.CONFIG_OPTIONS);
        //closedFase();
    }

//    private void closedFase() {
//        if(model == null){
//            return;
//        }
//        if(model.getState() != EnumState.CONFIG_OPTIONS){
//            return;
//        }
//        if (model.isClosed()){
//            fechaFase();
//        }else {
//            if(!menu.getChildren().contains(bFechar))
//                menu.getChildren().add(bFechar);
//        }
//    }
//
//    private void fechaFase() {
//        menu.getChildren().remove(bFechar);
//    }

    private void updateClose() {
        if(model.getCloseState(EnumState.CONFIG_OPTIONS)){
            menu.getChildren().remove(bFechar);
        }
        else {
            if(!menu.getChildren().contains(bFechar)){
                menu.getChildren().add(3,bFechar);
            }
        }

    }
}
