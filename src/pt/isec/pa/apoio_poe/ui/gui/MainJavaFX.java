package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.ModelManager;

public class MainJavaFX extends Application {

    ModelManager model;
    public MainJavaFX() {
        model = new ModelManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        ModelManager model = new ModelManager();
        RootPane root = new RootPane(model);
        //Scene cena = new Scene(root, stage.getMaxWidth(), stage.getMaxHeight());
        Scene cena = new Scene(root, 1600,800);
        System.out.println(cena.getHeight());
        stage.setScene(cena);
        stage.setTitle("Gestao de Projetos e Estágios");

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
