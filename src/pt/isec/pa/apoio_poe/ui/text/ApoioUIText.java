package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
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
    boolean isfinished = false;
    public ApoioUIText(ApoioContext context) {
        this.context = context;
        manager = context.getManager();
        flag = 0;
    }

    public void start(String[] args){
        if(!context.existsFileBin())
            if(args.length != 0){
                context.begin();
                context.gerirAlunos();
                System.out.println("Modo Debug");
                try {
                    context.addAluno("teste.csv");
                }catch (CollectionBaseException c){
                    System.out.println(c.getMessageOfExceptions());
                }
                context.recuarFase();

                context.gerirDocentes();

                try{
                    context.importDocentes("testeD.csv");
                }
                catch (CollectionBaseException c){
                    System.out.println(c.getMessageOfExceptions());
                }

                context.recuarFase();

                context.gerirEstagios();
                context.importPropostas("testeP.csv");
                context.recuarFase();

                context.avancarFase();
                try {
                    context.addCandidatura("cand.csv");
                }catch (CollectionBaseException c){
                    System.out.println(c.getMessageOfExceptions());
                }
                context.recuarFase();
            }



        while (!isfinished) {
            System.out.println(Log.getInstance().getAllMessage());
            switch (context.getState()) {
                case CONFIG_OPTIONS -> UIConfig_Options();
                case GESTAO_ALUNOS -> UIGestao_Clientes();
                case GESTAO_DOCENTES -> UIGestao_Docentes();
                case GESTAO_PROPOSTAS -> UIGestao_Estagios();
                case OPCOES_CANDIDATURA -> UIOpcoes_Candidatura();
                case ATRIBUICAO_PROPOSTAS -> UIAtribuicao_Propostas();
                case CONFLITO_ATRIBUICAO_CANDIDATURA -> UIConflito_Atribuicao_Candidatura();
                case ATRIBUICAO_ORIENTADORES -> UIAtribuicao_Orientadores();
                case GESTAO_ORIENTADORES -> UIGestao_Orientadores();
                case CONSULTA -> UIConsulta();
                case OBTENCAO_DADOS_ORIENTADORES -> UIObtencaoDadosOrientadores();
                case OBTENCAO_LISTA_PROPOSTA_ATRIBUICAO -> UIObtencaoDeListaDePropostaAtribuida();
                case OBTENCAO_LISTA_ALUNOS_PROPOSTAS -> UIObtencaoDeListaDeAlunoAtribuicao();
                case EDITAR_ALUNOS -> UIEditarAlunos(PAInput.readLong("Numero de Aluno"));
                case EDITAR_CANDIDATURAS -> UIEditarCandidaturas();
                case EDITAR_DOCENTES -> UIEditarDocentes();
                case EDITAR_PROPOSTAS -> UIEditarPropostas();
                case OBTENCAO_LISTA_ALUNOS -> UIObtencaoDeListaDeAluno();
                case OBTENCAO_LISTA_PROPOSTAS -> UIObtencaoDeListaDeProposta();
                case LOAD_STATE -> UILoadState();
                case SAIR -> UISair();
                default -> isfinished = true;

            }
        }
    }

    private void UISair() {
        switch(PAInput.chooseOption("Pretende guardar o estado da Aplicação?", "Sim", "Nao")){
            case 1 ->{
                try{
                    context.save();
                }catch (Exception e){
                    System.out.println(e.toString());
                }
            }
            case 2 -> {}
        }
        isfinished = true;
    }

    private void UILoadState() {
        if(context.existsFileBin()) {
            switch (PAInput.chooseOption("Pretende carregar o ultimo estado da Aplicação?", "Sim", "Nao")) {
                case 1 -> {
                    try {
                        context.load();
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
                case 2 -> context.begin();
            }
        }
        context.begin();
    }


    private void UIConfig_Options() {
        String [] opcoes;
        if(!context.isClosed()) { //Se nao esta fechado
            opcoes = new String[]{"Gestao de Alunos", "Gestão de Docentes", "Gestão de Propostas", "Fechar Fase", "Avançar Fase","Exit"};

        }
        else{
            opcoes = new String[]{"Gestao de Alunos", "Gestão de Docentes", "Gestão de Propostas", "Avançar Fase","Exit"};
        }
        switch (PAInput.chooseOption(context.getName(), opcoes)) {
            case 1 -> context.gerirAlunos();
            case 2 -> context.gerirDocentes();
            case 3 -> context.gerirEstagios();
            case 4 -> {
                if (context.isClosed()) {context.avancarFase();}
                else  {context.closeFase();}
            }
            case 5 -> {if (!context.isClosed()) context.avancarFase();
                        else context.sair();}
            case 6 -> context.sair();
        }
    }



    private void UIGestao_Clientes() {
        int option;
        if (!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Inserção Alunos Por Ficheiro CSV", "Exportar Alunos para CSV" ,"Consultar Alunos", "Editar Aluno", "Remover Aluno", "Voltar","Exit");
            switch (option) {
                case 1 -> {
                    try {
                        context.addAluno(PAInput.readString("Nome do ficheiro: ", true));
                    }catch (CollectionBaseException c){
                        System.out.println(c.getMessageOfExceptions());
                    }
                }
                case 2 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 3 -> System.out.println(context.getAlunosToString());
                case 4 -> context.editarAlunos();
                case 5 -> context.removeAluno(PAInput.readLong("Numero de Aluno"));
                case 6 -> context.recuarFase();
                case 7 -> context.sair();

            }
        } else {
            option = PAInput.chooseOption(context.getName(), "Exportar Alunos para CSV", "Consultar Alunos", "Voltar","Exit");
            switch (option) {
                case 1 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getAlunosToString());
                case 3 -> context.recuarFase();
                case 4 -> context.sair();
            }
        }
    }


    private void UIEditarAlunos(long nAluno) {

        switch (PAInput.chooseOption("Qual o campo a alterar", "Nome", "Curso",
                    "Ramo", "Classificacao", "Voltar", "Exit")){
            case 1 -> context.changeNameAluno(PAInput.readString("Novo nome", false), nAluno);
            case 2 -> context.changeCursoAluno(PAInput.readString("Novo curso", true), nAluno);
            case 3 -> context.changeRamoAluno(PAInput.readString("Novo ramo", true), nAluno);
            case 4 -> context.changeClassAluno(PAInput.readNumber("Nova classificaçao"), nAluno);
            case 5 -> context.recuarFase();
            case 6 -> context.sair();

        }
    }


    private void UIGestao_Docentes() {
        int option;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Importar Docentes por CSV", "Exportar Docentes para CSV", "Consultar Docentes", "Editar Docente", "Remover Docente", "Voltar", "Exit");
            switch (option) {
                case 1 -> {
                    try{
                        context.importDocentes(PAInput.readString("Nome do ficheiro: ", true));
                    }catch (CollectionBaseException c){
                        System.out.println(c.getMessageOfExceptions());
                    }

                }
                case 2 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 3 -> System.out.println(context.getDocentesToString());
                case 4 -> context.editarDocentes();
                case 5 -> context.removeDocente(PAInput.readString("Email do docente: ", true));
                case 6 -> context.recuarFase();
                case 7 -> context.sair();
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Exportar Docentes para CSV", "Consultar Docentes", "Voltar","Exit");
            switch (option) {
                case 1 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getDocentesToString());
                case 3 -> context.recuarFase();
                case 4 -> context.sair();
            }
        }
    }

    private void UIEditarDocentes() {
        String email = PAInput.readString("Email do docente: ", true);
        while (true) {
            switch (PAInput.chooseOption("Qual o campo a alterar", "Nome", "Voltar", "Exit")) {
                case 1 -> context.changeNomeDocente(PAInput.readString("Novo nome: ", false), email);
                case 2 -> context.recuarFase();
                case 3 -> context.sair();
            }

        }
    }

    private void UIGestao_Estagios() {
        int option;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Importar Estágios/Projetos","Exportar para CSV", "Consultar Estágio/Projeto", "Editar Estágio/Projeto",
                    "Remover Estágio/Projeto", "Voltar", "Exit");
            switch (option) {
                case 1 -> {
                    context.importPropostas(PAInput.readString("Nome do ficheiro: ", true));
                    while(Log.getInstance().hasNext()){
                        System.out.println(Log.getInstance().getMessage());
                    }
                }
                case 2 -> context.exportaCSV(PAInput.readString("Ficheiro: ", true));
                case 3 -> System.out.println(context.getPropostasToString());
                case 4 -> context.editarPropostas();
                case 5 -> context.removerProposta(PAInput.readString("Id Propostas: ",true));
                case 6 -> context.recuarFase();
                case 7 -> context.sair();
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Consultar Estágio/Projeto", "Exportar para CSV", "Voltar", "Exit");
            switch (option) {
                case 1 -> System.out.println(context.getPropostasToString());
                case 2 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 3 -> context.recuarFase();
                case 4 -> context.sair();
            }
        }
    }

    private void UIEditarPropostas() {
        //TODO
        switch  (PAInput.chooseOption(context.getName(), "Em construção", "Recuar Fase", "Exit")) {
            case 1 -> {}
            case 2 -> context.recuarFase();
            case 3 -> context.sair();
        }
    }

    private void UIOpcoes_Candidatura() {
        if(!context.isClosed()) { //Se nao esta fechado
            switch (PAInput.chooseOption(context.getName(), "Inserção de Candidaturas", "Exportar Candidaturas para CSV", "Consulta de Candidaturas", "Edição de Candidaturas",
                    "Obtencao de Listas de alunos", "Obtenção de listas de propostas de projecto/estágio", "Fechar Fase", "Recuar Fase", "Avançar Fase", "Exit")) {

                case 1 -> {
                    try {
                        context.addCandidatura(PAInput.readString("Ficheiro: ", true));
                    }catch (CollectionBaseException c){
                        System.out.println(c.getMessageOfExceptions());
                    }
                }
                case 2 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 3 -> System.out.println(context.getCandidaturas());
                case 4 -> context.editarCandidaturas();
                case 5 -> context.obtencaoListaAlunos();
                case 6 -> context.obtencaoListaProposta();
                case 7 -> context.closeFase();
                case 8 -> context.recuarFase();
                case 9 -> context.avancarFase();
                case 10 -> context.sair();
            }
        }
        else{
            switch  (PAInput.chooseOption(context.getName(), "Exportar Candidaturas para CSV", "Consulta de Candidaturas", "Obtencao de Listas de alunos",
                    "Obtenção de listas de propostas de projecto/estágio", "Recuar Fase", "Avançar Fase", "Exit")) {
                case 1 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getCandidaturas());
                case 3 -> context.obtencaoListaAlunos();
                case 4 -> context.obtencaoListaProposta();
                case 5 -> context.recuarFase();
                case 6 -> context.avancarFase();
                case 7 -> context.sair();
            }
        }
    }

    private void UIEditarCandidaturas() {

        switch (PAInput.chooseOption("Editar Candidatura", "Adicionar Propostas a candidatura", "Remover Propostas a candidatura",
                    "Voltar", "Exit")){
            case 1 ->context.addPropostaACandidatura(PAInput.readLong("Numero do Aluno: "),
                        PAInput.readString("Nome da Proposta: ", true));
            case 2 ->context.removePropostaACandidatura(PAInput.readLong("Numero do Aluno: "),
                        PAInput.readString("Nome da Proposta: ", true));
            case 3 -> context.recuarFase();
            case 4 -> context.sair();
        }

    }

    private void UIObtencaoDeListaDeProposta() {
        boolean sair = false;
        int []opcoes = new int[]{0,0,0,0};
        int num;
        String []filters = new String[] {"Autopropostas de alunos","Propostas de docentes","Propostas com candidaturas","Propostas sem candidatura", "Exit"};
        String []options = new String[]{"Autopropostas de alunos [ ]","Propostas de docentes [ ]","Propostas com candidaturas [ ]","Propostas sem candidatura [ ]", "Mostrar", "Voltar", "Exit"};
        while (!sair){
            num = 0;
            switch (PAInput.chooseOption("Filtros para obtenção de lista de proposta de projecto/estágio", options)){
                case 1 -> num = 1;
                case 2 -> num = 2;
                case 3 -> num = 3;
                case 4 -> num = 4;
                case 5 -> System.out.println(context.getPropostasWithFiltersToString(opcoes));
                case 6 -> {
                    context.recuarFase();
                    sair = true;
                }
                case 7 -> {
                    context.sair();
                    sair = true;
                }

            }
            selecionaFiltros(opcoes, num, filters, options);

        }
    }

    private void selecionaFiltros(int[] opcoes, int num, String[] filters, String[] options) {
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


    private void UIObtencaoDeListaDeAluno() {


        switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Com autoProposta", "Com candidatura já Registada", "Sem candidatura regista", "Voltar", "Exit")){
            case 1 -> System.out.println(context.obtencaoAlunosComAutoProposta());
            case 2 -> System.out.println(context.obtencaoAlunosComCandidatura());
            case 3 -> System.out.println(context.obtencaoAlunosSemCandidatura());
            case 4 -> context.recuarFase();
            case 5 -> context.sair();


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
        switch (PAInput.chooseOption(context.getName(), "Atribuição automática das autopropostas ou propostas de docentes",
                "Remoção manual de uma atribuição previamente realizada ou de todas as atribuições",
                "Obtenção de listas de alunos ",
                "Obtenção de listas de propostas de projecto estágio",
                "Exportar para ficheiro CSV os dados referentes aos alunos inscritos", "Undo", "Redo",
                "Fechar", "Recuar Fase", "Avançar Fase", "Exit")){
            case 1 -> context.atribuicaoAutomatica();
            case 2 -> manager.remocaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));
            case 3 -> context.obtencaoListaAlunos();
            case 4 -> context.obtencaoListaProposta();
            case 5 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 6 -> manager.undo();
            case 7 -> manager.redo();
            case 8 -> context.closeFase();
            case 9 -> context.recuarFase();
            case 10 -> context.avancarFase();
            case 11 -> context.sair();
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
                "Obtenção de listas de propostas de projecto estágio",
                "Exportar para ficheiro CSV os dados referentes aos alunos inscritos", "Undo", "Redo",
                "Fechar", "Recuar Fase", "Avançar Fase", "Sair"};

        if(flag == 0)
                option = PAInput.chooseOption(context.getName(), options);
        else{
            option = flag;
            flag = 0;
        }
        try {
            switch (option) {
                case 1 -> context.atribuicaoAutomatica(); // Atribuiçao automatica de projetos_estagios e projetos
                case 2 -> context.atribuicaoAutomaticaSemAtribuicoesDefinidas();
                case 3 -> manager.atribuicaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));
                case 4 -> manager.remocaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));
                case 5 -> context.obtencaoListaAlunos();
                case 6 -> context.obtencaoListaProposta();
                case 7 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 8 -> manager.undo();
                case 9 -> manager.redo();
                case 10 -> context.closeFase();
                case 11 -> context.recuarFase();
                case 12 -> context.avancarFase();
                case 13 -> context.sair();
            }
        }catch (ConflitoAtribuicaoAutomaticaException e){
            context.conflitoAtribuicaoCandidatura();
        }
    }

    private void UIAtribuicao_PropostasClosed() {
        switch (PAInput.chooseOption(context.getName(),"Obtenção de listas de alunos",
                "Obtenção de listas de propostas de projecto estágio",
                "Exportar para ficheiro CSV os dados referentes aos alunos inscritos", "Recuar Fase", "Avançar Fase", "Exit")){
            case 1 -> context.obtencaoListaAlunos();
            case 2 -> context.obtencaoListaProposta();
            case 3 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 4 -> context.recuarFase();
            case 5 -> context.avancarFase();
            case 6 -> context.sair();
        }
    }

    private void UIObtencaoDeListaDeAlunoAtribuicao() {

        switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Têm autoproposta associada", "Têm candidatura já registada", "Têm proposta atribuída",
                "Não têm qualquer proposta atribuída", "Voltar", "Exit")) {
            case 1 -> System.out.println(context.obtencaoAlunosComAutoPropostaAtribuida());
            case 2 -> System.out.println(context.obtencaoAlunosComCandidatura());
            case 3 -> System.out.println(context.getTodosAlunosComPropostaAtribuida());
            case 4 -> System.out.println(context.obtencaoAlunosSemProposta());
            case 5 -> context.recuarFase();
            case 6 -> context.sair();
        }


    }

    private void UIObtencaoDeListaDePropostaAtribuida() {
        boolean sair = false;
        int []opcoes = new int[]{0,0,0,0};
        int num;
        String []filters = new String[] {"Autopropostas de alunos","Propostas de docentes","Propostas não atribuídas","Propostas atribuídas"};
        String []options = new String[]{"Autopropostas de alunos [ ]","Propostas de docentes [ ]","Propostas não atribuídas [ ]","Propostas atribuídas [ ]", "Mostrar", "Voltar", "Exit"};

        while(!sair) {
            num = 0;
            switch (PAInput.chooseOption("Filtros para obtenção de lista de proposta de projecto/estágio", options)) {
                case 1 -> num = 1;
                case 2 -> num = 2;
                case 3 -> num = 3;
                case 4 -> num = 4;
                case 5 -> System.out.println(context.getPropostasWithFiltersToStringAtribuicao(opcoes));
                case 6 -> {
                    context.recuarFase();
                    sair = true;
                }
                case 7 ->{
                     context.sair();
                     sair = true;
                }

            }
            selecionaFiltros(opcoes, num, filters, options);
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
                "Exportar para CSV","Gestao de Orientadores", "Obtenção de dados de Orientadores", "Recuar Fase" ,"Fechar Fase e Avançar", "Exit")){
            case 1 -> context.associacaoAutomaticaDeDocentesAPropostas();
            case 2 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 3 -> context.gerirOrientadores();
            case 4 -> context.obtencaoDadosOrientador();
            case 5 -> context.recuarFase();
            case 6 -> context.closeFase();
            case 7 -> context.sair();
        }
    }

    private void UIGestao_Orientadores() {
        switch (PAInput.chooseOption(context.getName(),"Atribuir Orientador", "Consultar Orientadores", "Alterar Docente","Eliminar Orientador","Voltar", "Exit")){
            case 1 -> manager.atribuirOrientador(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 2 -> System.out.println(context.getAlunosComPropostaEOrientador());
            case 3 -> manager.alterarDocente(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 4 -> manager.removerDocente(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 5 -> context.recuarFase();
            case 6 -> context.sair();
        }
    }

    private void UIObtencaoDadosOrientadores() {


        switch (PAInput.chooseOption("Obtenção de dados diversos sobre atribuição de orientadores",
                    "Lista de estudantes com proposta atribuída e com orientador associado",
                    "Lista de estudantes com proposta atribuída mas sem orientador associado",
                    "Número de orientações por docente, em média, mínimo, máximo, e por docente especificado",
                    "Voltar", "Exit")){
            case 1 -> System.out.println(context.getAlunosComPropostaEOrientador());
            case 2 -> System.out.println(context.getAlunosComPropostaESemOrientador());
            case 3 -> System.out.println(context.getEstatisticasPorDocente());
            case 4 -> context.recuarFase();
            case 5 -> context.sair();


        }
    }

    private void UIConsulta() {
        switch (PAInput.chooseOption(context.getName(), "Exportar para CSV","Lista de alunos com propostas atribuídas",
                "Lista de estudantes sem propostas atribuídas e com opções de candidatura",
                "Conjunto de propostas disponíveis", "Conjunto de propostas atribuídas",
                "Número de orientações por docente, em média, mínimo, máximo, e por docente especificado.", "Exit")){
            case 1 -> context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 2 -> System.out.println(context.getTodosAlunosComPropostaAtribuida());
            case 3 -> System.out.println(context.obtencaoAlunosSemPropostaComCandidatura());
            case 4 -> System.out.println(context.getPropostasDisponiveis());
            case 5 -> System.out.println(context.getPropostasAtribuidas());
            case 6 -> System.out.println(context.getEstatisticasPorDocente());
            case 7 -> context.sair();
        }
    }



}
