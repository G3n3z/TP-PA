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

    /**
     *
     * @return return true a função correu como esperado
     * @throws IOException Pode lançar exceção caso nao consiga abrir o ficheiro
     * @throws ClassNotFoundException  Pode lançar exceção caso nao consiga ler os dados
     */
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

    /**
     * Muda o estado para o estado iniciald a aplicação sem dados armazenados
     */
    @Override
    public void begin() {
        changeState(EnumState.CONFIG_OPTIONS);
    }


    /**
     *
     * @return retorna false pois este estado nao permite a realização de guardar o estado da aplicação
     * @throws IOException
     */
    @Override
    public boolean save() throws IOException {
        return false;
    }
}
