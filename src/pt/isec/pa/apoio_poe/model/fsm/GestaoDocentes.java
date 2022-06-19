package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
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

    /**
     * Funcao de importação de docentes de ficheiro CSV
     * @param file path para o ficheiro a ser lido
     * @return retorna ErrorCode com o resultado da operacao
     * @throws CollectionBaseException pode lançar excessao se existir problemas com os dados a serem lidos porem
     * insere os docentes validos
     */
    @Override
    public boolean importDocentes(String file) throws CollectionBaseException {
        CollectionBaseException col = null;
        if(!CSVReader.startScanner(file,",")){
            return false;
        }
        Docente docente;
        int index = 0;
        while (CSVReader.hasNext()) {
            try {
                index++;
                if (!data.addDocente(readDocente(index))) {
                    throw new InvalidCSVField("Linha " + index + " -> Email já registado num docente");
                }
            }
            catch (InvalidCSVField | IncompleteCSVLine e){
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

    /**
     * Le linha de um csv
     * @param index index da linha em que o csv esta aberto
     * @return retorna Docente lido do csv
     * @throws IncompleteCSVLine lança excessão se existir um uma linha incompleta
     * @throws InvalidCSVField lança excessão se existir um campo invalido
     */
    private Docente readDocente(int index) throws IncompleteCSVLine, InvalidCSVField {
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

    /**
     * Verifica se o email esta ja atribuido a um aluno
     * @param index index da linha do csv
     * @param email email cdo docente a ser verificado
     * @return true se correu bem
     * @throws InvalidCSVField lança excessao se algum dado estiver corrompido
     */
    private boolean checkDocente(int index, String email) throws InvalidCSVField {
        if(data.existeAlunoComEmail(email)){
            throw new InvalidCSVField("Linha " + index + " -> Email já registado num aluno.", index, ErrorCode.E12);
        }
        return true;
    }

    /**
     *  remove um docente por email
     * @param email email do docente
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode removeDocente(String email) {
        Docente d = data.getDocente(email);
        if(d == null){
            //MessageCenter.getInstance().putMessage("Email não registado em nenhum docente");
            return ErrorCode.E4;
        }
        data.removeDocente(d);
        return ErrorCode.E0;
    }


    /**
     * exporta dados dos docentes
     * @param file path para o ficheiro a ser criado
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return ErrorCode.E2;
        }
        List<Docente> docentes = data.getDocentes();
        for (Docente d: docentes){
            CSVWriter.writeLine(",",true,false, d.getExportDocente());
        }
        CSVWriter.closeFile();
        return ErrorCode.E0;
    }

    /**
     * remove todos os docentes
     * @return true se correu bem
     */
    @Override
    public boolean removeAll() {
        for (Docente d : data.getDocentes()){
            data.removeDocente(d);
        }
        return true;
    }

    /**
     * Funcao de edicao de nome
     * @param novo_nome novo nome do docente
     * @param email email do docente a ser editado
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode changeNameDocente(String novo_nome, String email) {
        if(!data.changeNameDocente(novo_nome, email)){
            //MessageCenter.getInstance().putMessage("Nao existe docente com este email");
            return ErrorCode.E4;
        }
        return ErrorCode.E0;
    }

    /**
     * Inserção manual de um docente
     * @param email email do docente a ser criado
     * @param nome nome do docente a ser criado
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode insereDocente(String email, String nome) {
        Docente d = new Docente(email, nome);
        try {
            checkDocente(0,d.getEmail());
        } catch (InvalidCSVField e) {
            return ErrorCode.E12;
        }
        if(!data.addDocente(d)) {
            return ErrorCode.E12;
        }
        return ErrorCode.E0;
    }

    /**
     * Edicao de um docente
     * @param email email do docente a ser editado
     * @param nome novo nome do docente
     * @return retorna ErrorCode com o resultado da operacao
     */
    public ErrorCode editDocente(String email, String nome) {
        Docente d = new Docente(email, nome);
        if(!data.editDocente(d)){
            return ErrorCode.E4;
        }
        return ErrorCode.E0;
    }
}
