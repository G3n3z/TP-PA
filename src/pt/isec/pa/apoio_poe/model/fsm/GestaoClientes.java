package pt.isec.pa.apoio_poe.model.fsm;

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
        int index = 1;

        while(CSVReader.hasNext()){
            try{
                numAluno = CSVReader.readLong();
                nome = CSVReader.readString();
                email = CSVReader.readString();
                curso = CSVReader.readString();
                ramo = CSVReader.readString();
                classificacao = CSVReader.readDouble();
                possibilidade = CSVReader.readBoolean();
            }catch (InputMismatchException e){
                //Por mensagem em logo TODO
                CSVReader.nextLine();
                index++;
                continue;
            }catch (NoSuchElementException e){
                //Por mensagem em logo TODO
                return index != 1;
            }
            aluno = new Aluno(email, nome, numAluno, curso,ramo,classificacao,possibilidade);
            if(!data.addAluno(aluno)){
                //Por mensagem em logo TODO
                System.out.println("Aluno nao inserido no index " + index);
            }
            index++;
        }

        return true;
    }
}
