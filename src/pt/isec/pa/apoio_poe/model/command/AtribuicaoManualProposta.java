package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.util.ArrayList;
import java.util.Collections;

public class AtribuicaoManualProposta extends CommandAdapter{
    private long nAluno;
    private String idProposta;

    public AtribuicaoManualProposta(Data data, long nAluno ,String idProposta) {

        super(data);
        this.nAluno = nAluno;
        this.idProposta = idProposta;
    }

    /**
     * Atribui uma proposta a um aluno caso nao tenha nenmhuma proposta associada ao mesmo
     * ou a mesma tambem ainda nao esteja asssociada a nenhuma proposta e o aluno possa aceder a estagio
     * @return ErrorCode de sucesso ou insucesso da execução do comando
     */
    @Override
    public ErrorCode execute() {
         
        Aluno a = data.getAluno(nAluno);
        if(a == null) {
           //MessageCenter.getInstance().putMessage("Número de aluno " + nAluno + " incorreto.\n");
           return ErrorCode.E3;
        }
        Proposta p = data.getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(idProposta)).get(0);
        if(p instanceof Estagio && !a.isPossibilidade()) {
           //MessageCenter.getInstance().putMessage("Aluno " + nAluno + " não pode aceder a estágio.\n");
           return ErrorCode.E14;
        }
        if(p.isAtribuida()){
           //MessageCenter.getInstance().putMessage("Proposta já atribuida\n");
           return ErrorCode.E15;
        }
        a.setProposta(p);
        return ErrorCode.E0;
    }

    /**
     * Operacao de redo, execua o comando de remocao com os dados armazenados na classe da atribuicao realizada
     * @return ErrorCode de sucesso ou insucesso da execução do comando
     */
    @Override
    public ErrorCode undo() {
        return new RemocaoManualProposta(data, nAluno, idProposta).execute();
    }
}
