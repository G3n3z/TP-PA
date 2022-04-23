package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidField;
import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.List;
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
    public void editarDocentes() {
        changeState(EnumState.EDITAR_DOCENTES);
    }

    @Override
    public boolean importDocentes(String file) throws CollectionBaseException {
        CollectionBaseException col = null;
        if(!CSVReader.startScanner(file,",")){
            Log.getInstance().putMessage("O ficheiro não existe");
            return false;
        }
        Docente docente;
        int index = 0;
        while (CSVReader.hasNext()) {
            try {
                index++;
                if (!data.addDocente(readDocente(index))) {
                    throw new InvalidField("Linha " + index + " -> Email já registado num docente");
                }
            }
            catch (InvalidField | IncompleteCSVLine e){
                if(col == null){
                    col = new CollectionBaseException();
                }
                col.putException(e);
            }
            if(!CSVReader.nextLine())
                break;
        }
        CSVReader.closeReaders();
        if(col != null)
            throw col;

        return index != 1;
    }

    // Le um docente numa linha de csv
    private Docente readDocente(int index) throws IncompleteCSVLine, InvalidField {
        String email, nome;

        try {

            nome = CSVReader.readString();
            email = CSVReader.readString();
        } catch (NoSuchElementException e) {
            throw new IncompleteCSVLine("Linha " + index +" -> Linha Incompleta");
        }
        checkDocente(index, email);
        return new Docente(email, nome);
    }

    // Verifica se o email esta ja atribuido a um aluno
    private boolean checkDocente(int index, String email) throws InvalidField {
        if(data.existeAlunoComEmail(email)){
            throw new InvalidField("Linha " + index + " -> Email já registado num aluno.");
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
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }
        List<Docente> docentes = data.getDocente();
        for (Docente d: docentes){
            CSVWriter.writeLine(",",true,false, d.getExportDocente());
        }
        CSVWriter.closeFile();
        return true;
    }

    @Override
    public String getDocentesToString() {
        return data.getDocentesToString();
    }
}
