package pt.isec.pa.apoio_poe.ui.gui.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.ModelManager;

public class PopUP extends Popup {
    ModelManager model;
    private static PopUP instance;
    private Label title;
    private HBox hBoxTitle, hBoxButtons, hBoxMessage;
    private VBox center;
    BorderPane borderPane;
    private PopUP() {
        title = new Label("TITLE");
        hBoxTitle = new HBox(title);
        title.setFont(new Font(18));
        title.setTextFill(Color.WHITE);
        hBoxTitle.setAlignment(Pos.CENTER);
        HBox.setMargin(title, new Insets(20));
        hBoxTitle.setBackground(new Background(new BackgroundFill(Color.web("#37304a"), CornerRadii.EMPTY, Insets.EMPTY)));
        borderPane = new BorderPane();
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        borderPane.setTop(hBoxTitle);
        borderPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        borderPane.setMinWidth(700);
        borderPane.setMinHeight(400);
        center = new VBox();
        hBoxButtons = new HBox();
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxMessage = new HBox();
        hBoxMessage.setAlignment(Pos.CENTER);
        center.getChildren().addAll(hBoxMessage, hBoxButtons);
        center.setAlignment(Pos.CENTER);
        VBox.setMargin(hBoxMessage, new Insets(30,0,30,0));
        VBox.setMargin(hBoxButtons, new Insets(0,0,30,0));
        borderPane.setCenter(center);
        //Scene cena = new Scene(borderPane,400,400);
        this.getContent().add(borderPane);
        this.centerOnScreen();
        setWidth(600);
    }

    public static PopUP getInstance(){
        if(instance == null){
            instance = new PopUP();
            return instance;
        }
        return instance;

    }
    public void addItens(Node node){
        hBoxMessage.getChildren().add(node);
    }
    public void addButtons(Node node){
        hBoxButtons.getChildren().add(node);
    }
    public void clearPopUp(){
        //this.getContent().clear();
        hBoxMessage.getChildren().clear();
        hBoxButtons.getChildren().clear();
    }
    public void showPopUp(Stage stage){
        this.show(stage);
    }

    public void setTextTitle(String title){
        this.title.setText(title);
    }
    public void createButtonOk(){
        ButtonOk ok = new ButtonOk("Ok");
        ok.setOnAction(actionEvent -> {
            this.hide();
        });
        hBoxButtons.getChildren().add(ok);

    }



}
