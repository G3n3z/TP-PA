package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.Exceptions.BaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.utils.PAInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApoioUIText {
    private ApoioContext context;
    private ApoioManager manager;
    private BaseException exception = null;
    boolean isfinished = false;
    public ApoioUIText(ApoioContext context) {
        this.context = context;
        manager = context.getManager();
    }

    public void start(String[] args){


        while (!isfinished) {

            //System.out.println(MessageCenter.getInstance().getAllMessage());

                switch (context.getState()) {
                case CONFIG_OPTIONS -> UIConfig_Options();
                case GESTAO_ALUNOS -> UIGestao_Alunos();
                case GESTAO_DOCENTES -> UIGestao_Docentes();
                case GESTAO_PROPOSTAS -> UIGestao_Estagios();
                case OPCOES_CANDIDATURA -> UIOpcoes_Candidatura();
                case ATRIBUICAO_PROPOSTAS -> UIAtribuicao_Propostas();
                case ATRIBUICAO_MANUAL_PROPOSTAS -> UIAtribuicaoManualPropostas();
                case CONFLITO_ATRIBUICAO_CANDIDATURA -> UIConflito_Atribuicao_Candidatura();
                case ATRIBUICAO_ORIENTADORES -> UIAtribuicao_Orientadores();
                case GESTAO_ORIENTADORES -> UIGestao_Orientadores();
                case CONSULTA -> UIConsulta();
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
                    System.out.println(e);
                }
            }
            case 2 -> {}
        }
        isfinished = true;
    }

    private void UILoadState() {
        if(context.existsFileBin()) {
            switch (PAInput.chooseOption("Menu", "Começar", "Carregar Ultimo Ficheiro Guardado", "Sair")) {
                case 1 -> context.begin();
                case 2 -> {
                    try {
                        context.load();
                        manager = context.getManager();
                    }catch (Exception e){
                        System.out.println("Nao foi possivel carregar o ficheiro");
                    }
                    context.begin();
                }
                case 3 -> isfinished = true;
            }
        }
        else {
/*            switch (PAInput.chooseOption("Menu", "Começar", "Sair")) {
                case 1 -> context.begin();
                case 2 -> isfinished = true;
            }*/
            switch (PAInput.chooseOption("Menu", "Começar", "Sair", "Modo Debug")) {
                case 1 -> context.begin();
                case 2 -> isfinished = true;
                case 3 -> {
                    context.begin();
                    context.gerirAlunos();
                    System.out.println("Modo Debug");
                    try {
                        context.addAluno("teste.csv");
                    } catch (CollectionBaseException c) {
                        System.out.println(c.getMessageOfExceptions());
                    }
                    context.recuarFase();

                    context.gerirDocentes();

                    try {
                        context.importDocentes("testeD.csv");
                    } catch (CollectionBaseException c) {
                        System.out.println(c.getMessageOfExceptions());
                    }

                    context.recuarFase();
                    context.gerirEstagios();
                    try {
                        context.importPropostas("testeP.csv");
                    } catch (CollectionBaseException c) {
                        System.out.println(c.getMessageOfExceptions());
                    }
                    context.recuarFase();
                    context.avancarFase();
                    try {
                        context.addCandidatura("testeC.csv");
                    } catch (CollectionBaseException c) {
                        System.out.println(c.getMessageOfExceptions());
                    }
                    context.recuarFase();
                }
            }
        }
    }


    private void UIConfig_Options() {
        ErrorCode error = ErrorCode.E0;
        if(!context.isClosed()) { //Se nao esta fechado
            switch(PAInput.chooseOption(context.getName(),"Gestao de Alunos", "Gestão de Docentes", "Gestão de Propostas", "Fechar Fase", "Avançar Fase","Exit")){
                case 1 -> context.gerirAlunos();
                case 2 -> context.gerirDocentes();
                case 3 -> context.gerirEstagios();
                case 4 -> error = context.closeFase();
                case 5 -> context.avancarFase();
                case 6 -> context.sair();
            }
        }
        else{
            switch (PAInput.chooseOption(context.getName(), "Gestao de Alunos", "Gestão de Docentes", "Gestão de Propostas", "Avançar Fase","Exit")) {
                case 1 -> context.gerirAlunos();
                case 2 -> context.gerirDocentes();
                case 3 -> context.gerirEstagios();
                case 4 -> context.avancarFase();
                case 5 -> context.sair();
            }
        }
        System.out.println(errorReport(error));
    }


    private void UIGestao_Alunos() {
        int option;
        ErrorCode error = ErrorCode.E0;
        if (!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Inserir aluno manualmente","Inserção Alunos Por Ficheiro CSV", "Exportar Alunos para CSV",
                    "Consultar Alunos", "Editar Aluno", "Remover Aluno", "Remover todos os Alunos", "Voltar","Exit");

            switch (option) {
                case 1 -> error = context.insereAluno(new Aluno(PAInput.readString("Email:", true), PAInput.readString("Nome", false), PAInput.readLong("N.Aluno"),
                        PAInput.readString("Curso", true), PAInput.readString("Ramo", true), PAInput.readNumber("Classificacao"),
                        PAInput.readString("Possibilidade de Estagio", true).equalsIgnoreCase("true")));
                case 2 -> {
                    try {
                        context.addAluno(PAInput.readString("Nome do ficheiro: ", true));
                    }catch (CollectionBaseException c){
                        System.out.println(c.getMessageOfExceptions());
                    }
                }
                case 3 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 4 -> System.out.println(context.getAlunosToString());
                case 5 -> UIEditarAlunos();
                case 6 -> error = context.removeAluno(PAInput.readLong("Numero de Aluno: "));
                case 7 -> context.removeAll();
                case 8 -> context.recuarFase();
                case 9 -> context.sair();
            }
        } else {
            option = PAInput.chooseOption(context.getName(), "Exportar Alunos para CSV", "Consultar Alunos", "Voltar","Exit");
            switch (option) {
                case 1 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getAlunosToString());
                case 3 -> context.recuarFase();
                case 4 -> context.sair();
            }
        }
        System.out.println(errorReport(error));
    }


    private void UIEditarAlunos() {
        ErrorCode error;
        boolean sair = false;
        while(!sair) {
            error = ErrorCode.E0;
            switch (PAInput.chooseOption("Qual o campo a alterar", "Nome", "Curso",
                    "Ramo", "Classificacao", "Possibilidade ", "Voltar")) {
                case 1 -> error = context.changeNameAluno(PAInput.readLong("Numero de Aluno: "), PAInput.readString("Novo nome: ", false));
                case 2 -> error = context.changeCursoAluno(PAInput.readLong("Numero de Aluno: "), PAInput.readString("Novo curso: ", true));
                case 3 -> error = context.changeRamoAluno(PAInput.readLong("Numero de Aluno: "), PAInput.readString("Novo ramo: ", true));
                case 4 -> error = context.changeClassAluno(PAInput.readLong("Numero de Aluno: "), PAInput.readNumber("Nova classificaçao: "));
                case 5 -> error = context.changePossibilidadeAluno(PAInput.readLong("Numero de Aluno: "));   // TODO: changePossibilidade
                case 6 -> sair = true;
            }
            System.out.println(errorReport(error));
        }
    }


    private void UIGestao_Docentes() {
        int option;
        ErrorCode error = ErrorCode.E0;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Inserir Docente","Importar Docentes por CSV", "Exportar Docentes para CSV",
                    "Consultar Docentes", "Editar Docente", "Remover Docente","Remover todos os Docentes", "Voltar", "Exit");
            switch (option) {
                case 1 -> context.insereDocente(PAInput.readString("Email:", true), PAInput.readString("Nome", false));
                case 2 -> {
                    try{
                        context.importDocentes(PAInput.readString("Nome do ficheiro: ", true));
                    }catch (CollectionBaseException c){
                        System.out.println(c.getMessageOfExceptions());
                    }

                }
                case 3 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 4 -> System.out.println(context.getDocentesToString());
                case 5 -> UIEditarDocentes();
                case 6 -> error = context.removeDocente(PAInput.readString("Email do docente: ", true));
                case 7 -> context.removeAll();
                case 8 -> context.recuarFase();
                case 9 -> context.sair();
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Exportar Docentes para CSV", "Consultar Docentes", "Voltar","Exit");
            switch (option) {
                case 1 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getDocentesToString());
                case 3 -> context.recuarFase();
                case 4 -> context.sair();
            }
        }
        System.out.println(errorReport(error));
    }

    private void UIEditarDocentes() {
        ErrorCode error;
        boolean sair = false;
        while (!sair) {
            error = ErrorCode.E0;
            switch (PAInput.chooseOption("Qual o campo a alterar", "Nome", "Voltar")) {
                case 1 ->
                        error = context.changeNomeDocente(PAInput.readString("Email do docente: ", true), PAInput.readString("Novo nome: ", false));
                case 2 -> sair = true;
            }
            System.out.println(errorReport(error));
        }
    }

    private void UIGestao_Estagios() {
        int option;
        ErrorCode error = ErrorCode.E0;
        if(!context.isClosed()) {
            option = PAInput.chooseOption(context.getName(), "Importar Estagio/Projeto","Importar Estágios/Projetos por CSV","Exportar para CSV ", "Consultar Estágio/Projeto", "Editar Estágio/Projeto",
                    "Remover Estágio/Projeto", "Remover todos Estágios/Projetos", "Voltar", "Exit");
            switch (option) {
                case 1 -> {

                    String id = PAInput.readString("Id Proposta: ", true);
                    String tipo = PAInput.readString("Tipo: ", true);
                    Scanner sc = new Scanner(System.in);
                    String ramo;
                    List<String> ramos= new ArrayList<>();
                    System.out.println("Ramo:");
                    ramo = sc.next();

                    while(!ramo.equals("x") ){
                        System.out.println("Ramo: - Digite x para terminar de inserir");
                        ramos.add(ramo);
                        ramo = sc.next();
                    }

                    switch (tipo){
                        case "T1" ->  error = context.insereProposta(tipo, id, ramos,PAInput.readString("Titulo: ", false), PAInput.readString("Email Docente: ", true),
                                PAInput.readString("Entidade: ", true),PAInput.readString("Num Aluno: ", true));
                        case "T2" ->  error = context.insereProposta(tipo, id, ramos,PAInput.readString("Titulo: ", false), PAInput.readString("Email Docente: ", true),
                               " ",PAInput.readString("Num Aluno: ", true));
                        case "T3" ->  error = context.insereProposta(tipo, id, ramos,PAInput.readString("Titulo: ", false), PAInput.readString("Email Docente: ", true),
                                "",PAInput.readString("Num Aluno: ", true));
                    }

                }
                case 2 -> {
                    try {
                        context.importPropostas(PAInput.readString("Nome do ficheiro: ", true));
                    }
                    catch (CollectionBaseException e){
                        System.out.println(e.getMessageOfExceptions());
                    }
                }
                case 3 -> error = context.exportaCSV(PAInput.readString("Ficheiro: ", true));
                case 4 -> System.out.println(context.getPropostasToString());
                case 5 -> UIEditarPropostas();
                case 6 -> error = context.removerProposta(PAInput.readString("Id Propostas: ",true));
                case 7 -> context.removeAll();
                case 8 -> context.recuarFase();
                case 9 -> context.sair();
            }
        }else {
            option = PAInput.chooseOption(context.getName(), "Exportar para CSV", "Consultar Estágio/Projeto", "Voltar", "Exit");
            switch (option) {
                case 1 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getPropostasToString());
                case 3 -> context.recuarFase();
                case 4 -> context.sair();
            }
        }
        System.out.println(errorReport(error));
    }

    private void UIEditarPropostas() {
        ErrorCode error;

        boolean sair = false;
        while (!sair) {
            error = ErrorCode.E0;
            switch (PAInput.chooseOption(context.getName(), "Titulo", "Entidade", "Acrescentar Ramo", "Retirar Ramo", "Voltar")) {
                case 1 -> error = context.changeTitulo(PAInput.readString("Id Proposta: ", true), PAInput.readString("Novo titulo: ", false));
                case 2 -> error = context.changeEntidade(PAInput.readString("Id Proposta: ", true), PAInput.readString("Noa entidade: ", false));
                case 3 -> error = context.addRamo(PAInput.readString("Id Proposta: ", true), PAInput.readString("Ramo: ", true));
                case 4 -> error = context.removeRamo(PAInput.readString("Id Proposta: ", true), PAInput.readString("Ramo: ", true));
                case 5 -> sair = true;
            }
            System.out.println(errorReport(error));
        }
    }

    private void UIOpcoes_Candidatura() {
        ErrorCode error = ErrorCode.E0;
        if(!context.isClosed()) { //Se nao esta fechado
            switch (PAInput.chooseOption(context.getName(), "Inserção de Candidaturas ","Inserção de Candidaturas por CSV", "Exportar Candidaturas para CSV", "Consulta de Candidaturas", "Edição de Candidaturas",
                    "Remover todas as candidaturas","Obtencao de Listas de alunos", "Obtenção de listas de propostas de projecto/estágio", "Fechar Fase", "Recuar Fase", "Avançar Fase", "Exit")) {
                case 1 -> {
                    String num = PAInput.readString("Num.Aluno", true);
                    Scanner sc = new Scanner(System.in);
                    String ramo;
                    List<String> ramos= new ArrayList<>();
                    System.out.println("Ramo:");
                    ramo = sc.next();
                    ramos.add(ramo);
                    while(!ramo.equals("x") ){
                        System.out.println("Ramo: - Digite x para terminar de inserir");
                        ramo = sc.next();
                        ramos.add(ramo);
                    }
                    error = context.insereCandidatura(num, ramos);
                    sc.close();
                }
                case 2 -> {
                    try {
                        context.addCandidatura(PAInput.readString("Ficheiro: ", true));
                    }catch (CollectionBaseException c){
                        System.out.println(c.getMessageOfExceptions());
                    }
                }
                case 3 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 4 -> System.out.println(context.getCandidaturas());
                case 5 -> UIEditarCandidaturas();
                case 6 -> context.removeAll();
                case 7 -> UIObtencaoDeListaDeAluno();
                case 8 -> UIObtencaoDeListaDeProposta();
                case 9 -> error = context.closeFase();
                case 10 -> context.recuarFase();
                case 11 -> context.avancarFase();
                case 12 -> context.sair();
            }
        }
        else{
            switch  (PAInput.chooseOption(context.getName(), "Exportar Candidaturas para CSV", "Consulta de Candidaturas", "Obtencao de Listas de alunos",
                    "Obtenção de listas de propostas de projecto/estágio", "Recuar Fase", "Avançar Fase", "Exit")) {
                case 1 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 2 -> System.out.println(context.getCandidaturas());
                case 3 -> UIObtencaoDeListaDeAluno();
                case 4 -> UIObtencaoDeListaDeProposta();
                case 5 -> context.recuarFase();
                case 6 -> context.avancarFase();
                case 7 -> context.sair();
            }
        }
        System.out.println(errorReport(error));
    }

    private void UIEditarCandidaturas() {
        ErrorCode error;
        boolean sair = false;
        while(!sair){
            error = ErrorCode.E0;
            switch (PAInput.chooseOption("Editar Candidatura", "Adicionar Propostas a candidatura", "Remover Propostas a candidatura",
                    "Voltar")) {
                case 1 -> error = context.addPropostaACandidatura(PAInput.readLong("Numero do Aluno: "),
                        PAInput.readString("ID da Proposta: ", true));
                case 2 -> error = context.removePropostaACandidatura(PAInput.readLong("Numero do Aluno: "),
                        PAInput.readString("ID da Proposta: ", true));
                case 3 -> sair = true;
            }
            System.out.println(errorReport(error));
        }
    }

    private void UIObtencaoDeListaDeProposta() {
        boolean sair = false;
        int []opcoes = new int[]{0,0,0,0};
        int num;
        String []filters = new String[] {"Autopropostas de alunos","Propostas de docentes","Propostas com candidaturas","Propostas sem candidatura", "Mostrar", "Voltar"};
        String []options = new String[]{"Autopropostas de alunos [ ]","Propostas de docentes [ ]","Propostas com candidaturas [ ]",
                "Propostas sem candidatura [ ]", "Mostrar", "Voltar"};
        while (!sair){
            num = 0;
            switch (PAInput.chooseOption("Filtros para obtenção de lista de proposta de projecto/estágio", options)){
                case 1 -> num = 1;
                case 2 -> num = 2;
                case 3 -> num = 3;
                case 4 -> num = 4;
                case 5 -> System.out.println(context.getPropostasWithFiltersToString(opcoes));
                case 6 -> {
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
        boolean sair = false;
        while(!sair){
            switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Com autoProposta", "Com candidatura já Registada",
                    "Sem candidatura regista", "Voltar")) {
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
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption(context.getName(), "Atribuição automática das autopropostas ou propostas de docentes",
                "Obtenção de listas de alunos ",
                "Obtenção de listas de propostas de projecto estágio",
                "Exportar para ficheiro CSV os dados referentes aos alunos inscritos",
                "Fechar", "Recuar Fase", "Avançar Fase", "Exit")){
            case 1 -> context.atribuicaoAutomatica();
            case 2 -> UIObtencaoDeListaDeAlunoAtribuicao();
            case 3 -> UIObtencaoDeListaDePropostaAtribuida();
            case 4 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 5 -> error = context.closeFase();
            case 6 -> context.recuarFase();
            case 7 -> context.avancarFase();
            case 8 -> context.sair();
        }
        System.out.println(errorReport(error));
    }

    private void UIAtribuicao_PropostasComAnteriorFechada() {
        //Flag para voltar a executar
        int option;
        ErrorCode error = ErrorCode.E0;
        String []options = new String[]{"Atribuição automática das autopropostas ou propostas de docentes",
                "Atribuição automática de uma proposta disponível aos alunos ainda sem atribuições definidas",
                "Gestão manual de atribuições",
                "Obtenção de listas de alunos ",
                "Obtenção de listas de propostas de projecto estágio",
                "Exportar para ficheiro CSV os dados referentes aos alunos inscritos",
                "Fechar", "Recuar Fase", "Avançar Fase", "Exit"};

        if(exception == null)
                option = PAInput.chooseOption(context.getName(), options);
        else{
            option = 2;
        }
        try {
            switch (option) {
                case 1 -> context.atribuicaoAutomatica(); // Atribuiçao automatica de projetos_estagios e projetos
                case 2 -> {
                    context.atribuicaoAutomaticaSemAtribuicoesDefinidas();
                    exception = null;
                }
                case 3 -> context.gestaoManualAtribuicoes();
                case 4 -> UIObtencaoDeListaDeAlunoAtribuicao();
                case 5 -> UIObtencaoDeListaDePropostaAtribuida();
                case 6 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
                case 7 -> error = context.closeFase();
                case 8 -> context.recuarFase();
                case 9 -> context.avancarFase();
                case 10 -> context.sair();
            }
        }catch (ConflitoAtribuicaoAutomaticaException e){
            context.conflitoAtribuicaoCandidatura();
            exception = e;
        }
        System.out.println(errorReport(error));
    }

    private void UIAtribuicao_PropostasClosed() {
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption(context.getName(),"Obtenção de listas de alunos",
                "Obtenção de listas de propostas de projecto estágio",
                "Exportar para ficheiro CSV os dados referentes aos alunos inscritos", "Recuar Fase", "Avançar Fase", "Exit")){
            case 1 -> UIObtencaoDeListaDeAlunoAtribuicao();
            case 2 -> UIObtencaoDeListaDePropostaAtribuida();
            case 3 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 4 -> context.recuarFase();
            case 5 -> context.avancarFase();
            case 6 -> context.sair();
        }
        System.out.println(errorReport(error));
    }

    private void UIObtencaoDeListaDeAlunoAtribuicao() {
        boolean sair = false;
        while(!sair) {
            switch (PAInput.chooseOption("Obtenção de Lista de Aluno", "Têm autoproposta associada", "Têm candidatura já registada", "Têm proposta atribuída",
                    "Não têm qualquer proposta atribuída", "Voltar")) {
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

        while(!sair) {
            num = 0;
            switch (PAInput.chooseOption("Filtros para obtenção de lista de proposta de projecto/estágio", options)) {
                case 1 -> num = 1;
                case 2 -> num = 2;
                case 3 -> num = 3;
                case 4 -> num = 4;
                case 5 -> System.out.println(context.getPropostasWithFiltersToStringAtribuicao(opcoes));
                case 6 -> sair = true;
            }
            selecionaFiltros(opcoes, num, filters, options);
        }
    }

    private void UIConflito_Atribuicao_Candidatura(){
        System.out.println(context.getConflitoToString());
        if(context.existConflict()){
            UIConflito_Atribuicao_Candidatura_Exist_Conflict();
        }else {
            context.recuarFase();
            exception = new ConflitoAtribuicaoAutomaticaException();
        }

    }


    private void UIConflito_Atribuicao_Candidatura_Exist_Conflict() {
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption("Conflito na Atribuição de Candidatuas","Consultar Dados de Alunos em conflito",
                "Consultar Dados da Proposta", "Atribuir a Aluno", "Exit")){
            case 1 -> System.out.println(context.consultaAlunosConflito());
            case 2 -> System.out.println(context.consultaPropostaConflito());
            case 3 -> error = context.resolveConflito(PAInput.readLong("Numero do Aluno:"));
            case 4 -> context.sair();
        }
        System.out.println(errorReport(error));
    }


    private void UIAtribuicaoManualPropostas(){
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption(context.getName(), "Atribuição manual de propostas disponíveis aos alunos",
                "Remoção manual de uma atribuição previamente realizada",
                "Remoção manual de todas as atribuições",
                "Undo", "Redo", "Voltar", "Exit")) {
            case 1 -> error = manager.atribuicaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));
            case 2 -> error = manager.remocaoManual(PAInput.readLong("Num Aluno:"), PAInput.readString("Id. Proposta: ", true));
            case 3 -> error = manager.removerTodasAtribuicoes();
            case 4 -> manager.undo();
            case 5 -> manager.redo();
            case 6 -> context.recuarFase();
            case 7 -> context.sair();
        }
        System.out.println(errorReport(error));
    }

    private void UIAtribuicao_Orientadores() {
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption(context.getName(),"Associação automática dos docentes proponentes de projetos como orientador dos mesmos",
                "Exportar para CSV","Gestao de Orientadores", "Obtenção de dados de Orientadores", "Recuar Fase" ,"Fechar Fase e Avançar", "Exit")){
            case 1 -> context.associacaoAutomaticaDeDocentesAPropostas();
            case 2 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 3 -> context.gerirOrientadores();
            case 4 -> UIObtencaoDadosOrientadores();
            case 5 -> context.recuarFase();
            case 6 -> error = context.closeFase();
            case 7 -> context.sair();
        }
        System.out.println(errorReport(error));
    }

    private void UIGestao_Orientadores() {
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption(context.getName(),"Atribuir Orientador", "Consultar Orientadores", "Alterar Docente",
                "Eliminar Orientador", "Undo", "Redo", "Voltar", "Exit")){
            case 1 -> error = manager.atribuirOrientador(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 2 -> System.out.println(context.getAlunosComPropostaEOrientador());
            case 3 -> error = manager.alterarDocente(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 4 -> error = manager.removerDocente(PAInput.readString("Email do Docente:", true), PAInput.readString("ID Proposta: ", true));
            case 5 -> manager.undo();
            case 6 -> manager.redo();
            case 7 -> context.recuarFase();
            case 8 -> context.sair();
        }
        System.out.println(errorReport(error));
    }

    private void UIObtencaoDadosOrientadores() {
        boolean sair = false;
        while(!sair) {
            switch (PAInput.chooseOption("Obtenção de dados diversos sobre atribuição de orientadores",
                    "Lista de estudantes com proposta atribuída e com orientador associado",
                    "Lista de estudantes com proposta atribuída mas sem orientador associado",
                    "Número de orientações por docente, em média, mínimo, máximo, e por docente especificado",
                    "Voltar")) {
                case 1 -> System.out.println(context.getAlunosComPropostaEOrientador());
                case 2 -> System.out.println(context.getAlunosComPropostaESemOrientador());
                case 3 -> System.out.println(context.getEstatisticasPorDocente());
                case 4 -> sair = true;
            }
        }
    }

    private void UIConsulta() {
        ErrorCode error = ErrorCode.E0;
        switch (PAInput.chooseOption(context.getName(), "Exportar para CSV","Lista de alunos com propostas atribuídas",
                "Lista de estudantes sem propostas atribuídas e com opções de candidatura",
                "Conjunto de propostas disponíveis", "Conjunto de propostas atribuídas",
                "Número de orientações por docente, em média, mínimo, máximo, e por docente especificado.", "Exit")){
            case 1 -> error = context.exportaCSV(PAInput.readString("Nome do ficheiro a exportar: ", true));
            case 2 -> System.out.println(context.getTodosAlunosComPropostaAtribuida());
            case 3 -> System.out.println(context.obtencaoAlunosSemPropostaComCandidatura());
            case 4 -> System.out.println(context.getPropostasDisponiveisToString());
            case 5 -> System.out.println(context.getPropostasAtribuidasToString());
            case 6 -> System.out.println(context.getEstatisticasPorDocente());
            case 7 -> context.sair();
        }
        System.out.println(errorReport(error));
    }

    private String errorReport(ErrorCode error){
        switch(error){
            case E1 -> {return "Leitura de campo incorreto";}
            case E2 -> {return "Nome de ficheiro incorreto";}
            case E3 -> {return "Numero de aluno inexistente";}
            case E4 -> {return "Email de docente inexistente";}
            case E5 -> {return "Curso insexistente";}
            case E6 -> {return "Classificação não comprendida entre 0.0 e 1.0";}
            case E7 -> {return "Ramo inexistente";}
            case E8 -> {return "Tentativa de introdução de uma entidade em proposta não estágio";}
            case E9 -> {return "Proposta inexistente";}
            case E10 -> {return "Proposta ja contém ramo inserido";}
            case E11 -> {return "Numero de aluno já registado";}
            case E12 -> {return "Email já registado";}
            case E13 -> {return "Linha incompleta";}
            case E14 -> {return "Aluno não pode aceder a estágio";}
            case E15 -> {return "Proposta já atribuída a um aluno";}
            case E16 -> {return "Proposta sem Docente Orientador";}
            case E17 -> {return "Candidatura inexistente";}
            case E18 -> {return "Proposta inexistente em candidatura";}
            case E19 -> {return "Candidatura já existe";}
            case E20 -> {return "Tentativa de registar candidatura em aluno já com proposta";}
            case E21 -> {return "Inserção de candidatura a proposta com aluno já associado";}
            case E22 -> {return "Fase anterior Aberta";}
            case E23 -> {return "Atribuicao de propostas nao se encontra fechada";}
            case E24 -> {return "numero total de propostas é igual ou superior ao número total" +
                                " de alunos e se, para cada ramo, o número total de propostas " +
                                "é igual ou superior ao número de alunos";}
            case E25 -> {return "A operação não é válida no estado atual";}
            case E26 -> {return "Condições de fecho não alcançadas";}
            case E27 -> {return "Proposta não contem ramo inserido";}
            case E28 -> {return "Aluno não tem candidatura";}
            case E29 -> {return "Proposta contem aluno previamente attribuído";}
            case E30 -> {return "Proposta já contem docente orientador";}
            default -> {return "";}
        }
    }

}
