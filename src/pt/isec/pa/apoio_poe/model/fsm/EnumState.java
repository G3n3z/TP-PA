package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Data;

public enum EnumState {
    CONFLITO_ATRIBUICAO_CANDIDATURA ,CONFIG_OPTIONS, GESTAO_ALUNOS, GESTAO_PROPOSTAS, GESTAO_DOCENTES, OPCOES_CANDIDATURA, ATRIBUICAO_PROPOSTAS, ATRIBUICAO_ORIENTADORES
    ,GESTAO_ORIENTADORES, CONSULTA;

    IState createState(ApoioContext context, Data data){
        return switch (this){
            case CONFLITO_ATRIBUICAO_CANDIDATURA -> new ConflitoAtribuicaoCandidatura(context,false, data);
            case CONFIG_OPTIONS -> new ConfigOptions(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS),data);
            case GESTAO_ALUNOS -> new GestaoAlunos(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS),data);
            case GESTAO_DOCENTES -> new GestaoDocentes(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS), data);
            case GESTAO_PROPOSTAS -> new GestaoPropostas(context,  context.getBooleanState(EnumState.CONFIG_OPTIONS), data);
            case OPCOES_CANDIDATURA -> new OpcoesCandidatura(context,  context.getBooleanState(EnumState.OPCOES_CANDIDATURA), data);
            case ATRIBUICAO_PROPOSTAS -> new AtribuicaoPropostas(context,  context.getBooleanState(EnumState.ATRIBUICAO_PROPOSTAS),data);
            case ATRIBUICAO_ORIENTADORES -> new AtribuicaoOrientadores(context,  context.getBooleanState(EnumState.ATRIBUICAO_ORIENTADORES),data);
            case GESTAO_ORIENTADORES -> new GestaoOrientadores(context, context.getBooleanState(EnumState.GESTAO_ORIENTADORES),data);
            case CONSULTA -> new Consulta(context, context.getBooleanState(EnumState.CONSULTA),data);
        };
    }

}
