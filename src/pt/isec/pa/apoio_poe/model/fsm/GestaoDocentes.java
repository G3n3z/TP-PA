package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

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
            Log.getInstance().putMessage("O ficheiro não existe");
            return false;
        }

        String email, nome;
        Docente docente;
        int index = 1;


        while (CSVReader.hasNext()) {
            try {

                nome = CSVReader.readString();
                email = CSVReader.readString();
            } catch (NoSuchElementException e) {
                Log.getInstance().putMessage("Erro de leitura na linha: " + index + "do ficheiro: "+file);
                if(!CSVReader.nextLine()) break;
                index++;
                continue;
            }
            if(checkDocente(index, email)){
                docente = new Docente(email, nome);
                if (!data.addDocente(docente)) {
                    Log.getInstance().putMessage("Docente nao inserido no index " + index + " : email ja registado por outro docente\n");
                }
            }
            index++;
            if(!CSVReader.nextLine()) break;
        }
        CSVReader.closeReaders();

        return index != 1;
    }

    private boolean checkDocente(int index, String email) {
        if(data.existeAlunoComEmail(email)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um docente com um email ja registado por um aluno\n");
            return false;
        }
        return true;
    }

    @Override
    public void removeDocente(String email) {
        if(data.removeDocente(email)){
            Log.getInstance().putMessage("Email não registado em nenhum docente");
        }
    }

    @Override
    public void changeNameDocente(String novo_nome, String email) {
        if(!data.changeNameDocente(novo_nome, email)){
            Log.getInstance().putMessage("Nao existe docente com este email");
        }
    }

    @Override
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }
        data.exportDocente();
        CSVWriter.closeFile();
        return true;
    }
}
