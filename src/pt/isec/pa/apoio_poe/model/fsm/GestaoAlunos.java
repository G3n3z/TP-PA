package pt.isec.pa.apoio_poe.model.fsm;

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
    public boolean addAluno(String file) {
        String email, nome, ramo, curso;
        long numAluno;
        double classificacao;
        boolean possibilidade;
        Aluno aluno;
        if(!CSVReader.startScanner(file, ",")){
            Log.getInstance().putMessage("O ficheiro não existe\n");
            return false;
        }
        int index = 1;


        while (CSVReader.hasNext()) {
            try {
                numAluno = CSVReader.readLong();
                nome = CSVReader.readString();
                email = CSVReader.readString();
                curso = CSVReader.readString();
                ramo = CSVReader.readString();
                classificacao = CSVReader.readDouble();
                possibilidade = CSVReader.readBoolean();
            } catch (NoSuchElementException e) {
                Log.getInstance().putMessage("Erro de leitura na linha: " + index + " do ficheiro: " + file);
                if(!CSVReader.nextLine()) break;
                index++;
                continue;
            }
            if(fieldsCorrect(index,email, curso, ramo, classificacao)){
                aluno = new Aluno(email, nome, numAluno, curso, ramo, classificacao, possibilidade);
                if (!data.addAluno(aluno)) {
                    Log.getInstance().putMessage("Aluno nao inserido no index " + index);
                }
            }
            index++;
            if(!CSVReader.nextLine()) break;
        }
        CSVReader.closeReaders();

        return index!=1;
    }

    private boolean fieldsCorrect(int index,String email, String curso, String ramo, double classificacao) {
        boolean ok = true;
        if(data.existeDocenteComEmail(email)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um docente registado");
            ok = false;
        }
        if(data.existeAlunoComEmail(email)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um aluno registado");
            ok = false;
        }
        if (!data.existeCursos(curso)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um curso inexistente");
            ok = false;
        }
        if (!data.existeRamos(ramo)){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um ramo inexistente");
            ok =  false;
        }
        if(classificacao < 0 || classificacao > 1.0){
            Log.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com uma classificação nao compreendidada" +
                    "entre 0.0 e 1.0");
            ok =  false;
        }
        return ok;
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
                CSVWriter.writeLine(",",true, a.getExportAluno());
            }

            CSVWriter.closeFile();
            return true;
        }
        return false;
    }
}
