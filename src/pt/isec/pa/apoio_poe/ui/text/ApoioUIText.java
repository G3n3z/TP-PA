package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.utils.PAInput;

public class ApoioUIText {
    private ApoioContext context;
    private ApoioManager manager;
    private int flag;

    public ApoioUIText(ApoioContext context) {
        this.context = context;
        manager = context.getManager();
        flag = 0;
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
                case CONFLITO_ATRIBUICAO_CANDIDATURA -> UIConflito_Atribuicao_Candidatura();
                case ATRIBUICAO_ORIENTADORES -> UIAtribuicao_Orientadores();
                case GESTAO_ORIENTADORES -> UIGestao_Orientadores();
                default -> isfinished = true;

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
        int option;
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
                case 3 -> System.out.println(context.getAlunos());
                case 4 -> UIEditarAlunos(PAInput.readLong("Numero de Aluno"));
                case 5 -> context.removeAluno(PAInput.readLong("Numero de Aluno"));
                case 6 -> context.recuarFase();

            }
        } else {
            option = PAInput.chooseOption(context.getName(), "Consultar Alunos", "Voltar");
            switch (option) {
                case 1 -> System.out.println(context.getAlunos());
                case 2 -> context.recuarFase();
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
        int option;
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
                case 3 -> System.out.println(context.getDocentes());
                case 4 -> UIEditarDocentes(PAInput.readString("Email do docente: ", true));
                case 5 -> context.removeDocente(PAInput.readString("Email do docente: ", true));

                case 6 -> context.recuarFase();
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Consultar Docentes", "Voltar");
            switch (option) {
                case 1 -> System.out.println(context.getDocentes());
                case 2 -> context.recuarFase();
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
        int option;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Importar Estágios/Projetos", "Inserir Estágio/Projeto", "Consultar Estágio/Projeto", "Editar Estágio/Projeto", "Remover Estágio/Projeto", "Voltar");
            switch (option) {
                case 1 -> {
                    context.importPropostas(PAInput.readString("Nome do ficheiro: ", true));
                    while(Log.getInstance().hasNext()){
                        System.out.println(Log.getInstance().getMessage());
                    }
                }
                case 3 -> System.out.println(context.getPropostas());
                case 6 -> context.recuarFase();
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Consultar Estágio/Projeto", "Voltar");
            switch (option) {
                case 1 -> System.out.println(context.getPropostas());
                case 2 -> context.recuarFase();
            }
        }
    }

    private void UIOpcoes_Candidatura() {
        if(!context.isClosed()) { //Se nao esta fechado
            switch (PAInput.chooseOption(context.getName(), "Inserção de Propostas", "Consulta de Propostas", "Edição de Propostas",
                    "Obtencao de Listas de alunos", "Obtenção de listas de propostas de projecto/estágio", "Fechar Fase", "Recuar Fase", "Avançar Fase")) {

                case 1 -> context.addCandidatura(PAInput.readString("Ficheiro: ", true));
                case 2 -> System.out.println(context.getCandidaturas());
                case 3 -> UIEditarCandidaturas();
                case 4 -> UIObtencaoDeListaDeAluno();
                case 5 -> UIObtencaoDeListaDeProposta();
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

    private void UIEditarCandidaturas() {
        boolean sair = false;
        while (!sair){
            switch (PAInput.chooseOption("Editar Candidatura", "Adicionar Propostas a candidatura", "Remover Propostas a candidatura",
                    "Voltar")){
                case 1 ->context.addPropostaACandidatura(PAInput.readLong("Numero do Aluno: "),
                        PAInput.readString("Nome da Proposta: ", true));
                case 2 ->context.removePropostaACandidatura(PAInput.readLong("Numero do Aluno: "),
                        PAInput.readString("Nome da Proposta: ", true));
                case 3 -> sair = true;
            }
        }
    }

    private void UIObtencaoDeListaDeProposta() {
        boolean sair = false;
        int []opcoes = new int[]{0,0,0,0};
        int num;
        String []filters = new String[] {"Autopropostas de alunos","Propostas de docentes","Propostas com candidaturas","Propostas sem candidatura"};
        String []options = new String[]{"Autopropostas de alunos [ ]","Propostas de docentes [ ]","Propostas com candidaturas [ ]","Propostas sem candidatura [ ]", "Mostrar", "Voltar"};
        while (!sair){
            num = 0;
            switch (PAInput.chooseOption("Filtros para obtenção de lista de proposta de projecto/estágio", options)){
                case 1 -> num = 1;
                case 2 -> num = 2;
                case 3 -> num = 3;
                case 4 -> num = 4;
                case 5 -> System.out.println(context.getPropostasWithFiltersToString(opcoes));
                case 6 -> sair = true;

            }
            if(num!= 0){
                if (opcoes[num - 1] == 0) {
                    opcoes[num - 1] = num;
                    options[num - 1] = filters[num - 1].concat(" [X]");
                } else {
                    opcoes[num - 1] = 0;
                    options[num - 1] = filters[num - 1].concat(" [ ]");
                }
            }

        }
    }


    private void UIObtencaoDeListaDeAluno() {
        boolean sair = false;
        while(!sair){
            switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Com autoProposta", "Com candidatura já Registada", "Sem candidatura regista", "Voltar")){
                case 1 -> System.out.println(context.obtencaoAlunosComAutoProposta());
                case 2 -> System.out.println(context.obtencaoAlunosComCandidatura());
                case 3 -> System.out.println(context.obtencaoAlunosSemCandidatura());
                case 4 -> sair = true;
            }

        }
    }


    private void UIAtribuicao_Propostas() {
        if(context.isClosed()){ //Se o estado de atribuição de propostas está fechado
            UIAtribuicao_PropostasClosed();
        }
        else {
            if(context.getBooleanState(EnumState.OPCOES_CANDIDATURA)){ //Se atribuicao de propostas esta aberto mas o anterior está fechado
                UIAtribuicao_PropostasComAnteriorFechada();
            }
            else {
                UIAtribuicao_PropostasSemAnteriorFechada();
            }
        }

    }

    private void UIAtribuicao_PropostasSemAnteriorFechada() {
        switch (PAInput.chooseOption(context.getName(), "Atribuição automática das autopropostas ou propostas de docentes", "Recuar Fase", "Avançar Fase")){
            case 1 -> context.atribuicaoAutomatica();
            case 2 -> context.recuarFase();
            case 3 -> context.avancarFase();
        }
    }

    private void UIAtribuicao_PropostasComAnteriorFechada() {
        //Flag para voltar a executar
        int option;
        String []options = new String[]{"Atribuição automática das autopropostas ou propostas de docentes",
                "Atribuição automática de uma proposta disponível aos alunos ainda sem atribuições definidas",
                "Atribuição manual de propostas disponíveis aos alunos",
                "Remoção manual de uma atribuição previamente realizada ou de todas as atribuições",
                "Obtenção de listas de alunos ",
                "Obtenção de listas de propostas de projecto estágio", "Undo", "Redo",
                "Fechar", "Recuar Fase", "Avançar Fase"};
        if(flag == 0)
                option = PAInput.chooseOption(context.getName(), options);
        else{
            option = flag;
            flag = 0;
        }
        try {
            switch (option) {
                case 1 -> context.atribuicaoAutomatica(); // Atribuiçao automatica de projetos_estagios e projetos TODO
                case 2 -> context.atribuicaoAutomaticaSemAtribuicoesDefinidas();
                case 3 -> {manager.atribuicaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));}
                case 4 -> {manager.remocaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));}
                case 5 -> UIObtencaoDeListaDeAlunoAtribuicao();
                case 6 -> UIObtencaoDeListaDePropostaAtribuida();
                case 7 -> manager.undo();
                case 8 -> manager.redo();
                case 9 -> context.closeFase();
                case 10 -> context.recuarFase();
                case 11 -> context.avancarFase();
            }
        }catch (ConflitoAtribuicaoAutomaticaException e){
            context.conflitoAtribuicaoCandidatura();
        }
    }

    private void UIAtribuicao_PropostasClosed() {
        switch (PAInput.chooseOption(context.getName(),"Obtenção de listas de alunos",
                "Obtenção de listas de propostas de projecto estágio",
                "Recuar Fase", "Avançar Fase")){
            case 1 -> UIObtencaoDeListaDeAlunoAtribuicao();
            case 2 -> UIObtencaoDeListaDePropostaAtribuida();
            case 3 -> context.recuarFase();
            case 4 -> context.avancarFase();
        }
    }

    private void UIObtencaoDeListaDeAlunoAtribuicao() {
        boolean sair = false;
        while(!sair){
            switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Têm autoproposta associada", "Têm candidatura já registada", "Têm proposta atribuída", "Não têm qualquer proposta atribuída", "Voltar")){
                case 1 -> System.out.println(context.obtencaoAlunosComAutoPropostaAtribuida());
                case 2 -> System.out.println(context.obtencaoAlunosComCandidatura());
                case 3 -> System.out.println(context.getTodosAlunosComPropostaAtribuida());
                case 4 -> System.out.println(context.obtencaoAlunosSemProposta());
                case 5 -> sair = true;
            }

        }
    }

    private void UIObtencaoDeListaDePropostaAtribuida() {
        boolean sair = false;
        int []opcoes = new int[]{0,0,0,0};
        int num;
        String []filters = new String[] {"Autopropostas de alunos","Propostas de docentes","Propostas não atribuídas","Propostas atribuídas"};
        String []options = new String[]{"Autopropostas de alunos [ ]","Propostas de docentes [ ]","Propostas não atribuídas [ ]","Propostas atribuídas [ ]", "Mostrar", "Voltar"};
        while (!sair){
            num = 0;
            switch (PAInput.chooseOption("Filtros para obtenção de lista de proposta de projecto/estágio", options)){
                case 1 -> num = 1;
                case 2 -> num = 2;
                case 3 -> num = 3;
                case 4 -> num = 4;
                case 5 -> System.out.println(context.getPropostasWithFiltersToStringAtribuicao(opcoes));
                case 6 -> sair = true;

            }
            if(num!= 0){
                if (opcoes[num - 1] == 0) {
                    opcoes[num - 1] = num;
                    options[num - 1] = filters[num - 1].concat(" [X]");
                } else {
                    opcoes[num - 1] = 0;
                    options[num - 1] = filters[num - 1].concat(" [ ]");
                }
            }

        }
    }

    private void UIConflito_Atribuicao_Candidatura() {
        System.out.println(context.getConflitoToString());
        if(context.existConflict()){
            UIConflito_Atribuicao_Candidatura_Exist_Conflict();
        }else {
            context.recuarFase();
            flag = 2;
        }

    }


    private void UIConflito_Atribuicao_Candidatura_Exist_Conflict() {
        switch (PAInput.chooseOption("Conflito na Atribuição de Candidatuas","Consultar Dados de Alunos em conflito",
                "Consultar Dados da Proposta", "Atribuir a Aluno")){
            case 1 -> System.out.println(context.consultaAlunosConflito());
            case 2 -> System.out.println(context.consultaPropostaConflito());
            case 3 ->context.resolveConflito(PAInput.readLong("Numero do Aluno:"));
        }
    }

    private void UIAtribuicao_Orientadores() {
        switch (PAInput.chooseOption(context.getName(),"Associação automática dos docentes proponentes de projetos como orientador dos mesmos",
                "Gestao de Orientadores", "Obtenção de dados de Orientadores", "Recuar Fase" ,"Fechar Fase e Avançar")){
            case 1 -> context.associacaoAutomaticaDeDocentesAPropostas();
            case 2 -> context.gerirOrientadores();
            case 3 -> UIObtencaoDadosOrientadores();
            case 4 -> {}
            case 5 -> {}
        }
    }

    private void UIGestao_Orientadores() {
        switch (PAInput.chooseOption(context.getName(),"Atribuir Orientador", "Consultar Orientadores", "Alterar Docente","Eliminar Orientador","Voltar")){
            case 1 -> manager.atribuirOrientador(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 2 -> context.getAlunosComPropostaEOrientador();
            case 3 -> manager.alterarDocente(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 4 -> manager.removerDocente(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 5 -> context.recuarFase();
        }
    }

    private void UIObtencaoDadosOrientadores() {
        boolean sair = false;
        while (!sair){
            switch (PAInput.chooseOption("Obtenção de dados diversos sobre atribuição de orientadores",
                    "Lista de estudantes com proposta atribuída e com orientador associado",
                    "lista de estudantes com proposta atribuída mas sem orientador associado",
                    "número de orientações por docente, em média, mínimo, máximo, e por docente especificado",
                    "Voltar")){
                case 1 -> System.out.println(context.getAlunosComPropostaEOrientador());
                case 2 -> System.out.println(context.getAlunosComPropostaESemOrientador());
                case 3 -> System.out.println(context.getEstatisticasPorDocente());
                case 4 -> sair = true;

            }
        }
    }
}
