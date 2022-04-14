package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;

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
        return data.setOrientador(emailDocente, id);
    }

    @Override
    public boolean execute() {
        return data.removerOrientador(emailDocente, id);
    }
}
