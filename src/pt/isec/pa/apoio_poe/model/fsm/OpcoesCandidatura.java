package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.utils.CSVReader;

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
        changeState(EnumState.ATRIBUICAOPROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.OPCOES_CANDIDATURA;
    }

    @Override
    public boolean close() {
        if(context.getBooleanState(EnumState.CONFIG_OPTIONS)){
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
            for (Proposta p : data.getPropostas()) {
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
                //fazer return escusa contar o numero de ids TODO
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
        if(data.existePropostaSemAluno(idProposta)){
           a.addCandidatura(idProposta);
        }
    }
}
