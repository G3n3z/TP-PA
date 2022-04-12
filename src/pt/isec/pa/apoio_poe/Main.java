package pt.isec.pa.apoio_poe;

import pt.isec.pa.apoio_poe.model.command.ApoioManager;
import pt.isec.pa.apoio_poe.model.fsm.ApoioContext;
import pt.isec.pa.apoio_poe.ui.text.ApoioUIText;

public class Main {

    public static void main(String[] args) {

        ApoioContext context = new ApoioContext();
        ApoioUIText ui = new ApoioUIText(context);
        ui.start(args);

    }
}
