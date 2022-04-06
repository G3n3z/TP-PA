package pt.isec.pa.apoio_poe.model.fsm;

public enum EnumState {
    CONFIG_OPTIONS, GESTAO_CLIENTES, GESTAO_ESTAGIOS, GESTAO_DOCENTES, OPCOES_CANDIDATURA, ATRIBUICAOPROPOSTAS;

    IState createState(ApoioContext context){
        return switch (this){
            case CONFIG_OPTIONS -> new ConfigOptions(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS));
            case GESTAO_CLIENTES -> new GestaoClientes(context,  context.getBooleanState(EnumState.GESTAO_CLIENTES));
            case GESTAO_DOCENTES -> new GestaoDocentes(context,  context.getBooleanState(EnumState.GESTAO_DOCENTES));
            case GESTAO_ESTAGIOS -> new GestaoEstagios(context,  context.getBooleanState(EnumState.GESTAO_ESTAGIOS));
            case OPCOES_CANDIDATURA -> new OpcoesCandidatura(context,  context.getBooleanState(EnumState.OPCOES_CANDIDATURA));
            case ATRIBUICAOPROPOSTAS -> new AtribuicaoPropostas(context,  context.getBooleanState(EnumState.ATRIBUICAOPROPOSTAS));
        };
    }

}
