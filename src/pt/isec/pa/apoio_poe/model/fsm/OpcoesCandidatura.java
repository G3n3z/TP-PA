package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;
import pt.isec.pa.apoio_poe.model.Singleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
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
    public void editarCandidaturas() {
        changeState(EnumState.EDITAR_CANDIDATURAS);
    }

    @Override
    public void obtencaoListaAlunos() {
        changeState(EnumState.OBTENCAO_LISTA_ALUNOS);
    }

    @Override
    public void obtencaoListaProposta() {
        changeState(EnumState.OBTENCAO_LISTA_PROPOSTAS);
    }

    @Override
    public EnumState getState() {
        return EnumState.OPCOES_CANDIDATURA;
    }

    @Override
    public boolean close() {
        if(data.getBooleanState(EnumState.CONFIG_OPTIONS)){
            setClose(true);
            MessageCenter.getInstance().putMessage("Fase fechada corretamente\n");
            return true;
        }
        MessageCenter.getInstance().putMessage("Condições de fecho de fase não alcançadas.\n" +
                "Fase anterior ainda aberta.");
        return false;
    }

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
                                " está a propor-se ao à proposta " + p.getId() + " que já tem aluno associado");
                    }
                }
            }
            if(notfind){
                throw new InvalidCSVField("A candidatura do aluno: " + candidatura.getNumAluno() +
                    "está a propor-se ao à proposta: " + id + " que não existe");

            }
        }
        return find != candidatura.getIdProposta().size();
    }

    @Override
    public void addPropostaACandidatura(long nAluno, String idProposta) {
        Aluno a = data.getAluno(nAluno);
        if(a == null){
            MessageCenter.getInstance().putMessage("Numero de aluno não existente");
            return;
        }
        if(!a.temCandidatura()){
            MessageCenter.getInstance().putMessage("O aluno não tem candidatura");
            return;
        }
        if(data.existePropostaSemAluno(idProposta)){
           a.addCandidatura(idProposta);
        }

    }

    @Override
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }
        for(Candidatura c : data.getCandidaturas()){
            CSVWriter.writeLine(",",true,false, c.getExportCandidatura());
        }
        CSVWriter.closeFile();
        return true;
    }

    @Override
    public void removePropostaACandidatura(String id, long naluno) {
        if(!data.verificaProposta(id)){
            MessageCenter.getInstance().putMessage("Nao existe o id inserido");
            return;
        }
        Aluno a = data.getAluno(naluno);
        if(a == null){
            MessageCenter.getInstance().putMessage("Nao existe nenhum aluno com este numero inserido");
            return;
        }
        if(!a.temCandidatura()){
            MessageCenter.getInstance().putMessage("O aluno " + naluno + " nao tem candidatura");
            return;
        }
        if(a.getCandidatura().containsPropostaById(id)){
            a.getCandidatura().removeProposta(id);
        }
        else{
            MessageCenter.getInstance().putMessage("O aluno " + naluno + " nao tem essa proposta na sua candidatura");
        }

    }

    @Override
    public boolean removeAll() {
        for (Candidatura c : data.getCandidaturas()){
            data.removeCandidatura(c.getNumAluno());
        }
        return true;
    }
}
