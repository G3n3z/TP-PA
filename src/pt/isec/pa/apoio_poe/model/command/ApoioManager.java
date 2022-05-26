package pt.isec.pa.apoio_poe.model.command;

import pt.isec.pa.apoio_poe.model.Singleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public class ApoioManager {
    private Data data;
    CommandManager cm;

    public ApoioManager(Data data) {
        this.data = data;
        cm = new CommandManager();
    }

    public boolean hasUndo() {
        return cm.hasUndo();
    }
    public boolean undo() {
        if(hasUndo())
            return cm.undo();
        MessageCenter.getInstance().putMessage("Não é possivel fazer undo");
        return false;
    }

    public boolean hasRedo() {
        return cm.hasRedo();
    }
    public boolean redo() {
        if (hasRedo())
            return cm.redo();
        MessageCenter.getInstance().putMessage("Não é possivel fazer redo");
        return false;
    }

    public ErrorCode atribuicaoManual(long nAluno, String idProposta){
        return cm.invokeCommand(new AtribuicaoManualProposta(data, nAluno, idProposta));
    }
    public ErrorCode remocaoManual(long nAluno, String idProposta){
        return cm.invokeCommand(new RemocaoManualProposta(data, nAluno, idProposta));
    }

    public ErrorCode atribuirOrientador(String emailDocente, String idProposta) {
        return cm.invokeCommand(new AtribuicaoOrientadorProposta(data, emailDocente, idProposta));
    }

    public ErrorCode alterarDocente(String emailDocente, String idProposta) {
        return cm.invokeCommand(new AlteracaoOrientador(data, emailDocente, idProposta));
    }

    public ErrorCode removerDocente(String emailDocente, String idProposta) {
        return cm.invokeCommand(new RemoverOrientadorProposta(data, emailDocente, idProposta));
    }

    public ErrorCode removerTodasAtribuicoes() {
        return cm.invokeCommand(new RemocaoTotalAtribuicoes(data));
    }
}
