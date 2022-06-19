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
        Scene cena = new Scene(root, 1600,800);
        stage.setScene(cena);
        stage.setTitle("Gestao de Projetos e Estágios");
        stage.show();


//        Stage stage2 = new Stage();
//        RootPane root2 = new RootPane(model, stage2);
//        Scene scene2 = new Scene(root2,600,400);
//        stage2.setScene(scene2);
//        stage2.setTitle("TeoStateJFX2");
//        stage2.setMinWidth(400);
//        stage2.show();
//

    }
}
