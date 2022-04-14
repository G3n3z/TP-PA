package pt.isec.pa.apoio_poe.model.fsm;

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
    boolean fechado();
    boolean addAluno(String file);
    boolean importDocentes(String file);
    boolean importPropostas(String file);
    boolean addCandidatura(String file);

    void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno();

    void changeName(String novo_nome, long nAluno);

    void removeAluno(long numero_de_aluno);

    void changeCursoAluno(String novo_curso, long nAluno);

    void changeRamoAluno(String novo_ramo, long nAluno);

    void changeClassAluno(double nova_classificaçao, long nAluno);

    void removeDocente(String numero_de_aluno);

    void changeNameDocente(String novo_nome, String email);
    void addPropostaACandidatura(long nAluno, String idProposta);

    void atribuicaoAutomaticaSemAtribuicoesDefinidas();

    boolean existConflict();

    boolean resolveConflito(long numAluno);

    void associacaoAutomaticaDeDocentesAPropostas();

    boolean exportarCSV(String file);

    void removeProposta(String id);

    void removePropostaACandidatura(String id, long naluno);
}
