package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

import java.util.ArrayList;
import java.util.List;

public class AtribuicaoManualPropostas extends StateAdapter {

    public AtribuicaoManualPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    /**
     * Mudar de estado
     * @return retorna booleano se correu bem
     */
    @Override
    public boolean recuarFase(){
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() { return EnumState.ATRIBUICAO_MANUAL_PROPOSTAS; }

    /**
     * Obtem alunos com proposta confirmada
     * @return um List com uma copia dos alunos com proposta confirmada
     */
    @Override
    public List<Aluno> getAlunosComPropostaConfirmada() {
        List<Aluno> al = new ArrayList<>();
        for (Aluno a : data.getAlunos()) {
            if(a.temPropostaConfirmada()){
                al.add(a.getClone());
            }
        }
        return al;
    }

    /**
     *
     * @return um list com os alunos aos quais a atribuiçao de propostas nao foi previamente definido
     */
    @Override
    public List<Aluno> getAlunosComPropostaConfirmadaEditavel() {
        List<Aluno> al = new ArrayList<>();
        for (Aluno a : data.getAlunos()) {
            if(a.temPropostaConfirmada()){
                if(!(a.getProposta() instanceof Projeto_Estagio || a.getProposta().getNumAluno()!= null)){
                    al.add(a.getClone());
                }
            }
        }
        return al;
    }
}
