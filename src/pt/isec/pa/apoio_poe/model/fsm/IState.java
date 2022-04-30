package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidArguments;

import java.io.IOException;

public interface IState {
    boolean avancarFase();
    boolean recuarFase();
    EnumState getState();
    boolean close();
    boolean gerirAlunos();
    boolean gerirDocentes();
    boolean gerirEstagios();
    boolean conflitoAtribuicaoCandidatura();
    boolean gerirOrientadores();
    void gestaoManualAtribuicoes();
    void obtencaoDadosOrientador();
    void obtencaoListaProposta();
    void obtencaoListaAlunos();
    void editarCandidaturas();
    void editarDocentes();
    void editarAlunos();
    void editarPropostas();
    boolean fechado();
    boolean addAluno(String file) throws CollectionBaseException;
    boolean importDocentes(String file) throws CollectionBaseException;
    boolean importPropostas(String file);
    boolean addCandidatura(String file) throws CollectionBaseException;

    void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();

    boolean changeName(String novo_nome, long nAluno);

    void removeAluno(long numero_de_aluno);

    void changeCursoAluno(String novo_curso, long nAluno);

    void changeRamoAluno(String novo_ramo, long nAluno);

    void changeClassAluno(double nova_classificaçao, long nAluno);

    void removeDocente(String numero_de_aluno);

    void changeNameDocente(String novo_nome, String email);
    void addPropostaACandidatura(long nAluno, String idProposta);

    void atribuicaoAutomaticaSemAtribuicoesDefinidas() throws ConflitoAtribuicaoAutomaticaException;

    boolean existConflict();

    boolean resolveConflito(long numAluno);

    void associacaoAutomaticaDeDocentesAPropostas();

    boolean exportarCSV(String file);

    void removeProposta(String id);

    void removePropostaACandidatura(String id, long naluno);

    boolean load() throws IOException, ClassNotFoundException;

    void begin();

    boolean save() throws IOException;

    void sair();

    boolean existFileBin();

    boolean removeAll();

    void changeTitulo(String id, String novo_titulo) throws InvalidArguments;

    void changeEntidade(String id, String nova_entidade) throws InvalidArguments;

    void addRamo(String id, String ramo) throws InvalidArguments;

    void removeRamo(String id, String ramo) throws InvalidArguments;

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
