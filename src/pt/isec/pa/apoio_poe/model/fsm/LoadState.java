package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadState extends StateAdapter{

    public LoadState(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.LOAD_STATE;
    }

    @Override
    public boolean load() throws IOException, ClassNotFoundException {
        try{
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(data.getFicheiroBin()));
            data = (Data) oos.readObject();
            context.setData(data);
            changeState(data.getLastState());
        }catch (Exception e){
            throw e;
        }
        return true;
    }

    @Override
    public void begin() {
        changeState(EnumState.CONFIG_OPTIONS);
    }

    @Override
    public boolean existFileBin() {
        return Files.exists(Path.of(data.getFicheiroBin()));
    }
}
