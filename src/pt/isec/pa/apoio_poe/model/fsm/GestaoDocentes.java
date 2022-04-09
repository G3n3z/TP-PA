package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.utils.CSVReader;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class GestaoDocentes extends StateAdapter{

    public GestaoDocentes(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_DOCENTES;
    }

    @Override
    public boolean importDocentes(String file){
        if(!CSVReader.startScanner(file,",")){
            return false;
        }
        String email, nome;
        Docente docente;
        int index = 1;

        while (CSVReader.hasNext()){
            try{
                nome = CSVReader.readString();
                email = CSVReader.readString();
            }catch (InputMismatchException e){
                Log.getInstance().putMessage("Erro de leitura na linha " + index);
                CSVReader.nextLine();
                index++;
                continue;
            }catch (NoSuchElementException e){
                return index != 1;
            }

            docente = new Docente(email, nome);
            if(!data.addDocente(docente)){
                Log.getInstance().putMessage("Docente nao inserido no index " + index);
            }
            index++;
        }
        return true;
    }
}
