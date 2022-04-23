package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.util.HashSet;
import java.util.Set;

public class Obtencao_Lista_Propostas extends StateAdapter{

    public Obtencao_Lista_Propostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.OBTENCAO_LISTA_PROPOSTAS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

    public Set<Proposta> getPropostasWithFilters(int ...filters){
        Set<Proposta> propostas = new HashSet<>();
        for (int i : filters){
            switch (i){
                case 1 -> propostas.addAll(data.getAutoPropostas());
                case 2 -> propostas.addAll(data.getProjetos());
                case 3 -> propostas.addAll(data.getPropostasComCandidatura());
                case 4 -> propostas.addAll(data.getPropostasSemCandidatura());
            }
        }
        return propostas;
    }



    @Override
    public String getPropostasWithFiltersToString(int[] filters) {
        StringBuilder sb = new StringBuilder();
        getPropostasWithFilters(filters).forEach( p -> sb.append(p).append("\n"));
        return sb.toString();
    }
}
