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


    /**
     * Função de importação de dados relativos a alunos de CSV
     * @param file path para o ficheiro a importar
     * @return booleano com o resultado da operação
     * @throws CollectionBaseException pode lançar um container de exceções caso existe algum dado invalido, no entanto <p></p>
     * insere todos os alunos validos
     */
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

    /**
     *  le um aluno de uma linha do ficheiro CSV
     * @param index recebe um index da linha do CSV que está a ler
     * @return retorna um aluno criado
     * @throws InvalidCSVField lança invalidCSVFiel se existir um campo com dados do tipo errado ou não validos
     * @throws IncompleteCSVLine lança esta excessão caso a linha do CSV não tenho dados suficientes
     */
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


    /**
     * verifica se campos do aluno a inserir estão corretos
     * @param index index do ficheiro csv
     * @param email email do aluno
     * @param curso curso do aluno
     * @param ramo ramo do aluno
     * @param classificacao classificação
     * @throws InvalidCSVField lançar esta excessão caso alguma das validações corra mal
     */
    private void fieldsCorrect(int index, String email, String curso, String ramo, double classificacao) throws InvalidCSVField {
        boolean ok = true;
        StringBuilder sb = new StringBuilder();
        ErrorCode e = ErrorCode.E0;
        if(data.existeDocenteComEmail(email)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um docente registado");
            sb.append("Email já registado num docente. ");
            ok = false;
            e = ErrorCode.E4;
        }
        if(data.existeAlunoComEmail(email)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um email de um aluno registado");
            sb.append("Email já registado num aluno. ");
            ok = false;
            e = ErrorCode.E12;
        }
        if (!data.existeCursos(curso)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um curso inexistente");
            ok = false;
            sb.append("O curso não existe. ");
            e = ErrorCode.E5;
        }
        if (!data.existeRamos(ramo)){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com um ramo inexistente");
            ok =  false;
            sb.append("O ramo não existe. ");
            e = ErrorCode.E7;
        }
        if(classificacao < 0 || classificacao > 1.0){
            //MessageCenter.getInstance().putMessage("Na linha " + index + " está a tentar inserir um aluno com uma classificação nao compreendidada" +
            //        "entre 0.0 e 1.0");
            ok =  false;
            sb.append("Classificação nao compreendidada entre 0.0 e 1.0.");
            e = ErrorCode.E6;
        }
        if(!ok){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb,index,e);
        }
    }


    /**
     *  remove aluno por numero de aluno
     * @param numero_de_aluno numero do aluno a remover
     * @return ErrorCOde com o resultado da operação
     */
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


    /**
     *
     * @param file path para o ficheiro a guarda a informação
     * @return retorna true se a função correr bem
     */
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

    /**
     * Função que remove todos os alunos
     * @return retorna true se conclui com sucesso
     */
    @Override
    public boolean removeAll(){
        for (Aluno a : data.getAlunos()){
            data.removeAluno(a);
        }
        return true;
    }

    /**
     *
     * @param novo_nome novo nome do aluno a ser alterado
     * @param naluno numero do aluno a ser editado
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode changeName(String novo_nome, long naluno) {
        return data.changeNameAluno(naluno, novo_nome) ? ErrorCode.E0 : ErrorCode.E3;
    }

    /**
     * Funcao que altera o curso do aluno
     * @param novo_curso siga do curso
     * @param nAluno numero de aluno a ser editado
     * @return retorna ErrorCode com o resultado da operacao
     */
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


    /**
     * Funcao que altera o ramo de um aluno
     * @param novo_ramo siga do ramo
     * @param nAluno numero de aluno a ser editado
     * @return retorna ErrorCode com o resultado da operacao
     */
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

    /**
     *  Funcao que altera a classificação de um aluno
     * @param nova_classificacao nova classificação
     * @param nAluno numero de aluno a ser editado
     * @return retorna ErrorCode com o resultado da operacao
     */
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

    /**
     * Altera a possiblidade do aluno aceder ao estagio
     * @param nAluno numero do aluno a ser editado
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode changePossibilidadeAluno(long nAluno){
        if(!data.changePossibilidadeAluno(nAluno)){
            //MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }

    /**
     *
     * @param a aluno a ser inserido nos dados
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode insereAluno(Aluno a) {
        try {
            fieldsCorrect(0,a.getEmail(),a.getSiglaCurso(), a.getSiglaRamo(), a.getClassificacao());
        } catch (InvalidCSVField e) {
            return e.getErrorCode();
        }
        if(!data.addAluno(a)){
            return ErrorCode.E3;
        }
        return ErrorCode.E0;
    }

    /**
     * Funcao de edição dos campos possiveis de alteração de um aluno
     * @param email email do aluno
     * @param nome nome do aluno
     * @param nAluno numero do aluno
     * @param curso curso do aluno
     * @param ramo ramo do aluno
     * @param classificacao classificação do aluno
     * @param isPossible possibilidade de acesso ao estagio do aluno
     * @return retorna ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode editAluno(String email, String nome, Long nAluno, String curso, String ramo, Double classificacao, Boolean isPossible){
        Aluno a = new Aluno(email, nome, nAluno, curso, ramo, classificacao, isPossible);
        ErrorCode error = verificaDados(a);

        if(error != ErrorCode.E0){
            return error;
        }

        if(!data.editAluno(a)){
            return ErrorCode.E3;
        }
        return error;
    }


    private ErrorCode verificaDados(Aluno a) {
        if(!data.existeCursos(a.getSiglaCurso())){
            return ErrorCode.E5;
        }
        if(!data.existeRamos(a.getSiglaRamo())){
            return ErrorCode.E7;
        }
        if(a.getClassificacao() < 0.0 || a.getClassificacao() > 1.0){
            return ErrorCode.E6;
        }
        return ErrorCode.E0;
    }
}
