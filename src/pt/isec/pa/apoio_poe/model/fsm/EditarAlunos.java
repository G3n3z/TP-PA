package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.MessageCenter;
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
    public void changeCursoAluno(String novo_curso, long nAluno) {
        if(!data.existeCursos(novo_curso)){
            MessageCenter.getInstance().putMessage("Nao existe o curso inserido");
            return;
        }
        if(data.changeCursoAluno(novo_curso, nAluno)){
            MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
        }

    }

    @Override
    public void changeRamoAluno(String novo_ramo, long nAluno) {
        if(!data.existeRamos(novo_ramo)){
            MessageCenter.getInstance().putMessage("Nao existe o ramo inserido");
            return;
        }
        if(data.changeRamoAluno(novo_ramo, nAluno)){
            MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
        }
    }

    @Override
    public void changeClassAluno(double nova_classificacao, long nAluno) {
        if (nova_classificacao < 0.0 || nova_classificacao > 1.0){
            MessageCenter.getInstance().putMessage("Classificação nao se encontra entre 0.0 e 1.0");
        }
        if(data.changeClassAluno(nova_classificacao, nAluno)){
            MessageCenter.getInstance().putMessage("Numero de Aluno inexistente");
        }
    }
}
