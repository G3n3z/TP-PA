package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.data.Data;

public class ApoioManager {
    private Data data;
    CommandManager cm;

    public ApoioManager(Data data) {
        this.data = data;
        cm = new CommandManager();
    }

    public boolean hasUndo() { return cm.hasUndo(); }
    public boolean undo() { return cm.undo(); }
    public boolean hasRedo() { return cm.hasRedo(); }
    public boolean redo() { return cm.redo(); }

    public boolean atribuicaoManual(long nAluno, String idProposta){
        return cm.invokeCommand(new AtribuicaoManualProposta(data, nAluno, idProposta));
    }
    public boolean remocaoManual(long nAluno, String idProposta){
        return cm.invokeCommand(new RemocaoManualProposta(data, nAluno, idProposta));
    }

}
