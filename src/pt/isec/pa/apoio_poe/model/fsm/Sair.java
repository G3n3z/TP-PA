package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Sair extends StateAdapter{

    public Sair(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.SAIR;
    }
    @Override
    public boolean save() throws IOException {
        try {
            ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(data.getFicheiroBin()));
            ois.writeObject(data);

        }catch (Exception e){
            throw e;
        }
        return true;
    }
}
