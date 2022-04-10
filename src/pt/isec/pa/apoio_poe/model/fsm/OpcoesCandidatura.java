package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.utils.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        setClose(true);
        return true;
    }

    @Override
    public boolean addCandidatura(String file) {
        if(!CSVReader.startScanner(file, ",")){
            //// por mensagem no logo //TODO
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
                index++;
                Log.getInstance().putMessage("Erro de leitura na linha " + index);
                CSVReader.nextLine();
                continue;
            }

            candidatura = new Candidatura(numAluno, ids);
            if(data.existsFieldsOfCandidatura(candidatura)){
                data.addCandidatura(candidatura);
            }
            CSVReader.nextLine();
        }

        return true;
    }
}
