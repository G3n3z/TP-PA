package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;

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
        return new AtribuicaoOrientadorProposta(data, emailDocente, id).execute();
    }

    @Override
    public boolean undo() {
        return new AtribuicaoOrientadorProposta(data, oldEmailDocente, id).execute();
    }
}
