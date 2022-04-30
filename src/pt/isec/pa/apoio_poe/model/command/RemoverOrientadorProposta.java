package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.LogSingleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class RemoverOrientadorProposta extends CommandAdapter{
    private String emailDocente;
    private String id;
    public RemoverOrientadorProposta(Data data, String emailDocente, String id) {

        super(data);
        this.emailDocente = emailDocente;
        this.id = id;
    }

    @Override
    public boolean undo() {
        return new AtribuicaoOrientadorProposta(data, emailDocente, id).execute();
    }

    @Override
    public boolean execute() {
        for (Proposta p : data.getProposta()){
            if(p.getId().equalsIgnoreCase(id)){
                if(!p.temDocenteOrientador())
                    MessageCenter.getInstance().putMessage("Proposta sem docente orientador");
                else {
                    emailDocente = p.getEmailOrientador();
                    p.removeOrientador();
                }
                return true;
            }
        }
        MessageCenter.getInstance().putMessage("Proposta inexistente");
        return false;
    }
}
