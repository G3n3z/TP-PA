package pt.isec.pa.apoio_poe.model.data;

import java.util.Objects;

public class Aluno extends Pessoa{

    long numeroEstudante;
    String siglaCurso;
    String siglaRamo;
    double classificacao;
    boolean possibilidade;
    Proposta proposta;
    Candidatura candidatura;

    public Aluno(String email, String nome, long numeroEstudante, String siglaCurso, String siglaRamo, double classificacao, boolean possibilidade) {
        super(email, nome);
        this.numeroEstudante = numeroEstudante;
        this.siglaCurso = siglaCurso;
        this.siglaRamo = siglaRamo;
        this.classificacao = classificacao;
        this.possibilidade = possibilidade;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                " nome: " + getNome() +
                " email: " + getEmail() +
                " numeroEstudante=" + numeroEstudante +
                ", siglaCurso='" + siglaCurso + '\'' +
                ", siglaRamo='" + siglaRamo + '\'' +
                ", classificacao=" + classificacao +
                ", possibilidade=" + possibilidade +
                ", proposta=" + proposta +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aluno aluno = (Aluno) o;

        return numeroEstudante == aluno.numeroEstudante && getEmail().equals(aluno.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroEstudante, getEmail());
    }

    public String getSiglaRamo() {
        return siglaRamo;
    }


    public long getNumeroAluno() {
        return numeroEstudante;
    }

    public void addCandidatura(Candidatura candidatura) {
        this.candidatura = candidatura;
    }

    public void setProposta(Proposta p) {
        if(proposta == null)
            proposta = p;
    }
}
