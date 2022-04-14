package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Override
    public boolean importPropostas(String file) {
        if(!CSVReader.startScanner(file,",")){
            Log.getInstance().putMessage("O ficheiro não existe");
            return false;
        }

        String tipo;
        int index = 1;

        while (CSVReader.hasNext()) {
            try {

                tipo = CSVReader.readString();
                switch (tipo) {
                    case "T1" -> {
                        if (!leT1(index))
                            throw new NoSuchElementException();
                    }
                    case "T2" -> {
                        if (!leT2(index))
                            throw new NoSuchElementException();
                    }
                    case "T3" -> {
                        if (!leT3(index))
                            throw new NoSuchElementException();
                    }
                    default -> Log.getInstance().putMessage("Erro de leitura na linha " + index + ": tipo de proposta não válida\n");
                }
            } catch (NoSuchElementException e) {
                Log.getInstance().putMessage("Erro de leitura na linha: " + index + " do ficheiro: " + file);
                if(!CSVReader.nextLine()) break;
                index++;
                continue;
            }
            index++;
            if(!CSVReader.nextLine()) break;
        }

        return index != 1;
    }

    public boolean leT1(int index){
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
                    Log.getInstance().putMessage("Proposta nao inserida no index " + index + ": id ja existente\n");
                }
            }
            return true;
        }
        if(checkT1(index, ramos, numAluno)) {
            Proposta estagio = new Estagio(id, "T1", ramos, titulo, entidade, numAluno);
            if (data.addProposta(estagio)) {
                data.atribuipropostaNaoConfirmada(estagio, numAluno);
            }else {
                Log.getInstance().putMessage("Proposta nao inserida no index " + index + ": id ja existente\n");
            }
        }
        return true;
    }

    public boolean leT2(int index){
        String id, titulo, docente;
        List<String> ramos;
        long numAluno;
        try {
            id = CSVReader.readString();
            ramos = CSVReader.readRamos();
            titulo = CSVReader.readString();
            docente = CSVReader.readString();
        } catch (InputMismatchException e) {
            return false;
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (NoSuchElementException e){
            if(checkT2(index,ramos,docente)) {
                Projeto projeto = new Projeto(id, "T2", ramos, titulo, docente);
                if (!data.addProposta(projeto)) {
                    Log.getInstance().putMessage("Proposta nao inserida no index " + index + ": id ja existente\n");
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
                Log.getInstance().putMessage("Proposta nao inserida no index " + index + ": id ja existente\n");
        }
        return true;
    }

    public boolean leT3(int index){
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
                Log.getInstance().putMessage("Proposta nao inserida no index " + index + ": id ja existente\n");
            }
        }
        return true;
    }

    private boolean checkRamos(int index, List<String> ramos) {
        for(String ramo : ramos) {
            if (!data.existeRamos(ramo)) {
                Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir uma proposta com um ramo inexistente\n");
                return false;
            }
        }
        return true;
    }

    private boolean checkT1(int index, List<String> ramos, long numAluno) {
        boolean ok = checkRamos(index, ramos);
        if(!data.verificaNumAluno(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " aluno atribuído inexistente\n");
            ok = false;
        }
        if(!data.verificaRamoAluno(numAluno, ramos)){
            Log.getInstance().putMessage("Na linha " + index + " atribuição de proposta a aluno de ramo diferente");
            ok = false;
        }
        if(!data.verificaPossibilidade(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " atribuição de proposta a aluno não elegível");
            ok = false;
        }
        if(!data.verificaJaAtribuido(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " atribuição de proposta a aluno com proposta já atribuída");
            ok = false;
        }
        return ok;
    }

    private boolean checkT2(int index, List<String> ramos, String docente) {
        boolean ok = checkRamos(index, ramos);
        if(!data.existeDocenteComEmail(docente)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um docente não registado");
            ok = false;
        }
        return ok;
    }

    private boolean checkT2Aluno(int index, List<String> ramos, String docente, long numAluno) {
        boolean ok = checkRamos(index, ramos);
        if(!data.existeDocenteComEmail(docente)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um docente não registado");
            ok = false;
        }
        if(!data.verificaNumAluno(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " aluno atribuído inexistente");
            ok = false;
        }
        if(!data.verificaRamoAluno(numAluno, ramos)){
            Log.getInstance().putMessage("Na linha " + index + " atribuição de proposta a aluno de ramo diferente");
            ok = false;
        }
        if(data.verificaJaAtribuido(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " atribuição de proposta a aluno com proposta já atribuída");
            ok = false;
        }
        return ok;
    }

    private boolean checkT3(int index, long numAluno) {
        boolean ok = true;
        if(!data.verificaNumAluno(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " aluno atribuído inexistente");
            ok = false;
        }
        if(data.verificaJaAtribuido(numAluno)){
            Log.getInstance().putMessage("Na linha " + index + " atribuição de proposta a aluno com proposta já atribuída");
            ok = false;
        }
        return ok;
    }

    @Override
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }
        data.exportProposta();
        return true;
    }

    @Override
    public void removeProposta(String id) {
        if(!data.verificaProposta(id)){
            Log.getInstance().putMessage("Nao existe o id inserido");
        }
        data.removeProposta(id);
    }
}
