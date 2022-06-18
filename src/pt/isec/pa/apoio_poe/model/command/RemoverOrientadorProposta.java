package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public class RemoverOrientadorProposta extends CommandAdapter{
    private String emailDocente;
    private String id;

    /**
     *
     * @param data Classe em que estao armazenados os dados
     * @param emailDocente Email do docente a alterar
     * @param id Id da proposta a ser feita a alteracao
     */
    public RemoverOrientadorProposta(Data data, String emailDocente, String id) {

        super(data);
        this.emailDocente = emailDocente;
        this.id = id;
    }

    /**
     *
     * @return ErrorCode de sucesso ou insucesso da execução do comando
     */
    @Override
    public ErrorCode undo() {
        return new AtribuicaoOrientadorProposta(data, emailDocente, id).execute();
    }


    /**
     *  Remocao do orientador de determinada proposta com @param id
     * @return ErrorCode de sucesso ou insucesso da execução do comando
     */
    @Override
    public ErrorCode execute() {
        for (Proposta p : data.getProposta()){
            if(p.getId().equalsIgnoreCase(id)){
                if(!p.temDocenteOrientador())
                    //MessageCenter.getInstance().putMessage("Proposta sem docente orientador");
                    return ErrorCode.E16;
                else {
                    emailDocente = p.getEmailOrientador();
                    p.removeOrientador();
                }
                return ErrorCode.E0;
            }
        }
        //MessageCenter.getInstance().putMessage("Proposta inexistente");
        return ErrorCode.E9;
    }
}
