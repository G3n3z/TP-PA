package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.LogSingleton.Log;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;

public class ConfigOptions extends  StateAdapter{


    public ConfigOptions(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public EnumState getState() {
        return EnumState.CONFIG_OPTIONS;
    }

    @Override
    public boolean close() {
        if(verificaCondicaoFechoF1()) {
            setClose(true);
            return true;
        }
        Log.getInstance().putMessage("Condições de fecho de fase não alcançadas\n" +
                "Por favor verifique se o numero total de propostas é igual ou superior ao número total de alunos e se,\n" +
                "para cada ramo, o número total de propostas é igual ou superior ao número de alunos.\n");
        return false;
    }

    @Override
    public boolean gerirAlunos() {
        changeState(EnumState.GESTAO_ALUNOS);
        return true;
    }

    @Override
    public boolean gerirDocentes() {
        changeState(EnumState.GESTAO_DOCENTES);
        return true;
    }

    @Override
    public boolean gerirEstagios() {
        changeState(EnumState.GESTAO_PROPOSTAS);
        return true;
    }

    @Override
    public boolean avancarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }
    public boolean verificaCondicaoFechoF1() {
        int pDA = (int) data.getProposta().stream().filter(proposta -> proposta.getRamos() != null && proposta.getRamos().contains("DA")).count();
        int pRAS = (int) data.getProposta().stream().filter(proposta -> proposta.getRamos() != null && proposta.getRamos().contains("RAS")).count();
        int pSI = (int) data.getProposta().stream().filter(proposta -> proposta.getRamos() != null && proposta.getRamos().contains("SI")).count();
        int pDA_SI = (int) data.getProposta().stream().filter(proposta -> proposta.getRamos() != null && proposta.getRamos().contains("DA") && proposta.getRamos().contains("SI")).count();
        int pDA_RAS = (int) data.getProposta().stream().filter(proposta -> proposta.getRamos() != null && proposta.getRamos().contains("DA") && proposta.getRamos().contains("RAS")).count();
        int pRAS_SI = (int) data.getProposta().stream().filter(proposta -> proposta.getRamos() != null && proposta.getRamos().contains("RAS") && proposta.getRamos().contains("SI")).count();

        //5da 5si 2dasi -> 7ada 6asi -> da+si+dasi 12 TODO: consideracao dos alunos que ja estao atribuidos e remocao das propostas ja atribuidas

        int totDA = 0, totRAS = 0, totSI = 0;
        for(Aluno a : data.getAlunos()){
            if(a.getSiglaRamo().equals("DA"))
                totDA++;
            if(a.getSiglaRamo().equals("RAS"))
                totRAS++;
            if(a.getSiglaRamo().equals("SI"))
                totSI++;
        }
        //return (propostas.size() >= alunos.size()) && (pDA_SI >= (totDA + totSI)) && (pDA_RAS >= (totDA + totRAS)) && (pRAS_SI >= (totRAS + totSI)) && (pDA >= totDA) && (pRAS >= totRAS) && (pSI >= totSI);
        return true;
    }

}
