package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.utils.CSVReader;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class GestaoClientes extends StateAdapter{

    public GestaoClientes(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_CLIENTES;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public boolean addAluno(String file) {
        String email, nome, ramo, curso;
        long numAluno;
        double classificacao;
        boolean possibilidade;
        Aluno aluno;
        if(!CSVReader.startScanner(file, ",")){
            //// por mensagem no logo //TODO
            return false;
        }
        int index = 0;

        while(CSVReader.hasNext()){
            try{
                index++;
                numAluno = CSVReader.readLong();
                nome = CSVReader.readString();
                email = CSVReader.readString();
                curso = CSVReader.readString();
                ramo = CSVReader.readString();
                classificacao = CSVReader.readDouble();
                possibilidade = CSVReader.readBoolean();
            } catch (NoSuchElementException e){
                Log.getInstance().putMessage("Erro de leitura na linha " + index);
                CSVReader.nextLine();
                continue;
            }

            aluno = new Aluno(email, nome, numAluno, curso,ramo,classificacao,possibilidade);
            if(!data.addAluno(aluno)){
                Log.getInstance().putMessage("Aluno nao inserido no index " + index);
            }
            //index++;
            CSVReader.nextLine();
        }
        //CSVReader.closeReaders();
        return index!=1;
    }

    @Override
    public void changeName(String novo_nome, long naluno) {
        data.changeNameAluno(naluno, novo_nome);
    }

    @Override
    public void removeAluno(long numero_de_aluno) {
        if(data.removeAluno(numero_de_aluno)){
            Log.getInstance().putMessage("Numero de Aluno inexistente");
        }
    }

    @Override
    public void changeCursoAluno(String novo_curso, long nAluno) {
        if(data.changeCursoAluno(novo_curso, nAluno)){
            Log.getInstance().putMessage("Numero de Aluno inexistente");
        }

    }

    @Override
    public void changeRamoAluno(String novo_ramo, long nAluno) {
        if(data.changeRamoAluno(novo_ramo, nAluno)){
            Log.getInstance().putMessage("Numero de Aluno inexistente");
        }
    }

    @Override
    public void changeClassAluno(double nova_classificaçao, long nAluno) {
        if(data.changeClassAluno(nova_classificaçao, nAluno)){
            Log.getInstance().putMessage("Numero de Aluno inexistente");
        }
    }
}
