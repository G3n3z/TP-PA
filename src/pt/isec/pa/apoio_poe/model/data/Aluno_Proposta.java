package pt.isec.pa.apoio_poe.model.data;

public class Aluno_Proposta {
    Aluno aluno;
    Proposta proposta;
    int ordem;

    public Aluno_Proposta(Aluno aluno, Proposta proposta, int ordem) {
        this.aluno = aluno;
        this.proposta = proposta;
        this.ordem = ordem;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
}
