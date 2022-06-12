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
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TablePropostas;
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
        Label lDocente = new Label("Email do Docente Orientador");
        tfEmailOrientador = new TextField();
        btnInserir = new Button("Inserir Atribuição");
        btnAtualizar = new Button("Atualizar Atribuição");
        vBoxInsercao = new VBox(lProposta,tfIdProposta,lDocente,tfEmailOrientador, btnInserir);
        VBox.setMargin(lProposta, new Insets(50,0,20,0));
        VBox.setMargin(lProposta, new Insets(40,0,20,0));
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
               System.out.println(model.removerAtribuicaoOrientador(propostaStringCellDataFeatures.getValue().getEmailOrientador(), propostaStringCellDataFeatures.getValue().getId()));
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

        javafx.scene.control.Label lTable = new Label("Propostas com Orientador");
        lTable.setFont(new Font(18));
        HBox titleTable = new HBox(lTable);
        titleTable.setAlignment(Pos.CENTER);
        vBoxTable = new VBox(titleTable,tablePropostas);
        VBox.setMargin(titleTable, new Insets(50,0,30,0));
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
            System.out.println(model.insereOrientador(tfEmailOrientador.getText(), tfIdProposta.getText()));
            clearFields();
        });
        btnAtualizar.setOnAction(actionEvent -> {
            if(tfEmailOrientador.getText() == null || tfEmailOrientador.getText().equals(""))
                return;
            if (tfIdProposta.getText() == null || tfIdProposta.getText().equals("")){
                return;
            }
            System.out.println(model.editOrientador(tfEmailOrientador.getText(), tfIdProposta.getText()));
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
