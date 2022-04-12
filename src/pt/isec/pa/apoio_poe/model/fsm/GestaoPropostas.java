package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.utils.CSVReader;

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
                    default -> Log.getInstance().putMessage("Erro no index "+ index + " :tipo de proposta inexistente.");
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
        CSVReader.closeReaders();
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
        } catch (InputMismatchException e) {
            return false;
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (NoSuchElementException e){
            Proposta estagio = new Estagio(id,"T1",ramos,titulo,entidade);
            if(!data.addProposta(estagio)){
                Log.getInstance().putMessage("Proposta no index " + index + " nao inserida no index ");
            }
            return true;
        }
        Proposta estagio = new Estagio(id,"T1",ramos,titulo,entidade,numAluno);
        if(!data.addProposta(estagio)){
            Log.getInstance().putMessage("Proposta no index " + index + " nao inserida no index ");
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
        if(!data.verificaDocente(docente)){
            return false;
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (InputMismatchException e){
            Proposta projeto = new Projeto(id,"T2",ramos,titulo,docente);
            if(!data.addProposta(projeto)){
                Log.getInstance().putMessage("Proposta nao inserido no index " + index);
            }
            return true;
        }
        Proposta projeto = new Projeto(id,"T2",ramos,titulo,docente,numAluno);
        if(!data.addProposta(projeto)){
            Log.getInstance().putMessage("Proposta nao inserido no index " + index);
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
        if(!data.verificaAluno(numAluno)){
            return false;
        }
        Proposta autoproposta = new Projeto_Estagio(id,"T3",titulo,numAluno);
        if(!data.addProposta(autoproposta)){
            Log.getInstance().putMessage("Proposta nao inserido no index " + index);
        }
        data.atribuiproposta(autoproposta, numAluno);
        return true;
    }
}

//verificacoes TODO