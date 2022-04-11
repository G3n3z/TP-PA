package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.utils.PAInput;

public class ApoioUIText {
    private ApoioContext context;

    public ApoioUIText(ApoioContext context) {
        this.context = context;
    }

    public void start(String[] args){
        if(args.length != 0){
            context.gerirAlunos();
            System.out.println("Modo Debug");
            context.addAluno("teste.csv");
            context.recuarFase();

            context.gerirDocentes();
            context.importDocentes("testeD.csv");
            context.recuarFase();

            context.gerirEstagios();
            context.importPropostas("testeP.csv");
            context.recuarFase();

            context.avancarFase();
            context.addCandidatura("testeC.csv");
            context.recuarFase();
        }


        boolean isfinished = false;
        while (!isfinished) {
            System.out.println(Log.getInstance().getAllMessage());
            switch (context.getState()) {
                case CONFIG_OPTIONS -> UIConfig_Options();
                case GESTAO_CLIENTES -> UIGestao_Clientes();
                case GESTAO_DOCENTES -> UIGestao_Docentes();
                case GESTAO_PROPOSTAS -> UIGestao_Estagios();
                case OPCOES_CANDIDATURA -> UIOpcoes_Candidatura();
                case ATRIBUICAOPROPOSTAS -> UIAtribuicao_Propostas();
                default -> {
                    isfinished = true;
                }
            }
        }
    }


    private void UIConfig_Options() {
        String [] opcoes;
        if(!context.isClosed()) { //Se nao esta fechado
            opcoes = new String[]{"Gestao de Alunos", "Gestão de Docentes", "Gestão de Estágios", "Fechar Fase", "Avançar Fase"};

        }
        else{
            opcoes = new String[]{"Gestao de Alunos", "Gestão de Docentes", "Gestão de Estágios", "Avançar Fase"};
        }
        switch (PAInput.chooseOption(context.getName(), opcoes)) {
            case 1 -> context.gerirAlunos();
            case 2 -> context.gerirDocentes();
            case 3 -> context.gerirEstagios();
            case 4 -> {
                if (context.isClosed()) {context.avancarFase();}
                else  {context.closeFase();}
            }
            case 5 -> {if (!context.isClosed()) context.avancarFase();}
        }
    }



    private void UIGestao_Clientes() {
        int option = 0;
        if (!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Inserção Alunos Por Ficheiro CSV", "Inserir aluno manualmente", "Consultar Alunos", "Editar Aluno", "Remover Aluno", "Voltar");
            switch (option) {
                case 1 -> {
                    context.addAluno(PAInput.readString("Nome do ficheiro: ", true));
                    while(Log.getInstance().hasNext()){
                        System.out.println(Log.getInstance().getMessage());
                    }

                }
                case 2 -> {}
                case 3 -> {System.out.println(context.getAlunos());}
                case 4 -> {UIEditarAlunos(PAInput.readLong("Numero de Aluno"));}
                case 5 -> {context.removeAluno(PAInput.readLong("Numero de Aluno"));}
                case 6 -> {
                    context.recuarFase();
                }
            }
        } else {
            option = PAInput.chooseOption(context.getName(), "Consultar Alunos", "Voltar");
            switch (option) {
                case 1 -> {
                }
                case 2 -> {
                    context.recuarFase();
                }
            }
        }
    }


    private void UIEditarAlunos(long nAluno) {
        boolean sair = true;
        while(sair){
            switch (PAInput.chooseOption("Qual o campo a alterar", "Nome", "Curso",
                    "Ramo", "Classificacao", "Voltar")){
                case 1 -> context.changeNameAluno(PAInput.readString("Novo nome", false), nAluno);
                case 2 -> context.changeCursoAluno(PAInput.readString("Novo curso", true), nAluno);
                case 3 -> context.changeRamoAluno(PAInput.readString("Novo ramo", true), nAluno);
                case 4 -> context.changeClassAluno(PAInput.readNumber("Nova classificaçao"), nAluno);
                default -> sair = false;
            }
        }
    }


