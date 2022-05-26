package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

import java.io.IOException;

public interface IState {
    boolean avancarFase();
    boolean recuarFase();
    EnumState getState();
    ErrorCode close();
    boolean gerirAlunos();
    boolean gerirDocentes();
    boolean gerirEstagios();
    boolean conflitoAtribuicaoCandidatura();
    boolean gerirOrientadores();
    void gestaoManualAtribuicoes();
    void obtencaoDadosOrientador();
    void obtencaoListaProposta();
    void obtencaoListaAlunos();

    boolean fechado();
    boolean addAluno(String file) throws CollectionBaseException;
    boolean importDocentes(String file) throws CollectionBaseException;
    boolean importPropostas(String file) throws CollectionBaseException;
    boolean addCandidatura(String file) throws CollectionBaseException;

    void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();

    ErrorCode changeName(String novo_nome, long nAluno);

    ErrorCode removeAluno(long numero_de_aluno);

    ErrorCode changeCursoAluno(String novo_curso, long nAluno);

    ErrorCode changeRamoAluno(String novo_ramo, long nAluno);

    ErrorCode changeClassAluno(double nova_classificaçao, long nAluno);

    ErrorCode changePossibilidadeAluno(long nAluno);

    ErrorCode removeDocente(String numero_de_aluno);

    ErrorCode changeNameDocente(String novo_nome, String email);
    ErrorCode addPropostaACandidatura(long nAluno, String idProposta);

    void atribuicaoAutomaticaSemAtribuicoesDefinidas() throws ConflitoAtribuicaoAutomaticaException;

    boolean existConflict();

    ErrorCode resolveConflito(long numAluno);

    void associacaoAutomaticaDeDocentesAPropostas();

    ErrorCode exportarCSV(String file);

    ErrorCode removeProposta(String id);

    ErrorCode removePropostaACandidatura(String id, long naluno);

    boolean load() throws IOException, ClassNotFoundException;

    void begin();

    boolean save() throws IOException;

    void sair();

    boolean existFileBin();

    boolean removeAll();

    ErrorCode changeTitulo(String id, String novo_titulo);

    ErrorCode changeEntidade(String id, String nova_entidade) ;

    ErrorCode addRamo(String id, String ramo) ;

    ErrorCode removeRamo(String id, String ramo) ;

    String getAlunosToString();

    String getDocentesToString();

    String getPropostasToString();

    String getPropostasWithFiltersToString(int[] filters);

    String obtencaoAlunosComAutoProposta();

    String obtencaoAlunosComCandidatura();

    String obtencaoAlunosSemCandidatura();

    String obtencaoAlunosComAutoPropostaAtribuida();

    String getTodosAlunosComPropostaAtribuida();

    String obtencaoAlunosSemProposta();

    String getPropostasWithFiltersToStringAtribuicao(int[] filters);

    String getAlunosComPropostaEOrientador();

    String getAlunosComPropostaESemOrientador();

    String getEstatisticasPorDocente();

    String obtencaoAlunosSemPropostaComCandidatura();

    String getPropostasDisponiveis();

    String getPropostasAtribuidasToString();

    String getConflitoToString();


}
