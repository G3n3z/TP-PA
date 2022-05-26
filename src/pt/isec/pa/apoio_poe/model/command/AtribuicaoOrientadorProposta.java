package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.Singleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public class AtribuicaoOrientadorProposta extends CommandAdapter {
    private String emailDocente;
    private String idProposta;
    public AtribuicaoOrientadorProposta(Data data, String emailDocente, String idProposta) {
        super(data);
        this.emailDocente = emailDocente;
        this.idProposta = idProposta;
    }

    @Override
    public ErrorCode execute() {
        Docente d = data.getDocente(emailDocente);

        if(d == null){
            //MessageCenter.getInstance().putMessage("Nao existe o docente");
            return ErrorCode.E4;
        }
        for (Proposta p : data.getProposta()){
            if(p.getId().equalsIgnoreCase(idProposta) ){
                if(!p.temDocenteOrientador()) {
                    p.setDocenteOrientador(d);
                    return ErrorCode.E0;
                }else {
                    //MessageCenter.getInstance().putMessage("Proposta com docente Orientador");
                    return ErrorCode.E30;
                }
            }
        }
        //MessageCenter.getInstance().putMessage("Id de proposta inexistente");
        return ErrorCode.E9;
    }

    @Override
    public ErrorCode undo() {
        return new RemoverOrientadorProposta(data,emailDocente,idProposta).execute();
    }
}
