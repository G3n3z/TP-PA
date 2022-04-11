package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public enum EnumState {
    CONFIG_OPTIONS, GESTAO_CLIENTES, GESTAO_PROPOSTAS, GESTAO_DOCENTES, OPCOES_CANDIDATURA, ATRIBUICAOPROPOSTAS;

    IState createState(ApoioContext context, Data data){
        return switch (this){
            case CONFIG_OPTIONS -> new ConfigOptions(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS),data);
            case GESTAO_CLIENTES -> new GestaoClientes(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS),data);
            case GESTAO_DOCENTES -> new GestaoDocentes(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS), data);
            case GESTAO_PROPOSTAS -> new GestaoPropostas(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS), data);
            case OPCOES_CANDIDATURA -> new OpcoesCandidatura(context,  context.getBooleanState(EnumState.OPCOES_CANDIDATURA), data);
            case ATRIBUICAOPROPOSTAS -> new AtribuicaoPropostas(context,  context.getBooleanState(EnumState.ATRIBUICAOPROPOSTAS),data);
        };
    }

}
