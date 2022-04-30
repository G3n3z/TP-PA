package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.LogSingleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class AtribuicaoOrientadorProposta extends CommandAdapter {
    private String emailDocente;
    private String idProposta;
    public AtribuicaoOrientadorProposta(Data data, String emailDocente, String idProposta) {
        super(data);
        this.emailDocente = emailDocente;
        this.idProposta = idProposta;
    }

    @Override
    public boolean execute() {
        Docente d = data.getDocente(emailDocente);

        if(d == null){
            MessageCenter.getInstance().putMessage("Nao existe o docente");
            return false;
        }
        for (Proposta p : data.getProposta()){
            if(p.getId().equalsIgnoreCase(idProposta) ){
                if(!p.temDocenteOrientador()) {
                    p.setDocenteOrientador(d);
                    return true;
                }else {
                    MessageCenter.getInstance().putMessage("Proposta com docente Orientador");
                    return false;
                }
            }
        }
        MessageCenter.getInstance().putMessage("Id de proposta inexistente");
        return false;
    }

    @Override
    public boolean undo() {
        return new RemoverOrientadorProposta(data,emailDocente,idProposta).execute();
    }
}
