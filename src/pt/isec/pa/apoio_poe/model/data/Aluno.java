package pt.isec.pa.apoio_poe.model.data;

public class Aluno extends Pessoa{

    long numeroEstudante;
    String siglaCurso;
    String siglaRamo;
    double classificacao;
    boolean possibilidade;

    public Aluno(String email, String nome, long numeroEstudante, String siglaCurso, String siglaRamo, double classificacao, boolean possibilidade) {
        super(email, nome);
        this.numeroEstudante = numeroEstudante;
        this.siglaCurso = siglaCurso;
        this.siglaRamo = siglaRamo;
        this.classificacao = classificacao;
        this.possibilidade = possibilidade;
    }


}