    private void UIGestao_Docentes() {
        int option = 0;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Importar Docentes por CSV", "Inserir Docente", "Consultar Docentes", "Editar Docente", "Remover Docente", "Voltar");
            switch (option) {
                case 1 -> {
                    context.importDocentes(PAInput.readString("Nome do ficheiro: ", true));
                    while(Log.getInstance().hasNext()){
                        System.out.println(Log.getInstance().getMessage());
                    }
                }
                case 2 -> {
                    //Inserir manual
                }
                case 3 -> { System.out.println(context.getDocentes()); }
                case 4 -> { UIEditarDocentes(PAInput.readString("Email do docente: ", true));
                }
                case 5 -> {
                    context.removeDocente(PAInput.readString("Email do docente: ", true));
                }
                case 6 -> { context.recuarFase(); }
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Consultar Docentes", "Voltar");
            switch (option) {
                case 1 -> { System.out.println(context.getDocentes()); }
                case 2 -> { context.recuarFase(); }
            }
        }
    }

    private void UIEditarDocentes(String email) {
        boolean sair = true;
        while(sair){
            if (PAInput.chooseOption("Qual o campo a alterar", "Nome", "Voltar") == 1) {
                context.changeNomeDocente(PAInput.readString("Novo nome: ", false), email);
            } else {
                sair = false;
            }
        }
    }

    private void UIGestao_Estagios() {
        int option = 0;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Importar Estágios/Projetos", "Inserir Estágio/Projeto", "Consultar Estágio/Projeto", "Editar Estágio/Projeto", "Remover Estágio/Projeto", "Voltar");
            switch (option) {
                case 1 -> {
                    context.importPropostas(PAInput.readString("Nome do ficheiro: ", true));
                    while(Log.getInstance().hasNext()){
                        System.out.println(Log.getInstance().getMessage());
                    }
                }
                case 3 -> { System.out.println(context.getPropostas()); }
                case 6 -> { context.recuarFase(); }
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Consultar Estágio/Projeto", "Voltar");
            switch (option) {
                case 1 -> { System.out.println(context.getPropostas()); }
                case 2 -> { context.recuarFase(); }
            }
        }
    }

    private void UIOpcoes_Candidatura() {
        if(!context.isClosed()) { //Se nao esta fechado
            switch (PAInput.chooseOption(context.getName(), "Inserção de Propostas", "Consulta de Propostas", "Edição de Propostas",
                    "Obtencao de Listas de alunos", "Obtenção de listas de propostas de projecto/estágio", "Fechar Fase", "Recuar Fase", "Avançar Fase")) {
                case 1 -> {context.addCandidatura(PAInput.readString("Ficheiro: ", true));}
                case 2 -> {System.out.println(context.getCandidaturas());}
                case 3 -> {UIObtencaoDeListaDeAluno();}
                case 4 -> {}
                case 5 -> {}
                case 6 -> context.closeFase();
                case 7 -> context.recuarFase();
                case 8 -> context.avancarFase();
            }
        }
        else{
            switch  (PAInput.chooseOption(context.getName(), "Avançar Fase", "Recuar Fase")) {
                case 1 -> context.avancarFase();
                case 2 -> context.recuarFase();
            }
        }
    }

    private void UIObtencaoDeListaDeAluno() {
        boolean sair = false;
        while(sair){
            switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Com autoProposta", "Com candidatura já Registada", "Sem candidatura regista")){
                case 1 -> {}
            }

        }
    }


    private void UIAtribuicao_Propostas() {
        switch (PAInput.chooseOption(context.getName(), "Recuar Fase", "Fechar Fase", "Avançar Fase")){
            case 1 -> context.recuarFase();
            case 2 -> context.closeFase();
            case 3 -> context.avancarFase();
        }
    }



}
