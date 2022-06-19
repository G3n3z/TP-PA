package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.*;

public class OpcoesCandidatura extends StateAdapter{

    public OpcoesCandidatura(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public boolean avancarFase() {
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }


    @Override
    public EnumState getState() {
        return EnumState.OPCOES_CANDIDATURA;
    }

    /**
     * Apenas fecha a fase se o estado de CONFIG_OPTIONS estiver fechado
     * @return ErrorCode com o resultado da operação
     */
    @Override
    public ErrorCode close() {
        if(data.getBooleanState(EnumState.CONFIG_OPTIONS)){
            setClose(true);
            //"Fase fechada corretamente\n";
            data.closeState(getState());
            return ErrorCode.E0;
        }
        //"Condições de fecho de fase não alcançadas.\n" +
        //        "Fase anterior ainda aberta."
        return ErrorCode.E22;
    }

    /**
     *
     * @param file path para o ficheiro a ser lido
     * @return retorna true se conseguir ler todas as candidaturas sem erros
     * @throws CollectionBaseException lança um container de exceções com os problemas causados. <p></p>
     * No entanto adiciona todas as candidaturas que consiga ler com sucesso
     */
    @Override
    public boolean addCandidatura(String file) throws CollectionBaseException {
        CollectionBaseException col = null;
        if(!CSVReader.startScanner(file, ",")){
            col = new CollectionBaseException();
            col.putException(new InvalidCSVField("O ficheiro não existe"));
            throw col;
        }
        int index = 0;
        Candidatura candidatura;

        while(CSVReader.hasNext()){
            try {
                index++;
                candidatura = readCandidatura(index);
                if(candidatura != null )
                    if(!data.addCandidatura(candidatura)){
                        throw new InvalidCSVField("Linha: " + index + " -> O aluno: " + candidatura.getNumAluno() +" ja tem uma candidatura registada");
                }
            } catch (InvalidCSVField | IncompleteCSVLine e ) {
                if(col == null){
                    col = new CollectionBaseException();
                }
                col.putException(e);
            }
            if(!CSVReader.nextLine()) break;

        }
        CSVReader.closeReaders();
        if(col != null)
            throw col;
        return index != 1;

    }

    /**
     *
     * @param index index onde vai a ler o ficheiro
     * @return retorna candidatura instanciada com os dados lidos
     * @throws IncompleteCSVLine lança exceção quando uma linha não está completa
     * @throws InvalidCSVField lança exceção quando um campo é invalido
     */
    public Candidatura readCandidatura(int index) throws IncompleteCSVLine, InvalidCSVField {
        long numAluno;
        List<String> ids = new ArrayList<>();
        try {
            numAluno = CSVReader.readLong2();
            while (CSVReader.hasNext()){
                ids.add(CSVReader.readString());
            }
        }catch (NoSuchElementException e){
            IncompleteCSVLine ex = new IncompleteCSVLine("Na linha " + index + " -> Linha Incompleta");
            ex.putLine(index);
            throw ex;
        } catch (InvalidCSVField e) {
            e.addToBeginMessage("Na linha "     + index + " -> ");
            e.addToMessage(" numero de aluno");
            e.putLine(index);
            throw e;
        }
        Candidatura candidatura = new Candidatura(numAluno, new ArrayList<>(ids));
        if(!existsFieldsOfCandidatura(candidatura,index))
            return null;

        return candidatura;
    }

    /**
     *
     * @param candidatura candidatura criada
     * @param index linha do ficheiro csv
     * @return return true se a candidatura tem todos os dados validos
     * @throws InvalidCSVField lança exceção se um dos dados estiver corrompido
     * @seeAlso  InvalidCSVField.getExcepMessage()
     */
    public boolean existsFieldsOfCandidatura(Candidatura candidatura, int index) throws InvalidCSVField {
        Aluno a = data.getAluno(candidatura.getNumAluno());
        StringBuilder sb = new StringBuilder();
        InvalidCSVField e;
        if(a == null) {
            throw new InvalidCSVField("Linha: " +  index + "-> Não existe aluno com o numero: " + candidatura.getNumAluno() + ". ");
        }
        if(a.temPropostaNaoConfirmada() || a.temPropostaConfirmada()){
            sb.append("O aluno ").append("já possui uma proposta. ");
        }
        if(candidatura.getIdProposta().size() == 0){
            sb.append("Tentou inserir uma candidatura vazia. ");
        }
        if(sb.toString().length() > 0){
            throw new InvalidCSVField("Linha: " + index + " -> " + sb.toString());
        }
        return !candidaturaTemPropostaComAluno(candidatura, index);
    }

    /**
     * Função que verifica se uma nova candidatura tem uma proposta com aluno associado
     * @param candidatura candidatura a ser analisada e possivelmente inserida nos dados
     * @param index index do csv
     * @return return true se a candidatura for váida
     * @throws InvalidCSVField lança exceção se a candidadtura tiver uma proposta com aluno ja associado
     */
    public boolean candidaturaTemPropostaComAluno(Candidatura candidatura, int index) throws InvalidCSVField {
        int find = 0;
        boolean notfind;  //testa se certo id passado na candidatura existe para poder imprimir
        for (String id : candidatura.getIdProposta()) {
            notfind = true;
            for (Proposta p : data.getProposta()) {
                if(p.getId().equalsIgnoreCase(id)){
                    find++;
                    notfind = false;
                    if (p.getNumAluno() != null) {
                        throw new InvalidCSVField("Linha: " + index + " -> A candidatura do aluno: " + candidatura.getNumAluno() +
                                " está a propor-se ao à proposta " + p.getId() + " que já tem aluno associado", index, ErrorCode.E21);
                    }
                }
            }
            if(notfind){
                throw new InvalidCSVField("A candidatura do aluno: " + candidatura.getNumAluno() +
                    "está a propor-se ao à proposta: " + id + " que não existe", index, ErrorCode.E9);

            }
        }
        return find != candidatura.getIdProposta().size();
    }

    /**
     * Funçao que adiciona proposta a candidatura
     * @param nAluno numero de aluno da candidatura em questão
     * @param idProposta id da proposta a adicionar
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode addPropostaACandidatura(long nAluno, String idProposta) {
        Aluno a = data.getAluno(nAluno);
        if(a == null){
            //MessageCenter.getInstance().putMessage("Numero de aluno não existente");
            return ErrorCode.E3;
        }
        if(!a.temCandidatura()){
            //MessageCenter.getInstance().putMessage("O aluno não tem candidatura");
            return ErrorCode.E28;
        }
        if(!data.existePropostaSemAluno(idProposta)){
            return ErrorCode.E15;
        }
        a.addPropostaACandidatura(idProposta);
        return ErrorCode.E0;
    }

    /**
     * Função que exporta os dados das candidaturas para um ficheiro CSV
     * @param file path para o ficheiro a ser gravado
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return ErrorCode.E2;
        }
        for(Candidatura c : data.getCandidaturas()){
            CSVWriter.writeLine(",",true,false, c.getExportCandidatura());
        }
        CSVWriter.closeFile();
        return ErrorCode.E0;
    }

    /**
     * Funcao que remove proposta a uma candidatura
     * @param id id da porposta a ser removida
     * @param naluno numero de aluno da candidatura
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode removePropostaACandidatura(String id, long naluno) {
        if(!data.verificaProposta(id)){
            //MessageCenter.getInstance().putMessage("Nao existe o id inserido");
            return ErrorCode.E9;
        }
        Aluno a = data.getAluno(naluno);
        if(a == null){
            //MessageCenter.getInstance().putMessage("Nao existe nenhum aluno com este numero inserido");
            return ErrorCode.E3;
        }
        if(!a.temCandidatura()){
            //MessageCenter.getInstance().putMessage("O aluno " + naluno + " nao tem candidatura");
            return ErrorCode.E28;
        }
        if(a.getCandidatura().containsPropostaById(id)){
            a.getCandidatura().removeProposta(id);
        }
        else{
            //MessageCenter.getInstance().putMessage("O aluno " + naluno + " nao tem essa proposta na sua candidatura");
            return ErrorCode.E18;
        }
        return ErrorCode.E0;
    }

    /**
     * Funcao que remove todas as candidaturas
     * @return boolean se a operação correu bem
     */
    @Override
    public boolean removeAll() {
        for (Candidatura c : data.getCandidaturas()){
            data.removeCandidatura(c.getNumAluno());
        }
        return true;
    }

    /**
     *
     * @return retorna String com os alunos com autoproposta
     */
    @Override
    public String obtencaoAlunosComAutoProposta() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getPropostaNaoConfirmada() instanceof Projeto_Estagio).forEach(sb::append);
        return sb.toString();
    }

    /**
     *
     * @return String com os alunos com candidatura
     */
    @Override
    public String obtencaoAlunosComCandidatura() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getCandidatura() != null).forEach(sb::append);
        return sb.toString();
    }

    /**
     *
     * @return retorna String com alunos sem candidatura
     */
    @Override
    public String obtencaoAlunosSemCandidatura() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getCandidatura() == null && (!a.temPropostaNaoConfirmada() && !a.temPropostaConfirmada())).forEach(sb::append);
        return sb.toString();
    }

    /**
     * Funcao que o ontem as propostas com filtors
     * @param filters inteiros que especificam os filtros a ser aplicados
     * @return retorna Set com as propostas com filtros
     */
    public Set<Proposta> getPropostasWithFilters(int ...filters){
        Set<Proposta> propostas = new HashSet<>();
        for (int i : filters){
            switch (i){
                case 1 -> propostas.addAll(data.getAutoPropostas());
                case 2 -> propostas.addAll(data.getProjetos());
                case 3 -> propostas.addAll(data.getPropostasComCandidatura());
                case 4 -> propostas.addAll(data.getPropostasSemCandidatura());
            }
        }
        return propostas;
    }

    /**
     * Funcao que retorna as propostas com filtros em String
     * @param filters inteiros que especificam os filtros a ser aplicados
     * @return String com as propostas
     */
    @Override
    public String getPropostasWithFiltersToString(int[] filters) {
        StringBuilder sb = new StringBuilder();
        getPropostasWithFilters(filters).forEach( p -> sb.append(p).append("\n"));
        return sb.toString();
    }

    /**
     * Funcao que remove candidatura a um aluno
     * @param numAluno numero de aluno a  remover a candidatura
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode removeCandidatura(long numAluno) {
        data.removeCandidatura(numAluno);
        return ErrorCode.E0;
    }

    /**
     *  Funcaod e inserção manual de candidatura
     * @param nAluno numero de aluno a ser inserida a candidatura
     * @param ids List com as propostas da candidatura
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode insereCandidatura(String nAluno, List<String> ids) {
        Long numAluno = parseNumAluno(nAluno);
        if(numAluno == null){
            return ErrorCode.E3;
        }
        Aluno a = data.getAluno(numAluno);
        if(a == null){
            return ErrorCode.E3;
        }
        if(a.temPropostaNaoConfirmada() || a.temPropostaConfirmada()){
           return ErrorCode.E21;
        }
        if(ids == null || ids.size() == 0){
            return ErrorCode.E9;
        }

        Candidatura c = new Candidatura(numAluno, ids);
        try {
            candidaturaTemPropostaComAluno(c,0);
        } catch (InvalidCSVField e) {
            return e.getErrorCode();
        }

        data.addCandidatura(c);

        return ErrorCode.E0;
    }

    /**
     * Funcao de edicao de candidatura
     * @param nAluno numero de aluno a qual a candidatura ira ser alterada
     * @param ids novos ids das propostas
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode editCandidatura(String nAluno, List<String> ids) {
        Long numAluno = parseNumAluno(nAluno);
        if(numAluno == null){
            return ErrorCode.E3;
        }
        Aluno a = data.getAluno(numAluno);
        if(a == null){
            return ErrorCode.E3;
        }
        Candidatura c = a.getCandidatura();
        Proposta p;
        List<String> idsValidos = new ArrayList<>();
        for (String id : ids) {
            if(!c.getIdProposta().contains(id)){
                p = data.getOnePropostaById(id);
                if(p == null){
                    return ErrorCode.E9;
                }
                if(p.isAtribuida() ){
                    return ErrorCode.E15;
                }
                if(p.getNumAluno() != null && !Objects.equals(p.getNumAluno(), numAluno)){
                    return ErrorCode.E29;
                }
                idsValidos.add(id);
            }else{
                idsValidos.add(id);
            }
        }
        c.getIdProposta().clear();
        c.getIdProposta().addAll(idsValidos);
        return ErrorCode.E0;
    }

    /**
     * Funcao que transforma uma string em um long
     * @param nAluno string com numero de aluno a ser transformado
     * @return retorna um Long com o numero transformado, poderá ser null se não conseguir trasnformar a string em long
     */
    private Long parseNumAluno(String nAluno) {
        long numAluno;
        try {
            numAluno = Long.parseLong(nAluno);
        }catch (NullPointerException | NumberFormatException e){
            return null;
        }
        return numAluno;
    }

    /**
     *
     * @return retorna List com os copias dos alunos com autoproposta
     */
    @Override
    public List<Aluno> getAlunosComAutoProposta() {
        List<Aluno> al = new ArrayList<>();
        for (Aluno aluno : data.getAlunos()) {
            if(aluno.temPropostaNaoConfirmada()){
                al.add(aluno.getClone());
            }
        }
        return al;

    }

    /**
     *
     * @return retorna List com os copias dos alunos com candidatura
     */
    @Override
    public List<Aluno> getAlunosComCandidatura() {

        List<Aluno> al = new ArrayList<>();
        for (Aluno aluno : data.getAlunos()) {
            if(aluno.getCandidatura() != null){
                al.add(aluno.getClone());
            }
        }
        return al;
    }

    /**
     *
     * @return retorna List com os copias dos alunos sem candudatura
     */
    @Override
    public List<Aluno> getAlunosSemCandidatura() {
        List<Aluno> al = new ArrayList<>();
        for (Aluno aluno : data.getAlunos()) {
            if(aluno.getCandidatura() == null){
                al.add(aluno.getClone());
            }
        }
        return al;
    }

    /**
     *
     * @param opcoes inteiros que correspondem aos filtros a serem aplicados
     * @return retorna List com os copias das propostas com os filtros aplicados
     */
    @Override
    public List<Proposta> getPropostasWithFiltersCopia(int ...opcoes) {
        Set<Proposta> propostas =  getPropostasWithFilters(opcoes);
        List<Proposta> copia = new ArrayList<>();
        for (Proposta proposta : propostas) {
            copia.add(proposta.getClone());
        }

        return copia;
    }
}
