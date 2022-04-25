package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.InvalidArguments;
import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditarPropostas extends StateAdapter{
    public EditarPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.EDITAR_PROPOSTAS;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.GESTAO_PROPOSTAS);
        return true;
    }

    @Override
    public void changeTitulo(String id, String novo_titulo) throws InvalidArguments {
        Proposta p = getPropostaById(id);
        if (p == null)
            return;
        p.setTitulo(novo_titulo);
    }

    @Override
    public void changeEntidade(String id, String nova_entidade) throws InvalidArguments {
        Proposta p = getPropostaById(id);
        if(p instanceof Estagio e){
           e.changeEntidade(nova_entidade);
        }else {
            throw new InvalidArguments("Apenas existe entidade nos estágios");
        }
    }

    @Override
    public void addRamo(String id, String ramo) throws InvalidArguments {
        if(!data.existeRamos(ramo)){
            throw new InvalidArguments("Ramo inexistente");
        }
        Proposta p = getPropostaById(id);
        if(p.getRamos()!= null && p.getRamos().contains(ramo)){
            throw new InvalidArguments("A proposta já contem o ramo inserido");
        }
        p.addRamo(ramo);
    }

    private Proposta getPropostaById(String id) throws InvalidArguments {
        List<Proposta> propostas = data.getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(id));
        if(propostas.size() == 0){
            throw new InvalidArguments("Nao existe a proposta com o id " + id);
        }
        return propostas.get(0);
    }

    @Override
    public void removeRamo(String id, String ramo) throws InvalidArguments {
        if(!data.existeRamos(ramo)){
            throw new InvalidArguments("Ramo inexistente");
        }
        Proposta p = getPropostaById(id);
        if(p.getRamos()!= null && !p.getRamos().contains(ramo)){
            throw new InvalidArguments("A proposta já não contem o ramo inserido");
        }
        p.removeRamo(ramo);
    }
}
