package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestaoOrientadores extends StateAdapter{

    public GestaoOrientadores(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_ORIENTADORES);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_ORIENTADORES;
    }

    /**
     *
     * @return retorna string com os alunos com proposta e orientador
     */
    @Override
    public String getAlunosComPropostaEOrientador() {
        StringBuilder sb = new StringBuilder();
        for (Aluno a : data.getAlunos()){
            if (a.temPropostaConfirmada()){
                if(a.getProposta().temDocenteOrientador()){
                    sb.append("O aluno ").append(a.getNumeroAluno()).append(" - ").append(a.getNome()).append(" tem a proposta ").append(a.getProposta().getId())
                            .append(" Orientada pelo docente: ").append(a.getProposta().getEmailOrientador()).append('\n');
                }
            }
        }
        return sb.toString();
    }

    /**
     *
     * @return retorna uma copia das propostas com orientador
     */
    @Override
    public List<Proposta> getPropostasComOrientador() {
        List<Proposta> propostas = data.getPropostasCopia();
        propostas.removeIf(proposta -> !proposta.temDocenteOrientador());
        return propostas;
    }

}
