package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

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
    public ErrorCode changeTitulo(String id, String novo_titulo){
        Proposta p = getPropostaById(id);
        if (p == null)
            return ErrorCode.E9;
        p.setTitulo(novo_titulo);
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode changeEntidade(String id, String nova_entidade) {
        Proposta p = getPropostaById(id);
        if(p == null){
            return ErrorCode.E9;
        }
        if(p instanceof Estagio e){
           e.changeEntidade(nova_entidade);
        }else {
            //MessageCenter.getInstance().putMessage("Apenas existe entidade nos estágios");
            return ErrorCode.E8;
        }
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode addRamo(String id, String ramo)  {
        if(!data.existeRamos(ramo)){
            //MessageCenter.getInstance().putMessage("Ramo inexistente");
            return ErrorCode.E7;
        }
        Proposta p = getPropostaById(id);
        if(p == null){
            return ErrorCode.E9;
        }
        if(p.getRamos()!= null && p.getRamos().contains(ramo)){
            //MessageCenter.getInstance().putMessage("A proposta já contem o ramo inserido");
            return ErrorCode.E10;
        }
        p.addRamo(ramo);
        return ErrorCode.E0;
    }

    private Proposta getPropostaById(String id) {
        List<Proposta> propostas = data.getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(id));
        if(propostas.size() == 0){
            //MessageCenter.getInstance().putMessage("Nao existe a proposta com o id " + id);
            return null;
        }
        return propostas.get(0);
    }

    @Override
    public ErrorCode removeRamo(String id, String ramo) {
        if(!data.existeRamos(ramo)){
            //MessageCenter.getInstance().putMessage("Ramo inexistente");
            return ErrorCode.E7;
        }
        Proposta p = getPropostaById(id);
        if(p == null){
            return ErrorCode.E9;
        }
        if(p.getRamos()!= null && !p.getRamos().contains(ramo)){
            //MessageCenter.getInstance().putMessage("A proposta já não contem o ramo inserido");
            return ErrorCode.E27;
        }
        p.removeRamo(ramo);
        return ErrorCode.E0;
    }
}
