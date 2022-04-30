package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.InvalidArguments;
import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;

public class EditarAlunos extends StateAdapter{

    public EditarAlunos(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.EDITAR_ALUNOS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.GESTAO_ALUNOS);
        return true;
    }
    @Override
    public boolean changeName(String novo_nome, long naluno) {
        return data.changeNameAluno(naluno, novo_nome);
    }

    @Override
    public void changeCursoAluno(String novo_curso, long nAluno) throws InvalidArguments {
        if(!data.existeCursos(novo_curso)){
            throw new InvalidArguments("Nao existe o curso inserido");
        }
        if(data.changeCursoAluno(novo_curso, nAluno)){
            throw new InvalidArguments("Numero de Aluno inexistente");
        }

    }

    @Override
    public void changeRamoAluno(String novo_ramo, long nAluno) throws InvalidArguments {
        if(!data.existeRamos(novo_ramo)){
            throw new InvalidArguments("Nao existe o ramo inserido");

        }
        if(data.changeRamoAluno(novo_ramo, nAluno)){
            throw new InvalidArguments("Numero de Aluno inexistente");
        }
    }

    @Override
    public void changeClassAluno(double nova_classificacao, long nAluno) throws InvalidArguments {
        if (nova_classificacao < 0.0 || nova_classificacao > 1.0){
            throw new InvalidArguments("Classificação nao se encontra entre 0.0 e 1.0");
        }
        if(data.changeClassAluno(nova_classificacao, nAluno)){
            throw new InvalidArguments("Numero de Aluno inexistente");
        }
    }
}
