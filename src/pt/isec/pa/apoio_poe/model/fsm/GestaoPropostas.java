package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.BaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;
import pt.isec.pa.apoio_poe.utils.Constantes;

import java.util.*;

public class GestaoPropostas extends StateAdapter{

    public GestaoPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_PROPOSTAS;
    }
    /**
     * Importa propostas de ficheiro csv
     * @return boolean false se impossível abrir ficheiro
     *
     */
    @Override
    public boolean importPropostas(String file) throws CollectionBaseException {
        if(!CSVReader.startScanner(file,",")){
            return false;
        }

        String tipo;
        int index = 1;
        CollectionBaseException cb = null;
        while (CSVReader.hasNext()) {

            try {

                tipo = CSVReader.readString();
                switch (tipo) {
                    case "T1" -> {
                        if (!leT1(index))
                            throw new InvalidCSVField("Na linha " + index + " -> Tipo de proposta invalido" );
                    }
                    case "T2" -> {
                        if (!leT2(index))
                            throw new InvalidCSVField("Na linha " + index + " -> Tipo de proposta invalido" );
                    }
                    case "T3" -> {
                        if (!leT3(index))
                            throw new InvalidCSVField("Na linha " + index + " -> Tipo de proposta invalido" );
                    }
                    default -> throw new InvalidCSVField("Linha: " + index + " -> tipo de proposta não válida");
                }
            } catch (BaseException e) {
                //MessageCenter.getInstance().putMessage("Erro de leitura na linha: " + index + " do ficheiro: " + file);
                if(cb == null){
                    cb = new CollectionBaseException();
                }
                cb.putException(e);

                if(!CSVReader.nextLine()) break;
                index++;
                continue;
            }

            index++;
            if(!CSVReader.nextLine()) break;
        }
        if(cb != null)
            throw cb;
        return index != 1;
    }
    /**
     * Verifica e adiciona aos dados as propostas tipo T1 importadas
     *
     */
    public boolean leT1(int index) throws InvalidCSVField {
        String id, titulo, entidade;
        List<String> ramos;
        long numAluno;
        try {
            id = CSVReader.readString();
            ramos = CSVReader.readRamos();
            titulo = CSVReader.readString();
            entidade = CSVReader.readString();
        } catch (NoSuchElementException e) {
            return false;
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (NoSuchElementException e){
            if(checkRamos(index, ramos)) {
                Proposta estagio = new Estagio(id, "T1", ramos, titulo, entidade);
                if (!data.addProposta(estagio)) {
                    //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                    InvalidCSVField ex = new InvalidCSVField("Na linha" + index + " -> " + "Proposta nao inserida porque o id ja existente");
                    ex.putLine(index);
                    throw ex;
                }
            }
            return true;
        }
        if(checkT1(index, ramos, numAluno)) {
            Proposta estagio = new Estagio(id, "T1", ramos, titulo, entidade, numAluno);
            if (data.addProposta(estagio)) {
                data.atribuipropostaNaoConfirmada(estagio, numAluno);
            }else {
                //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                InvalidCSVField ex = new InvalidCSVField("Na linha" + index + " -> " + "Proposta nao inserida porque o id ja existente");
                ex.putLine(index);
                throw ex;
            }
        }
        return true;
    }
    /**
     * Verifica e adiciona aos dados as propostas tipo T2 importadas
     *
     */
    public boolean leT2(int index) throws InvalidCSVField, IncompleteCSVLine {
        String id, titulo, docente;
        List<String> ramos;
        long numAluno;
        try {
            id = CSVReader.readString();
            ramos = CSVReader.readRamos();
            titulo = CSVReader.readString();
            docente = CSVReader.readString();
        } catch (NoSuchElementException e) {
            throw new IncompleteCSVLine("Na linha " + index + " -> Linha Incompleta");
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (NoSuchElementException e){
            if(checkT2(index,ramos,docente)) {
                Projeto projeto = new Projeto(id, "T2", ramos, titulo, docente);
                if (!data.addProposta(projeto)) {
                    //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                    throw new InvalidCSVField("Linha : " + index + "-> Proposta nao inserida porque o id ja existente", index);
                }else{
                    data.atribuiDocente(projeto);
                }
            }
            return true;
        }
        if(checkT2Aluno(index, ramos, docente, numAluno)) {
            Proposta projeto = new Projeto(id, "T2", ramos, titulo, docente, numAluno);
            if (data.addProposta(projeto)) {
                data.atribuipropostaNaoConfirmada(projeto, numAluno);
                data.atribuiDocente((Projeto) projeto);
            } else
                //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                throw new InvalidCSVField("Linha : " + index + "-> Proposta nao inserida porque o id ja existente", index);

        }
        return true;
    }
    /**
     * Verifica e adiciona aos dados as propostas tipo T3 importadas
     *
     */
    public boolean leT3(int index) throws InvalidCSVField {
        String id, titulo;
        long numAluno;
        try {
            id = CSVReader.readString();
            titulo = CSVReader.readString();
            numAluno = CSVReader.readLong();
        } catch (InputMismatchException e) {
            return false;
        }
        if(checkT3(index, numAluno)) {
            Proposta autoproposta = new Projeto_Estagio(id, "T3", titulo, numAluno);
            if (data.addProposta(autoproposta)) {
                data.atribuipropostaNaoConfirmada(autoproposta, numAluno);
            } else {
                //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                throw new InvalidCSVField("Linha : " + index + "-> Proposta nao inserida porque o id ja existente", index);
            }
        }
        return true;
    }
    /**
     * Verifica se os ramos atribuídos são corretos
     *
     */
    private boolean checkRamos(int index, List<String> ramos) {
        for(String ramo : ramos) {
            if (!data.existeRamos(ramo)) {
                //("Linha " + index + " -> está a tentar inserir uma proposta com um ramo inexistente");
                return false;
            }
        }
        return true;
    }
    /**
     * Verifica se os dados das propostas tipo T1 importadas são corretos
     *
     */
    private boolean checkT1(int index, List<String> ramos, long numAluno) throws InvalidCSVField {
        boolean ok = true;

        StringBuilder sb = new StringBuilder();

        if(!checkRamos(index, ramos)){
            sb.append("Está a tentar inserir uma proposta com um ramo inexistente. ");
        }

        if(!data.verificaNumAluno(numAluno)){
            //("Linha " + index + " -> aluno atribuído inexistente");
            sb.append("O aluno não existe. ");
            ok = false;
        }
        if(!data.verificaRamoAluno(numAluno, ramos)){
            //("Linha " + index + " -> atribuição de proposta a aluno de ramo diferente");
            sb.append("atribuição de proposta a aluno de ramo diferente. ");
            ok = false;
        }
        if(!data.verificaPossibilidade(numAluno)){
            //("Linha " + index + " -> atribuição de proposta a aluno não elegível");
            sb.append("atribuição de proposta a aluno não elegível. ");
            ok = false;
        }
        if(data.verificaJaAtribuido(numAluno)){
            //("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            sb.append("atribuição de proposta a aluno com proposta já atribuída. ");
            ok = false;
        }

        if(!ok){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }
        return true;
    }
    /**
     * Verifica se os dados das propostas tipo T1 importadas são corretos
     *
     */
    private boolean checkT2(int index, List<String> ramos, String docente) throws InvalidCSVField {
        StringBuilder sb = new StringBuilder();

        if(!checkRamos(index, ramos)){
            sb.append("Está a tentar inserir uma proposta com um ramo inexistente. ");
        }
        if(!data.existeDocenteComEmail(docente)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> está a tentar inserir um docente não registado");
            sb.append(" está a tentar inserir um docente não registado");
        }
        if(sb.length() > 0){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }
        return true;
    }
    /**
     * Verifica se os dados das propostas tipo T2 importadas são corretos
     *
     */
    private boolean checkT2Aluno(int index, List<String> ramos, String docente, long numAluno) throws  InvalidCSVField {
        StringBuilder sb = new StringBuilder();

        if(!checkRamos(index, ramos)){
            sb.append("Está a tentar inserir uma proposta com um ramo inexistente. ");
        }
        if(!data.existeDocenteComEmail(docente)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> está a tentar inserir um docente não registado");
            sb.append(" está a tentar inserir um docente não registado");
        }
        if(!data.verificaNumAluno(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
            sb.append(" aluno atribuído inexistente");
        }
        if(!data.verificaRamoAluno(numAluno, ramos)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno de ramo diferente");

            sb.append(" atribuição de proposta a aluno de ramo diferente");
        }
        if(data.verificaJaAtribuido(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            sb.append(" atribuição de proposta a aluno com proposta já atribuída");
        }
        if(sb.length() > 0){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }
        return true;
    }

    /**
     * Verifica se os dados das propostas tipo T3 importadas são corretos
     * @param index index da linha do ficheiro csv
     * @param numAluno número do aluno
     * @return boolean true caso execute tudo bem
     * @throws InvalidCSVField lança exceção quando um campo é invalido
     */
    private boolean checkT3(int index, long numAluno) throws InvalidCSVField {
        StringBuilder sb = new StringBuilder();
        if(!data.verificaNumAluno(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
            sb.append(" Aluno atribuído inexistente");
        }
        if(data.verificaJaAtribuido(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            sb.append("Atribuição de proposta a aluno com proposta já atribuída");
        }
        if(sb.length() > 0){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }

        return true;
    }
    /**
     * Exporta informação das propostas para ficheiro CSV
     * @param file nome do ficheiro a gravar
     *
     */
    @Override
    public ErrorCode exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return ErrorCode.E2;
        }
        for(Proposta p : data.getProposta())
            CSVWriter.writeLine(",", true, false,p.exportProposta());
        CSVWriter.closeFile();
        return ErrorCode.E0;
    }
    /**
     * Remove uma proposta
     * @param id id da proposta a remover
     * @return ErrorCode com o resultado da remoção
     */
    @Override
    public ErrorCode removeProposta(String id) {
        if(!data.verificaProposta(id)){
            //MessageCenter.getInstance().putMessage("Nao existe o id inserido");
            return ErrorCode.E9;
        }

        data.removeProposta(id);
        return ErrorCode.E0;
    }
    /**
     * Remove todas as propostas
     *
     */
    @Override
    public boolean removeAll() {
        for (Proposta p : data.getProposta()){
            data.removeProposta(p.getId());
        }
        return true;
    }
    /**
     * Altera o titulo da proposta
     * @param id id da proposta a alterar
     * @param novo_titulo novo titulo da proposta
     * @return ErrorCode com o resultado da operação
     */
    @Override
    public ErrorCode changeTitulo(String id, String novo_titulo){
        Proposta p = getPropostaById(id);
        if (p == null)
            return ErrorCode.E9;
        p.setTitulo(novo_titulo);
        return ErrorCode.E0;
    }
    /**
     * Altera a entidade da proposta
     * @param id id da proposta a alterar
     * @param nova_entidade nova entidade da proposta
     * @return ErrorCode com o resultado da operação
     */
    @Override
    public ErrorCode changeEntidade(String id, String nova_entidade) {
        Proposta p = getPropostaById(id);
        if(p == null){
            return ErrorCode.E9;
        }
        if(p instanceof Estagio e){
            e.changeEntidade(nova_entidade);
        }else {
            //MessageCenter.getInstance().putMessage("Apenas existe entidade nos estágios");
            return ErrorCode.E8;
        }
        return ErrorCode.E0;
    }
    /**
     * Adiciona ramo a proposta
     * @param id id da proposta a alterar
     * @param ramo ramo a adicionar
     * @return ErrorCode com o resultado da operação
     */
    @Override
    public ErrorCode addRamo(String id, String ramo)  {
        if(!data.existeRamos(ramo)){
            //MessageCenter.getInstance().putMessage("Ramo inexistente");
            return ErrorCode.E7;
        }
        Proposta p = getPropostaById(id);
        if(p == null){
            return ErrorCode.E9;
        }
        if(p.getRamos()!= null && p.getRamos().contains(ramo)){
            //MessageCenter.getInstance().putMessage("A proposta já contem o ramo inserido");
            return ErrorCode.E10;
        }
        p.addRamo(ramo);
        return ErrorCode.E0;
    }
    /**
     * Obtém uma proposta dos dados
     * @param id id da proposta
     * @return Proposta proposta pretendida
     */
    private Proposta getPropostaById(String id) {
        List<Proposta> propostas = data.getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(id));
        if(propostas.size() == 0){
            //MessageCenter.getInstance().putMessage("Nao existe a proposta com o id " + id);
            return null;
        }
        return propostas.get(0);
    }
    /**
     * Remove o ramo a uma proposta
     * @param id id da proposta a alterar
     * @param ramo ramo a remover
     * @return ErrorCode com o resultado da operação
     */
    @Override
    public ErrorCode removeRamo(String id, String ramo) {
        if(!data.existeRamos(ramo)){
            //MessageCenter.getInstance().putMessage("Ramo inexistente");
            return ErrorCode.E7;
        }
        Proposta p = getPropostaById(id);
        if(p == null){
            return ErrorCode.E9;
        }
        if(p.getRamos()!= null && !p.getRamos().contains(ramo)){
            //MessageCenter.getInstance().putMessage("A proposta já não contem o ramo inserido");
            return ErrorCode.E27;
        }
        p.removeRamo(ramo);
        return ErrorCode.E0;
    }

    /**
     * Insere uma proposta
     * @param tipo tipo da proposta a inserir
     * @param id id da proposta a inserir
     * @param ramos lista dos ramos da proposta a inserir
     * @param titulo titulo da proposta a inserir
     * @param docente email do docente preponente caso exista
     * @param entidade entidade da proposta a inserir
     * @param nAluno número do aluno pré-atribuído caso exista
     * @return ErrorCode com o resultado da inserção
     *
     */
    @Override
    public ErrorCode insereProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        ErrorCode e;
        Proposta p;
        Long numAluno;
        try {
            numAluno = Long.parseLong(nAluno);
        } catch (NumberFormatException | NullPointerException exception) {
            numAluno = null;
        }
        switch (tipo) {
            case "T1" -> {
                e = verificaT1(tipo, id, ramos, titulo, entidade, numAluno);
                if (e != ErrorCode.E0) {
                    return e;
                }
            }
            case "T2" ->{
                e = verificaT2(tipo, id, ramos, titulo, docente, numAluno);
                if (e != ErrorCode.E0) {
                    return e;
                }
            }
            case "T3" ->{
                e = verificaT3(tipo, id, titulo, numAluno);
                if (e != ErrorCode.E0) {
                    return e;
                }
            }
        }
        p = Proposta.factory(tipo, id, ramos,titulo,docente,entidade, numAluno) ;
        if(!data.addProposta(p)){
            return ErrorCode.E32;
        }
        return ErrorCode.E0;
    }
    /**
     * Verifica se os dados das propostas tipo T3 são corretos
     *
     */
    private ErrorCode verificaT3(String tipo, String id, String titulo, Long numAluno) {
        if(!data.verificaNumAluno(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
            return ErrorCode.E3;
        }
        if(data.verificaJaAtribuido(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            return ErrorCode.E29;
        }


        return ErrorCode.E0;
    }
    /**
     * Verifica se os dados das propostas tipo T2 são corretos
     *
     */
    private ErrorCode verificaT2(String tipo, String id, List<String> ramos, String titulo, String docente, Long nAluno) {
        if (!checkRamos(0, ramos)) {
            return ErrorCode.E3;
        }
        if(nAluno != null){

            if (!data.verificaNumAluno(nAluno)) {
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
                return ErrorCode.E3;
            }
            if (!data.verificaRamoAluno(nAluno, ramos)) {
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno de ramo diferente");
                return ErrorCode.E31;
            }
            if (data.verificaJaAtribuido(nAluno)) {
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
                return ErrorCode.E29;
            }
        }
        if(!data.existeDocenteComEmail(docente)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> está a tentar inserir um docente não registado");
            return ErrorCode.E4;
        }

        return ErrorCode.E0;
    }
    /**
     * Verifica se os dados das propostas tipo T1 são corretos
     *
     */
    private ErrorCode verificaT1(String tipo, String id, List<String> ramos, String titulo, String entidade, Long numAluno) {
        if(ramos.size() == 0){
            return ErrorCode.E7;
        }
        if(!verificaExistenciaRamos(ramos)){
            return ErrorCode.E7;
        }
        if(entidade == null){

        }

        if(numAluno !=null) {

            if(!data.verificaNumAluno(numAluno)){
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
                return ErrorCode.E3;
            }
            if(!data.verificaRamoAluno(numAluno, ramos)){
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno de ramo diferente");
                return ErrorCode.E31;
            }
            if(!data.verificaPossibilidade(numAluno)){
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno não elegível");
                return ErrorCode.E14;
            }
            if(data.verificaJaAtribuido(numAluno)){
                return ErrorCode.E29;
            }
        }
        if(!checkRamos(0, ramos)){
            return ErrorCode.E7;
        }

        return ErrorCode.E0;

    }
    /**
     * Verifica se uma propostas contem os ramos
     *
     */
    private boolean verificaExistenciaRamos(List<String> ramos) {
        for (String r: ramos) {
            if(!Constantes.getRamos().contains(r)){
                return false;
            }
        }
        return true;
    }
    /**
     * Edita uma proposta existente
     * @param tipo tipo da proposta a editar
     * @param id id da proposta a editar
     * @param ramos lista dos ramos da proposta a editar
     * @param titulo titulo da proposta a editar
     * @param docente email do docente preponente caso exista
     * @param entidade entidade da proposta a editar
     * @param nAluno número do aluno pré-atribuído caso exista
     * @return ErrorCode com o resultado da edição
     *
     */
    @Override
    public ErrorCode editProposta(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, String nAluno) {
        ErrorCode e;
        Proposta prop = getPropostaById(id), p;
        Long numAluno = null;
        if(nAluno != null && !nAluno.equals("")){
            try {
                numAluno = Long.parseLong(nAluno);
            } catch (NullPointerException | NumberFormatException exception) {
                return ErrorCode.E3;
            }
        }
        if(prop == null){
            return ErrorCode.E9;
        }
        p = prop.getClone();
        switch (tipo){
            case "T1" -> {
                e = editEstagio(p, id, titulo, entidade, ramos, nAluno);
                if(e != ErrorCode.E0)
                    return e;
            }
            case "T2" -> {
                e = editProjeto(p, id, titulo, docente, ramos, nAluno);
                if(e != ErrorCode.E0)
                    return e;
            }
            case "T3" -> {}
        }

        if(!p.getTitulo().equals(titulo)) {
            p.setTitulo(titulo);
        }
        if(!(p instanceof Projeto_Estagio) && (e = alteraRamos(p, ramos)) != ErrorCode.E0){
            return e;
        }
        if(!(p instanceof Projeto_Estagio) && (numAluno != null)){
            Aluno a = null;
            if(p.getNumAluno() != null) {
                a = data.getAluno(p.getNumAluno());
                if (a == null) {
                    return ErrorCode.E3;
                }
                if(a.temPropostaConfirmada()){
                    return ErrorCode.E29;
                }
            }
            if(data.getAlunos().stream().anyMatch(al -> {
                if (al.temPropostaNaoConfirmada()) {
                    if (al.getPropostaNaoConfirmada().getId().equals(id))
                        return true;
                }
                if (al.temPropostaConfirmada()) {
                    return al.getProposta().getId().equals(id);
                }
                return false;
            })){
                return ErrorCode.E34;
            }
            Aluno novo = data.getAluno(numAluno);
            if(novo ==null){
                return ErrorCode.E3;
            }
            if(prop instanceof Estagio && !novo.isPossibilidade()){
                return ErrorCode.E14;
            }
            if(!prop.getRamos().contains(novo.getSiglaRamo())){
                return ErrorCode.E31;
            }
            if(novo.temPropostaConfirmada() || novo.temPropostaNaoConfirmada()){
                return ErrorCode.E34;
            }
            if(a != null) {
                if (a.temPropostaNaoConfirmada()) {
                    a.setPropostaNaoConfirmada(null);
                }
            }
            novo.setPropostaNaoConfirmada(p);
            p.setNumAluno(numAluno);
        }

        data.alteraProposta(p);
        return ErrorCode.E0;
    }

    /**
     * Edita o docente de uma proposta do tipo projeto
     * @param p proposta a editar
     * @param docente docente a atribuir
     * @return ErrorCode com o resultado da edição
     */

    private ErrorCode editProjeto(Proposta p, String id, String titulo, String docente, List<String> ramos, String nAluno) {
        ErrorCode e = ErrorCode.E0;

        if(p instanceof Projeto projeto && !docente.equals(projeto.getEmailDocente())){
            e = alteraDocente(projeto, docente);
        }
        return e;
    }
    /**
     * Edita o docente orientador e proponente de uma proposta do tipo projeto
     * @param p proposta a editar
     * @param docente docente a atribuir
     * @return ErrorCode com o resultado da edição
     */
    private ErrorCode alteraDocente(Projeto p, String docente) {
        if(!data.existeDocenteComEmail(docente)){
            return ErrorCode.E4;
        }
        p.setEmailDocente(docente);
        p.setDocenteProponente(data.getDocente(docente));
        if(p.temDocenteOrientador()){
            p.setDocenteOrientadorDocenteProponente();
        }
        return ErrorCode.E0;
    }

    /**
     * Altera a entidade de uma proposta do tipo estágio
     * @return ErrorCode com o resultado da edição
     */
    private ErrorCode editEstagio(Proposta p, String id, String titulo, String entidade, List<String> ramos, String nAluno) {
        ErrorCode e = ErrorCode.E0;

        if (p instanceof Estagio estagio && !estagio.getEntidade().equals(entidade)){
            estagio.changeEntidade(entidade);
        }


        return e;
    }

    /**
     * Altera os ramos de uma proposta
     * @param p proposta a alterar
     * @param ramos Lista dos novos ramos da proposta
     * @return ErrorCode com o resultado da edição
     */
    private ErrorCode alteraRamos(Proposta p, List<String> ramos) {

        for (String ramo : ramos) {
            if(!Constantes.getRamos().contains(ramo)){
                return ErrorCode.E7;
            }
        }
        String ramoAluno = null;
        if(p.getNumAluno() != null){
            Aluno a = data.getAluno(p.getNumAluno());
            if(a != null){
                ramoAluno = a.getSiglaRamo();
            }
        }
        if(ramoAluno != null) {

            if(!ramos.contains(ramoAluno)){
                return ErrorCode.E33;
            }
            p.setRamos(ramos);
            return ErrorCode.E0;
        }
        p.setRamos(ramos);
        //p.getRamos().addAll(ramos);
        return ErrorCode.E0;
    }


}


