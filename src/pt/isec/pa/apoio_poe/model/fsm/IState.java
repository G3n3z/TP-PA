package pt.isec.pa.apoio_poe.model.fsm;

public interface IState {
    boolean avancarFase();
    boolean recuarFase();
    EnumState getState();
    boolean close();
    boolean gerirAlunos();
    boolean gerirDocentes();
    boolean gerirEstagios();
    boolean fechado();
    boolean addAluno(String file);
    boolean importDocentes(String file);

}
