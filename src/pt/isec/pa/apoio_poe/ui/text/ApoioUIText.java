package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.utils.PAInput;

public class ApoioUIText {
    private ApoioContext context;

    public ApoioUIText(ApoioContext context) {
        this.context = context;
    }
    public void start(){
        boolean isfinished = false;
        while (!isfinished) {
            switch (context.getState()) {
                case CONFIG_OPTIONS -> UIConfig_Options();
                case GESTAO_CLIENTES -> UIGestao_Clientes();
                case GESTAO_DOCENTES -> UIGestao_Docentes();
                case GESTAO_ESTAGIOS -> UIGestao_Estagios();
                case OPCOES_CANDIDATURA -> UIOpcoes_Candidatura();
                default -> {
                    isfinished = true;
                }
            }
        }
    }


    private void UIConfig_Options() {
        if(!context.fechado()) { //Se nao esta fechado
            switch (PAInput.chooseOption(context.getName(), "Gestao de Alunos", "Gestão de Docentes", "Gestão de Estágios", "Fechar Fase", "Avançar Fase")) {
                case 1 -> context.gerirAlunos();
                case 2 -> context.gerirDocentes();
                case 3 -> context.gerirEstagios();
                case 4 -> context.fecharFase();
                case 5 -> context.avancarFase();
            }
        }
        else{
            if (PAInput.chooseOption(context.getName(), "Avançar Fase") == 1) {
                context.avancarFase();
            }
        }
    }
    private void UIGestao_Clientes() {
        int option = 0;
        option = PAInput.chooseOption(context.getName(), "Inserir Aluno", "Consultar Alunos", "Editar Aluno", "Remover Aluno", "Voltar");
        switch (option) {
            case 1 -> {}
            case 2 -> {}
            case 3 -> {}
        }

        context.recuarFase();
    }

    private void UIGestao_Docentes() {
        int option = 0;
        option = PAInput.chooseOption(context.getName(), "Inserir Docente", "Consultar Docentes", "Editar Docente" ,"Remover Docente", "Voltar");
        switch (option) {
            case 1 -> {}
            case 2 -> {}
            case 3 -> {}
        }

        context.recuarFase();
    }

    private void UIGestao_Estagios() {
        int option = 0;


        option = PAInput.chooseOption(context.getName(), "Inserir Estágio/Projeto", "Consultar Estágio/Projeto", "Editar Estágio/Projeto" ,"Remover Estágio/Projeto", "Voltar");
        switch (option) {
            case 1 -> {}
            case 2 -> {}
            case 3 -> {}
        }
        context.recuarFase();
    }

    private void UIOpcoes_Candidatura() {
        if(!context.fechado()) { //Se nao esta fechado
            switch (PAInput.chooseOption(context.getName(), "Inserção de Propostas", "Consulta de Propostas", "Edição de Propostas",
                    "Obtencao de Listas de alunos", "Obtenção de listas de propostas de projecto/estágio", "Fechar Fase", "Avançar Fase")) {
                case 1 -> {}
                case 2 -> {}
                case 3 -> {}
                case 4 -> {}
                case 5 -> {}
            }
        }
        else{
            if (PAInput.chooseOption(context.getName(), "Avançar Fase") == 1) {
                context.avancarFase();
            }
        }
    }



}
