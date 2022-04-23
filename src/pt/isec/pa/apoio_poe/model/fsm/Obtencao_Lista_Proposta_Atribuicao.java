package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.HashSet;
import java.util.Set;

public class Obtencao_Lista_Proposta_Atribuicao extends StateAdapter{

    public Obtencao_Lista_Proposta_Atribuicao(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.OBTENCAO_LISTA_PROPOSTA_ATRIBUICAO;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public String getPropostasWithFiltersToStringAtribuicao(int[] filters) {
        StringBuilder sb = new StringBuilder();
        getPropostasWithFiltersAtribuicao(filters).forEach( p -> sb.append(p).append("\n"));
        return sb.toString();
    }

    public Set<Proposta> getPropostasWithFiltersAtribuicao(int ...filters){
        Set<Proposta> propostas = new HashSet<>();
        for (int i : filters){
            switch (i){
                case 1 -> propostas.addAll(data.getAutoPropostas());
                case 2 -> propostas.addAll(data.getProjetos());
                case 3 -> propostas.addAll(data.getPropostasSemAluno());
                case 4 -> propostas.addAll(data.getPropostasAtribuidas());
            }
        }
        return propostas;
    }



}
