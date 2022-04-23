package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

public class Obtencao_Lista_Alunos extends StateAdapter{
    public Obtencao_Lista_Alunos(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.OBTENCAO_LISTA_ALUNOS;
    }
    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public String obtencaoAlunosComAutoProposta() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getPropostaNaoConfirmada() instanceof Projeto_Estagio).forEach(sb::append);
        return sb.toString();
    }

    @Override
    public String obtencaoAlunosComCandidatura() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getCandidatura() != null).forEach(sb::append);
        return sb.toString();
    }

    @Override
    public String obtencaoAlunosSemCandidatura() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(a -> a.getCandidatura() == null && (!a.temPropostaNaoConfirmada() && !a.temPropostaConfirmada())).forEach(sb::append);
        return sb.toString();
    }
}
