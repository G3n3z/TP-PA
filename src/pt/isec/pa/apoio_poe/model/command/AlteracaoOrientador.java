package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class AlteracaoOrientador extends CommandAdapter{
    private String emailDocente;
    private String id;
    private String oldEmailDocente;
    public AlteracaoOrientador(Data data, String emailDocente, String id) {
        super(data);
        oldEmailDocente = emailDocente;
        this.emailDocente = emailDocente;
        this.id = id;
    }

    @Override
    public boolean execute() {
        Docente d = data.getDocente(emailDocente);
        if(d == null) {
            Log.getInstance().putMessage("Nao existe o docente");
            return false;
        }

        for (Proposta p : data.getProposta()){
            if(p.getId().equalsIgnoreCase(id) ){
                if(!p.temDocenteOrientador()){
                    Log.getInstance().putMessage("Proposta nao tem docente orientador para alterar");
                    return false;
                }

                oldEmailDocente = p.getEmailOrientador();
                p.setDocenteOrientador(d);
                return true;
            }
        }
        Log.getInstance().putMessage("Id de proposta inexistente");
        return false;
    }

    @Override
    public boolean undo() {
        return new AlteracaoOrientador(data, oldEmailDocente, id).execute();
    }
}
