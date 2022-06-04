package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.ModelManager;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.ui.gui.utils.ButtonMenu;
import pt.isec.pa.apoio_poe.ui.gui.utils.MenuVertical;
import pt.isec.pa.apoio_poe.ui.gui.utils.TableCandidatura;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;



public class OpcoesCandidaturaUI extends BorderPane {

    ModelManager model;
    ButtonMenu btnCandidaturas, btnInserirCandidaturas, btnExportarCSV, btnRemoverAll, btnObtencoes, btnFechar, btnRecuar, btnAvancar;
    MenuVertical menu;
    List<Node> nodesVisibles;
    TableCandidatura tableView;
    TextField tfAluno, tfPropostas;
    HBox hBoxInsereCandidaturas, hBoxButtonInsereCand;
    Label propostasInseridas;
    VBox vBoxCamposInsercaoPropostas, vBoxInsereCandidaturas, camposCentro;
    Button btnInsereCandManual, btnInsereCandCSV, btnEditCandidatura,btnVoltarEdit;
    Consumer<Candidatura> consumerEdit;
    public OpcoesCandidaturaUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        preparaMenu();
        preparaTabela();
        preparaCamposInserir();
        preparaObtencoes();
        nodesVisibles = new ArrayList<>();
        nodesVisibles.add(tableView);
        camposCentro = new VBox();
        setCenter(camposCentro);
    }

    private void preparaTabela() {
        consumerEdit = (c) ->{

          hBoxButtonInsereCand.getChildren().clear();
          hBoxButtonInsereCand.getChildren().addAll(btnEditCandidatura, btnVoltarEdit);
          tfAluno.setDisable(true);
          tfAluno.setText(Long.toString(c.getNumAluno()));
          List<String> id = c.getIdProposta();
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < id.size(); i++) {
              sb.append(id.get(i));
              if(i != id.size()-1){
                  sb.append(",");
              }
          }
          tfPropostas.setText(sb.toString());
          if (!nodesVisibles.contains(hBoxInsereCandidaturas)) {
              nodesVisibles.add(hBoxInsereCandidaturas);
          }
          update();
        };
        tableView = new TableCandidatura(model, consumerEdit);
    }

    private void preparaObtencoes() {

    }

    private void preparaCamposInserir() {
        Label lAluno = new Label("Aluno");
        tfAluno = new TextField();

        propostasInseridas = new Label("Propostas: ");
        tfPropostas = new TextField();
        Label help = new Label("Introduzir propostas separadas por ,");
        btnInsereCandManual = new Button("Insere Candidatura");
        btnInsereCandCSV = new Button("Importar CSV");

        /*Butoes edicao*/
        btnEditCandidatura = new Button("Atualizar");
        btnVoltarEdit = new Button("Voltar");

        hBoxButtonInsereCand = new HBox(btnInsereCandManual, btnInsereCandCSV);
        hBoxButtonInsereCand.setAlignment(Pos.CENTER);

        VBox vAluno =new VBox(lAluno, tfAluno);

        vBoxCamposInsercaoPropostas = new VBox(propostasInseridas, tfPropostas, help);
        vBoxInsereCandidaturas = new VBox(vAluno,vBoxCamposInsercaoPropostas, hBoxButtonInsereCand);

        hBoxInsereCandidaturas = new HBox(vBoxInsereCandidaturas);
        hBoxInsereCandidaturas.setAlignment(Pos.CENTER);
    }

    private void preparaMenu() {
        btnCandidaturas = new ButtonMenu("Candidaturas");
        btnInserirCandidaturas = new ButtonMenu("Inserir Candidaturas");
        btnExportarCSV = new ButtonMenu("Exportar CSV");
        btnRemoverAll= new ButtonMenu("Remover Todas");
        btnObtencoes = new ButtonMenu("Obter Listas");
        btnFechar = new ButtonMenu("Fechar Fase");
        btnRecuar = new ButtonMenu("Recuar Fase");
        btnAvancar = new ButtonMenu("Avançar Fase");
        menu = new MenuVertical(btnCandidaturas,btnInserirCandidaturas,btnExportarCSV,btnRemoverAll,btnObtencoes,btnFechar,btnRecuar,btnAvancar);
        setLeft(menu);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });
        btnAvancar.setOnAction(actionEvent -> {
            model.avancarFase();
        });
        btnFechar.setOnAction(actionEvent -> {
            model.fecharFase();
        });
        btnRecuar.setOnAction(actionEvent -> {
            model.recuarFase();
        });
        btnInserirCandidaturas.setOnAction(actionEvent -> {
            if (!nodesVisibles.contains(hBoxInsereCandidaturas)) {
                nodesVisibles.add(hBoxInsereCandidaturas);
            }
            update();
        });
        btnCandidaturas.setOnAction(actionEvent -> {
            nodesVisibles.clear();
            nodesVisibles.add(tableView);
            update();
        });
        btnExportarCSV.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Drawing (*.csv)", "*.csv")
            );
            File f = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(f == null){
                return;
            }
            ErrorCode error = model.exportCSV(f.getAbsolutePath());
        });
        btnInsereCandManual.setOnAction(actionEvent -> {
            List<String> ids;
            if (!checkFields())
                return;
            ids = getTextFields();
            System.out.println(ids.toString());
            System.out.println(model.insereCandidatura(tfAluno.getText(), ids));
        });
        btnInsereCandCSV.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Drawing (*.csv)", "*.csv")
            );
            File f = fileChooser.showOpenDialog(this.getScene().getWindow());
            if(f == null){
                return;
            }
            boolean error = true;
            try {
                error = model.importCandidaturasCSV(f.getAbsolutePath());
            } catch (CollectionBaseException e) {
                System.out.println(e.getMessageOfExceptions());
            }
        });
        btnEditCandidatura.setOnAction(actionEvent -> {
            if (!checkFields()) return;
            List<String> ids;
            if (!checkFields())
                return;
            ids = getTextFields();
            ErrorCode e = model.editCandidatura(tfAluno.getText(), ids);
            repoeBotaoInserir();
            clearFieldsEdit();
        });
        btnVoltarEdit.setOnAction(actionEvent -> {
            repoeBotaoInserir();
            clearFieldsEdit();
        });
        btnRemoverAll.setOnAction(actionEvent -> {
            model.removeAllCandidatura();
        });

    }

    private List<String> getTextFields() {
        List<String> ids ;
        StringTokenizer st = new StringTokenizer(tfPropostas.getText(),",");
        ids = new ArrayList<>();
        while (st.hasMoreElements()){
            ids.add(st.nextToken());
        }
        return ids;
    }

    private boolean checkFields() {
        if(tfPropostas.getText().equals("")){
            return false;
        }
        if (tfAluno.getText().equals("")){
            return false;
        }

        return true;
    }

    private void clearFieldsEdit() {
        tfAluno.setDisable(false);
        tfAluno.setText("");
        tfPropostas.setText("");
    }

    private void repoeBotaoInserir() {
        hBoxButtonInsereCand.getChildren().clear();
        hBoxButtonInsereCand.getChildren().addAll(btnInsereCandManual, btnInsereCandCSV);
    }

    private void update() {
        camposCentro.getChildren().clear();
        for (Node nodesVisible : nodesVisibles) {
            camposCentro.getChildren().add(nodesVisible);
        }
        isClosed();
        this.setVisible(model != null && model.getState() == EnumState.OPCOES_CANDIDATURA);
    }

    private void isClosed() {
        if(model.isClosed()){
            menu.getChildren().remove(btnInserirCandidaturas);
            menu.getChildren().remove(btnFechar);
        }
    }

}
