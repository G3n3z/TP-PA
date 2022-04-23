package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

public class Obtencao_Lista_Alunos_Propostas extends StateAdapter{
    public Obtencao_Lista_Alunos_Propostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.OBTENCAO_LISTA_ALUNOS_PROPOSTAS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public String obtencaoAlunosComAutoPropostaAtribuida() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getProposta() instanceof Projeto_Estagio).forEach(sb::append);
        return sb.toString();
    }

    @Override
    public String getTodosAlunosComPropostaAtribuida() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(Aluno::temPropostaConfirmada).forEach(aluno -> {
            sb.append("Aluno: ").append(aluno.getNumeroAluno()).append(" ").append(aluno.getNome())
                    .append(" tem a proposta ").append(aluno.getProposta().getId()).append(" Ordem: ")
                    .append(aluno.getOrdem()).append("\n");
        });

        return sb.toString();
    }
    @Override
    public String obtencaoAlunosComCandidatura() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getCandidatura() != null).forEach(sb::append);
        return sb.toString();
    }

    @Override
    public String obtencaoAlunosSemProposta() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> !a.temPropostaNaoConfirmada() && !a.temPropostaConfirmada()).forEach(sb::append);
        return sb.toString();
    }
}
