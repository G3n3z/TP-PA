package pt.isec.pa.apoio_poe.model.data;

import java.util.Objects;

public class Aluno extends Pessoa {

    private long numeroEstudante;
    private String siglaCurso;
    private String siglaRamo;
    private double classificacao;
    private boolean possibilidade;
    private Integer ordem = null;
    private Proposta propostaNaoConfirmada;
    private Proposta proposta;
    private Candidatura candidatura = null;

    private Aluno(long numeroEstudante){
        super("", "");
        this.numeroEstudante = numeroEstudante;
    }

    public Aluno(String email, String nome, long numeroEstudante, String siglaCurso, String siglaRamo, double classificacao, boolean possibilidade) {
        super(email, nome);
        this.numeroEstudante = numeroEstudante;
        this.siglaCurso = siglaCurso;
        this.siglaRamo = siglaRamo;
        this.classificacao = classificacao;
        this.possibilidade = possibilidade;
    }

    public static Aluno getDummyAluno(Aluno aluno){
        return new Aluno(aluno.getEmail(), aluno.getNome(), aluno.numeroEstudante, aluno.siglaCurso, aluno.siglaRamo, aluno.classificacao, aluno.possibilidade);
    }
    public static Aluno getDummyAluno(long naluno){
        return new Aluno(naluno);
    }
    /*

        ===================GETS=======================

     */

    public Integer getOrdem() {
        return ordem;
    }

    public boolean isPossibilidade() {
        return possibilidade;
    }

    public Proposta getPropostaNaoConfirmada() {
        return propostaNaoConfirmada;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public Candidatura getCandidatura() {
        return candidatura;
    }


    public String getSiglaCurso() {
        return siglaCurso;
    }

    public String getSiglaRamo() {
        return siglaRamo;
    }

    public long getNumeroAluno() {
        return numeroEstudante;
    }

    public double getClassificacao() {
        return classificacao;
    }
        /*

        ===================SETS=======================

     */

    public void setPropostaNaoConfirmada(Proposta p) {
        if (propostaNaoConfirmada == null)
            propostaNaoConfirmada = p;
    }

    public void setProposta(Proposta proposta) {
        if(this.proposta != null){
            return;
        }
        this.proposta = proposta;
        if(candidatura == null){
            ordem = 1;
            return;
        }
        ordem = candidatura.getIdProposta().indexOf(proposta.getId()) + 1;
    }

    public void setSiglaCurso(String siglaCurso) {
        this.siglaCurso = siglaCurso;
    }

    public void setSiglaRamo(String siglaRamo) {
        this.siglaRamo = siglaRamo;
    }

    public void setClassificacao(double classificacao) {
        this.classificacao = classificacao;
    }


    public void addCandidatura(Candidatura candidatura) {
        this.candidatura = candidatura;
    }
    public void addCandidatura(String candidatura) {
        this.candidatura.getIdProposta().add(candidatura);
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    public void removeProposta(){
        this.proposta = null;
    }

    /*

        ===================TOSTRING=======================

     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Numero de Estudante: ").append(numeroEstudante).append(" Nome: ").append(getNome()).append(" Email: ").append(getEmail());
        sb.append(" Curso: ").append(siglaCurso).append(" Ramo: ").append(siglaRamo)
                .append(" Classificação: ").append(classificacao).append("Possibilidade de fazer Estágio: ")
                .append(possibilidade).append("\n");
        if(proposta != null){
            sb.append("Proposta: ").append(proposta);
        }else if(propostaNaoConfirmada != null){
            sb.append("Proposta Nao Confirmada: ").append(propostaNaoConfirmada);
        }else if(candidatura != null){
            sb.append("Ainda sem proposta, mas com candidatura: ").append(candidatura);
        }else{
            sb.append("Ainda sem proposta e sem candidatura");
        }



        return sb.append("\n\n").toString();
    }

       /*

        ===================HASHCODE && EQUALS=======================

     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aluno aluno = (Aluno) o;

        return numeroEstudante == aluno.numeroEstudante;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroEstudante);
    }

    public boolean temPropostaConfirmada(){
        return proposta != null;
    }

    public boolean temPropostaNaoConfirmada(){
        return propostaNaoConfirmada != null;
    }


    public boolean temCandidatura() {
        return candidatura != null;
    }
}
