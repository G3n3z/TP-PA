package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidField;
import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.List;
import java.util.NoSuchElementException;

public class GestaoAlunos extends StateAdapter{

    public GestaoAlunos(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_ALUNOS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public void editarAlunos() {
        changeState(EnumState.EDITAR_ALUNOS);
    }


    @Override
    public boolean addAluno(String file) throws CollectionBaseException {
        if(!CSVReader.startScanner(file, ",")){
            Log.getInstance().putMessage("O ficheiro não existe\n");
            return false;
        }
        CollectionBaseException col = null;
        Aluno a;
        int index = 0;
        while (CSVReader.hasNext()) {
            try{
                index++;
                a = readAluno(index);
                if(!data.addAluno(a)){
                   throw new InvalidField("Na linha " + index + " -> Numero de aluno já registado");
                }
            }catch (InvalidField | IncompleteCSVLine e){
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

    // le um aluno de uma linha do ficheiro CSV
    private Aluno readAluno(int index) throws InvalidField, IncompleteCSVLine {
        String email, nome, ramo, curso;
        Long numAluno;
        Double classificacao;
        Boolean possibilidade;
        try {
            numAluno = CSVReader.readLong2();
            nome = CSVReader.readString();
            email = CSVReader.readString();
            curso = CSVReader.readString();
            ramo = CSVReader.readString();
            classificacao = CSVReader.readDouble2();
            possibilidade = CSVReader.readBoolean2();
        } catch (InvalidField e){
            e.addToBeginMessage("Na linha " + index + " -> ");
            e.putLine(index);
            //e.putAluno(new Aluno(email, nome, numAluno,curso,ramo, classificacao, possibilidade));
            throw e;
        }catch (NoSuchElementException e) {
            IncompleteCSVLine ex = new IncompleteCSVLine("Na linha " + index + " -> Linha Incompleta");
            ex.putLine(index);
            throw ex;
        }

        fieldsCorrect(index, email, curso, ramo, classificacao);

        return new Aluno(email, nome, numAluno, curso, ramo, classificacao, possibilidade);

    }

    // verifica se campos corretos
    private void fieldsCorrect(int index, String email, String curso, String ramo, double classificacao) throws InvalidField {
        boolean ok = true;
        StringBuilder sb = new StringBuilder();
        if(data.existeDocenteComEmail(email)){
            //Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um docente registado");
            sb.append("Email já registado num docente. ");
            ok = false;
        }
        if(data.existeAlunoComEmail(email)){
            //Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um aluno registado");
            sb.append("Email já registado num aluno. ");
            ok = false;
        }
        if (!data.existeCursos(curso)){
            //Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um curso inexistente");
            ok = false;
            sb.append("O curso não existe. ");
        }
        if (!data.existeRamos(ramo)){
            //Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um ramo inexistente");
            ok =  false;
            sb.append("O ramo não existe. ");
        }
        if(classificacao < 0 || classificacao > 1.0){
            //Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com uma classificação nao compreendidada" +
            //        "entre 0.0 e 1.0");
            ok =  false;
            sb.append("Classificação nao compreendidada entre 0.0 e 1.0.");
        }
        if(!ok){
            throw new InvalidField("Na linha " + index + " -> " + sb);
        }
    }




    @Override
    public void removeAluno(long numero_de_aluno) {
        if(data.removeAluno(numero_de_aluno)){
            Log.getInstance().putMessage("Numero de Aluno inexistente");
        }
    }



    @Override
    public boolean exportarCSV(String file) {
        List<Aluno> alunos;
        if(CSVWriter.startWriter(file)){
            alunos = data.getAlunos();
            for(Aluno a: alunos){
                CSVWriter.writeLine(",",true, false, a.getExportAluno());
            }

            CSVWriter.closeFile();
            return true;
        }
        return false;
    }

    @Override
    public String getAlunosToString() {
        return data.getAlunosToString();
    }
}
