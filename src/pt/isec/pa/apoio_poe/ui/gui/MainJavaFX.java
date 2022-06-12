package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.ui.gui.utils.AlertSingleton;

import java.io.IOException;

public class MainJavaFX extends Application {

    ModelManager model;
    public MainJavaFX() {
        model = new ModelManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        ModelManager model = new ModelManager();
        RootPane root = new RootPane(model, stage);
        //Scene cena = new Scene(root, stage.getMaxWidth(), stage.getMaxHeight());
        Scene cena = new Scene(root, 1600,800);
        System.out.println(cena.getHeight());
        stage.setScene(cena);
        stage.setTitle("Gestao de Projetos e Estágios");
//        stage.setOnCloseRequest(windowEvent -> {
//            AlertSingleton.getInstanceConfirmation().setAlertText("Guardar", "Pretende Guardar o estado da Aplicação?", "");
//            AlertSingleton.getInstanceConfirmation().showAndWait().ifPresent(result -> {
//                if (result.getText().equalsIgnoreCase("YES")){
//                    try {
//                        model.save();
//                    } catch (IOException e) {
//                        AlertSingleton.getInstanceConfirmation().setAlertText("Guardar", "Nao foi possivel guardar?", "");
//                        AlertSingleton.getInstanceConfirmation().showAndWait();
//                    }
//                }
//            });
//
//            AlertSingleton.getInstanceConfirmation().setAlertText("Sair", "Pretende Sair da Aplicação?", "");
//            AlertSingleton.getInstanceConfirmation().showAndWait().ifPresent(result -> {
//                if (result.getText().equalsIgnoreCase("YES")){
//                    Platform.exit();
//                }
//                else{
//                    windowEvent.consume();
//                }
//            });
//        });

        stage.show();
//        Stage stage2 = new Stage();
//        RootPane root2 = new RootPane(model);
//        Scene scene2 = new Scene(root2,600,400);
//        stage2.setScene(scene2);
//        stage2.setTitle("TeoStateJFX2");
//        stage2.setMinWidth(400);
//        stage2.show();


    }
}
