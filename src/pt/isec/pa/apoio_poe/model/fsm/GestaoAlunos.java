package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
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
    public boolean addAluno(String file) throws CollectionBaseException {
        if(!CSVReader.startScanner(file, ",")){
            //MessageCenter.getInstance().putMessage("O ficheiro não existe\n"); //TODO
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
                   throw new InvalidCSVField("Na linha " + index + " -> Numero de aluno já registado");
                }
            }catch (InvalidCSVField | IncompleteCSVLine e){
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
    private Aluno readAluno(int index) throws InvalidCSVField, IncompleteCSVLine {
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
        } catch (InvalidCSVField e){
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
    private void fieldsCorrect(int index, String email, String curso, String ramo, double classificacao) throws InvalidCSVField {
        boolean ok = true;
        StringBuilder sb = new StringBuilder();
        if(data.existeDocenteComEmail(email)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um docente registado");
            sb.append("Email já registado num docente. ");
            ok = false;
        }
        if(data.existeAlunoComEmail(email)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um aluno registado");
            sb.append("Email já registado num aluno. ");
            ok = false;
        }
        if (!data.existeCursos(curso)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um curso inexistente");
            ok = false;
            sb.append("O curso não existe. ");
        }
        if (!data.existeRamos(ramo)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um ramo inexistente");
            ok =  false;
            sb.append("O ramo não existe. ");
        }
        if(classificacao < 0 || classificacao > 1.0){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com uma classificação nao compreendidada" +
            //        "entre 0.0 e 1.0");
            ok =  false;
            sb.append("Classificação nao compreendidada entre 0.0 e 1.0.");
        }
        if(!ok){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb);
        }
    }




    @Override
    public ErrorCode removeAluno(long numero_de_aluno) {
        Aluno a = data.getAluno(numero_de_aluno);
        if(a == null){
            //MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
            return ErrorCode.E3;
        }
        data.removeAluno(a);
        return ErrorCode.E0;
    }



    @Override
    public ErrorCode exportarCSV(String file) {
        List<Aluno> alunos;
        if(CSVWriter.startWriter(file)){
            alunos = data.getAlunos();
            for(Aluno a: alunos){
                CSVWriter.writeLine(",",true, false, a.getExportAluno());
            }

            CSVWriter.closeFile();
            return ErrorCode.E0;
        }
        return ErrorCode.E2;
    }

    @Override
    public boolean removeAll(){
        for (Aluno a : data.getAlunos()){
            data.removeAluno(a);
        }
        return true;
    }

    @Override
    public ErrorCode changeName(String novo_nome, long naluno) {
        return data.changeNameAluno(naluno, novo_nome) ? ErrorCode.E0 : ErrorCode.E3;
    }

    @Override
    public ErrorCode changeCursoAluno(String novo_curso, long nAluno) {
        if(!data.existeCursos(novo_curso)){
            //MessageCenter.getInstance().putMessage("Nao existe o curso inserido");
            return ErrorCode.E5;
        }
        if(!data.changeCursoAluno(novo_curso, nAluno)){
            //MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode changeRamoAluno(String novo_ramo, long nAluno) {
        if(!data.existeRamos(novo_ramo)){
            //MessageCenter.getInstance().putMessage("Nao existe o ramo inserido");
            return ErrorCode.E7;
        }
        if(!data.changeRamoAluno(novo_ramo, nAluno)){
            //MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode changeClassAluno(double nova_classificacao, long nAluno) {
        if (nova_classificacao < 0.0 || nova_classificacao > 1.0){
            //MessageCenter.getInstance().putMessage("Classificação nao se encontra entre 0.0 e 1.0");
            return ErrorCode.E6;
        }
        if(!data.changeClassAluno(nova_classificacao, nAluno)){
            //MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode changePossibilidadeAluno(long nAluno){
        if(!data.changePossibilidadeAluno(nAluno)){
            //MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode insereAluno(Aluno a) {
        try {
            fieldsCorrect(0,a.getEmail(),a.getSiglaCurso(), a.getSiglaRamo(), a.getClassificacao());
        } catch (InvalidCSVField e) {
            return ErrorCode.E3;
        }
        if(!data.addAluno(a)){
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }
}
