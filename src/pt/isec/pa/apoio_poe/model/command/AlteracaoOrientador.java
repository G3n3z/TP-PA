package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.Singleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

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
    public ErrorCode execute() {
        Docente d = data.getDocente(emailDocente);
        if(d == null) {
            //MessageCenter.getInstance().putMessage("Nao existe o docente");
            return ErrorCode.E4;
        }

        for (Proposta p : data.getProposta()){
            if(p.getId().equalsIgnoreCase(id) ){
                if(!p.temDocenteOrientador()){
                    //MessageCenter.getInstance().putMessage("Proposta nao tem docente orientador para alterar");
                    return ErrorCode.E16;
                }

                oldEmailDocente = p.getEmailOrientador();
                p.setDocenteOrientador(d);
                return ErrorCode.E0;
            }
        }
        //MessageCenter.getInstance().putMessage("Id de proposta inexistente");
        return ErrorCode.E9;
    }

    @Override
    public ErrorCode undo() {
        return new AlteracaoOrientador(data, oldEmailDocente, id).execute();
    }
}
