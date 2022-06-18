package pt.isec.pa.apoio_poe.ui.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.*;
import pt.isec.pa.apoio_poe.ui.resource.CSSManager;

public class GestaoOrientadoresUI extends BorderPane {
    ModelManager model;
    MenuVertical menu;
    ButtonMenu btnGestaoOrientadores, btnAtribui, btnVoltar;
    TablePropostas tablePropostas;
    VBox vBoxTable, vBoxInsercao;
    TextField tfIdProposta, tfEmailOrientador;
    Button btnInserir, btnAtualizar;
    public GestaoOrientadoresUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaMenu();
        preparaTabela();
        preparaInsercao();
        VBox center = new VBox(vBoxTable, vBoxInsercao);
        setCenter(center);
    }

    private void preparaInsercao() {
        Label lProposta = new Label("ID da Proposta");
        tfIdProposta = new TextField();
        tfIdProposta.setMaxWidth(400);
        Label lDocente = new Label("Email do Docente Orientador");
        tfEmailOrientador = new TextField();
        tfEmailOrientador.setMaxWidth(400);
        btnInserir = new Button("Inserir Atribuição");
        btnInserir.setId("button_submit");
        CSSManager.applyCSS(btnInserir, "button_SUBMIT.css");
        btnAtualizar = new Button("Atualizar Atribuição");
        btnAtualizar.setId("button_submit");
        CSSManager.applyCSS(btnAtualizar, "button_SUBMIT.css");
        vBoxInsercao = new VBox(lProposta,tfIdProposta,lDocente,tfEmailOrientador, btnInserir);
        VBox.setMargin(lProposta, new Insets(30,0,20,0));
        VBox.setMargin(lDocente, new Insets(20,0,20,0));
        VBox.setMargin(tfEmailOrientador, new Insets(0,0,20,0));
        vBoxInsercao.setAlignment(Pos.CENTER);

    }

    private void preparaTabela() {
        tablePropostas = new TablePropostas(model, null);
        tablePropostas.removeCols("Docente", "Entidade", "N.Aluno");
        TableColumn<Proposta, String> colEmailDocente = new TableColumn<>("Email Orientador");
        colEmailDocente.setCellValueFactory(propostaStringCellDataFeatures -> {
            Docente orientador = propostaStringCellDataFeatures.getValue().getOrientador();
            if(orientador == null){
                return new ReadOnlyObjectWrapper<>("");
            }
            return new ReadOnlyObjectWrapper<>(orientador.getEmail());
        });
        colEmailDocente.setPrefWidth(210);
        TableColumn<Proposta, String> colNome = new TableColumn<>("Nome Orientador");
        colNome.setCellValueFactory(propostaStringCellDataFeatures -> {
            Docente orientador = propostaStringCellDataFeatures.getValue().getOrientador();
            if(orientador == null){
                return new ReadOnlyObjectWrapper<>("");
            }
            return new ReadOnlyObjectWrapper<>(orientador.getNome());
        });
        colNome.setPrefWidth(200);
        TableColumn<Proposta, Button> colEditar = new TableColumn<>("Editar");
        colEditar.setCellValueFactory(propostaStringCellDataFeatures -> {
            Button editar = new Button("Editar");
            editar.setOnAction(actionEvent -> {
                tfEmailOrientador.setText(propostaStringCellDataFeatures.getValue().getEmailOrientador());
                tfIdProposta.setText(propostaStringCellDataFeatures.getValue().getId());
                if(vBoxInsercao.getChildren().contains(btnInserir)){
                    vBoxInsercao.getChildren().remove(btnInserir);
                    vBoxInsercao.getChildren().add(btnAtualizar);
                }
            });
            editar.setId("button_editar");
            CSSManager.applyCSS(editar,"button_editar.css");
            return new ReadOnlyObjectWrapper<>(editar);
        });
        colEditar.setPrefWidth(140);
        TableColumn<Proposta, Button> colRemover = new TableColumn<>("Remover");
        colRemover.setCellValueFactory(propostaStringCellDataFeatures -> {
           Button remover = new Button("Remover");
           remover.setOnAction(actionEvent -> {

               model.removerAtribuicaoOrientador(propostaStringCellDataFeatures.getValue().getEmailOrientador(), propostaStringCellDataFeatures.getValue().getId());
               clearFields();
               modoInsercao();
           });
            remover.setId("button_delete");
            CSSManager.applyCSS(remover,"buttonDelete.css");
            return new ReadOnlyObjectWrapper<>(remover);
        });
        colRemover.setPrefWidth(140);
        tablePropostas.addCols(colEmailDocente);
        tablePropostas.addCols(colNome);
        tablePropostas.addCols(colEditar);
        tablePropostas.addCols(colRemover);

        Label lTable = new Label("Propostas com Orientador");
        lTable.setFont(new Font(26));
        HBox titleTable = new HBox(lTable);
        titleTable.setAlignment(Pos.CENTER);
        vBoxTable = new VBox(titleTable,tablePropostas);
        VBox.setMargin(titleTable, new Insets(25,0,25,0));
    }

    private void preparaMenu() {
        btnGestaoOrientadores = new ButtonMenu("Gestão de Orientadores");
        btnAtribui = new ButtonMenu("Atribuir Orientador");
        btnVoltar = new ButtonMenu("Voltar");
        menu = new MenuVertical(btnGestaoOrientadores,btnAtribui, btnVoltar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
            updateTabela();
        });
        model.addPropertyChangeListener(ModelManager.PROP_PROPOSTAS, evt -> {

            updateTabela();
        });
        model.addPropertyChangeListener(ModelManager.PROP_ALUNOS, evt -> {

            updateTabela();
        });
        model.addPropertyChangeListener(ModelManager.PROP_DOCENTES, evt -> {

            updateTabela();
        });
        btnVoltar.setOnAction(actionEvent -> {
            model.recuarFase();
        });
        btnInserir.setOnAction(actionEvent ->  {
            if(tfEmailOrientador.getText() == null || tfEmailOrientador.getText().equals(""))
                return;
            if (tfIdProposta.getText() == null || tfIdProposta.getText().equals("")){
                return;
            }
            ErrorCode e = model.insereOrientador(tfEmailOrientador.getText(), tfIdProposta.getText());
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas na inserção", MessageTranslate.translateErrorCode(e));
            }
            clearFields();
        });
        btnAtualizar.setOnAction(actionEvent -> {
            if(tfEmailOrientador.getText() == null || tfEmailOrientador.getText().equals(""))
                return;
            if (tfIdProposta.getText() == null || tfIdProposta.getText().equals("")){
                return;
            }
            ErrorCode e = model.editOrientador(tfEmailOrientador.getText(), tfIdProposta.getText());
            if(e != ErrorCode.E0){
                AlertSingleton.getInstanceWarning().setAlertText("", "Problemas na inserção", MessageTranslate.translateErrorCode(e));
            }
            clearFields();
            modoInsercao();
        });
        btnAtribui.setOnAction(actionEvent -> {
            clearFields();
            modoInsercao();
        });
    }

    private void modoInsercao() {
        if(vBoxInsercao.getChildren().contains(btnAtualizar)) {
            vBoxInsercao.getChildren().remove(btnAtualizar);
            vBoxInsercao.getChildren().add(btnInserir);
        }
    }

    private void clearFields() {
        tfEmailOrientador.setText("");
        tfIdProposta.setText("");
    }

    private void updateTabela() {
        if(model != null && model.getState() == EnumState.GESTAO_ORIENTADORES){
            tablePropostas.getItems().clear();
            tablePropostas.getItems().addAll(model.getPropostasComOrientador());
        }
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EnumState.GESTAO_ORIENTADORES);

    }
}
