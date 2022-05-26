package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public enum EnumState {
    CONFLITO_ATRIBUICAO_CANDIDATURA ,CONFIG_OPTIONS, GESTAO_ALUNOS, GESTAO_PROPOSTAS, GESTAO_DOCENTES, OPCOES_CANDIDATURA, ATRIBUICAO_PROPOSTAS, ATRIBUICAO_MANUAL_PROPOSTAS,
    ATRIBUICAO_ORIENTADORES, GESTAO_ORIENTADORES, CONSULTA, LOAD_STATE, SAIR;

    IState createState(ApoioContext context, Data data){
        return switch (this){
            case CONFLITO_ATRIBUICAO_CANDIDATURA -> new ConflitoAtribuicaoCandidatura(context,false, data);
            case CONFIG_OPTIONS -> new ConfigOptions(context,  data.getBooleanState(EnumState.CONFIG_OPTIONS),data);
            case GESTAO_ALUNOS -> new GestaoAlunos(context,  data.getBooleanState(EnumState.CONFIG_OPTIONS),data);
            case GESTAO_DOCENTES -> new GestaoDocentes(context,  data.getBooleanState(EnumState.CONFIG_OPTIONS), data);
            case GESTAO_PROPOSTAS -> new GestaoPropostas(context,  data.getBooleanState(EnumState.CONFIG_OPTIONS), data);
            case OPCOES_CANDIDATURA -> new OpcoesCandidatura(context,  data.getBooleanState(EnumState.OPCOES_CANDIDATURA), data);
            case ATRIBUICAO_PROPOSTAS -> new AtribuicaoPropostas(context,  data.getBooleanState(EnumState.ATRIBUICAO_PROPOSTAS),data);
            case ATRIBUICAO_MANUAL_PROPOSTAS -> new AtribuicaoManualPropostas(context, data.getBooleanState(EnumState.ATRIBUICAO_MANUAL_PROPOSTAS),data);
            case ATRIBUICAO_ORIENTADORES -> new AtribuicaoOrientadores(context,  data.getBooleanState(EnumState.ATRIBUICAO_ORIENTADORES),data);
            case GESTAO_ORIENTADORES -> new GestaoOrientadores(context, data.getBooleanState(EnumState.GESTAO_ORIENTADORES),data);
            case CONSULTA -> new Consulta(context, data.getBooleanState(EnumState.CONSULTA),data);
            case LOAD_STATE -> new LoadState(context, data.getBooleanState(EnumState.LOAD_STATE),data);
            case SAIR -> new Sair(context, false,data);
        };
    }

}
