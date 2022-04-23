package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
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


    //asf
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
            return true;
        }
        return false;
    }

    @Override
    public boolean addCandidatura(String file) {
        if(!CSVReader.startScanner(file, ",")){
            Log.getInstance().putMessage("O ficheiro não existe");
            return false;
        }
        long numAluno;
        List<String> ids = new ArrayList<>();
        int index = 1;
        Candidatura candidatura;

        while(CSVReader.hasNext()){
            try {
                numAluno = CSVReader.readLong();
                while (CSVReader.hasNext()){
                    ids.add(CSVReader.readString());
                }
            }catch (NoSuchElementException e){
                Log.getInstance().putMessage("Erro de leitura na linha: " + index + " do ficheiro: " + file);
                if(!CSVReader.nextLine()) break;
                index++;
                continue;
            }

            candidatura = new Candidatura(numAluno, new ArrayList<>(ids));
            if(existsFieldsOfCandidatura(candidatura)){
                if(!data.addCandidatura(candidatura)){
                    Log.getInstance().putMessage("O aluno ja tem uma candidatura registada linha: " + index );
                }
            }

            if(!CSVReader.nextLine()) break;
            ids.clear();
            index++;
        }
        CSVReader.closeReaders();
        return index!=0;
    }

    public boolean existsFieldsOfCandidatura(Candidatura candidatura) {
        Aluno a = data.getAluno(candidatura.getNumAluno());
        if(a == null) {
            Log.getInstance().putMessage("Não existe aluno com o numero: " + candidatura.getNumAluno());
            return false;
        }
        if(a.temPropostaNaoConfirmada() || a.temPropostaConfirmada()){
            Log.getInstance().putMessage("O aluno " + a.getNumeroAluno() + " - " + a.getNome() +"já possui uma proposta");
            return false;
        }
        if(candidatura.getIdProposta().size() == 0){
            Log.getInstance().putMessage("O aluno : " + candidatura.getNumAluno() + " tentou inserir uma candidatura vazia" );
            return false;
        }
        if(a.temPropostaNaoConfirmada() || a.temPropostaConfirmada()){
            Log.getInstance().putMessage("O aluno : " + candidatura.getNumAluno() + " já tem uma proposta associada");
        }

        return !candidaturaTemPropostaComAluno(candidatura);
    }

    public boolean candidaturaTemPropostaComAluno(Candidatura candidatura){
        int find = 0;
        boolean notfind;  //testa se certo id passado na candidatura existe para poder imprimir
        for (String id : candidatura.getIdProposta()) {
            notfind = true;
            for (Proposta p : data.getProposta()) {
                if(p.getId().equalsIgnoreCase(id)){
                    find++;
                    notfind = false;
                    if (p.getNumAluno() != null) {
                        Log.getInstance().putMessage("A candidatura do aluno: " + candidatura.getNumAluno() +
                                " está a propor-se ao à proposta " + p.getId() + " que já tem aluno associado");
                        return true;
                    }
                }
            }
            if(notfind){Log.getInstance().putMessage("A candidatura do aluno: " + candidatura.getNumAluno() +
                    "está a propor-se ao à proposta: " + id + " que não existe");

            }
        }
        return find != candidatura.getIdProposta().size();
    }

    @Override
    public void addPropostaACandidatura(long nAluno, String idProposta) {
        Aluno a = data.getAluno(nAluno);
        if(a == null){
            Log.getInstance().putMessage("Numero de aluno não existente");
            return;
        }
        if(!a.temCandidatura()){
            Log.getInstance().putMessage("O aluno não tem candidatura");
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
            Log.getInstance().putMessage("Nao existe o id inserido");
            return;
        }
        Aluno a = data.getAluno(naluno);
        if(a == null){
            Log.getInstance().putMessage("Nao existe nenhum aluno com este numero inserido");
            return;
        }
        if(!a.temCandidatura()){
            Log.getInstance().putMessage("O aluno " + naluno + " nao tem candidatura");
            return;
        }
        if(a.getCandidatura().containsPropostaById(id)){
            a.getCandidatura().removeProposta(id);
        }
        else{
            Log.getInstance().putMessage("O aluno " + naluno + " nao tem essa proposta na sua candidatura");
        }

    }


}
