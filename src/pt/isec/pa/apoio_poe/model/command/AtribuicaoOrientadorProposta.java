package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;

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
        if(data.verificaDocente(emailDocente)){
            if (!data.setOrientador(emailDocente, idProposta)) {
                Log.getInstance().putMessage("Nao existe a proposta");
            }else {
                return true;
            }
        }
        else {
            Log.getInstance().putMessage("Nao existe o docente");
        }
        return false;
    }

    @Override
    public boolean undo() {
        return data.removerOrientador(emailDocente, idProposta);
    }
}
