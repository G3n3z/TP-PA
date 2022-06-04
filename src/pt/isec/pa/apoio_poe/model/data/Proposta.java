package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proposta implements Serializable {
    private String id;
    private String tipo;
    private List<String> ramos;
    private String titulo;
    private Long numAluno = null;
    private boolean atribuida = false;
    Docente proponente;
    Docente orientador;

    private Proposta(String id){
        this.id = id;
    }
    public Proposta(String id, String tipo, String titulo, Long numAluno) {
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.numAluno = numAluno;
    }

    public Proposta(String id, String tipo, List<String> ramos, String titulo) {
        this.id = id;
        this.tipo = tipo;
        this.ramos = new ArrayList<>(ramos);
        this.titulo = titulo;
    }

    public Proposta(String id, String tipo, List<String> ramos, String titulo, Long numAluno) {
        this.id = id;
        this.tipo = tipo;
        this.ramos = new ArrayList<>(ramos);
        this.titulo = titulo;
        this.numAluno = numAluno;
    }

    public static Proposta getDummy(String proposta) {
        return new Proposta(proposta);
    }

    public static Proposta factory(String tipo, String id, List<String> ramos, String titulo, String docente, String entidade, Long nAluno) {
        return switch (tipo){
            case "T1" -> new Estagio(id, tipo, ramos, titulo, entidade, nAluno);
            case "T2" -> new Projeto(id, tipo,ramos,titulo, docente, nAluno);
            case "T3" -> new Projeto_Estagio(id, tipo, titulo, nAluno);
            default -> null;
        };
    }

    public boolean isAtribuida() {
        return atribuida;
    }

    public void setAtribuida(boolean atribuida) {
        this.atribuida = atribuida;
    }

    @Override
    public String toString() {
        return "Id: " + id +
                "; Tipo: " + tipo +
                "; Ramos: " + (ramos == null ? "n/a" : ramos) +
                "; Titulo: " + titulo +
                "; Aluno pré-associado: " + (numAluno == null ? "n/a" :  numAluno);
    }
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getNumAluno() {
        return numAluno;
    }
    public void addRamo(String newRamo){
        if (ramos == null){
            ramos = new ArrayList<>();
        }
        ramos.add(newRamo);
    }
    public List<String> getRamos() {
        if(ramos == null)
            return null;
        return new ArrayList<>(ramos);
    }
    public String getRamosToExport(){
        if(ramos == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ramos.size(); i++){
            sb.append(ramos.get(i));
            if (i != ramos.size() - 1)
                sb.append("|");
        }
        return sb.toString();
    }
    public String getTitulo() {
        return titulo;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proposta proposta))
            return false;
        return getId().equals(proposta.getId());
    }

    public void setNumAluno(Long numAluno){
        this.numAluno = numAluno;
    }

    public void setDocenteOrientador(Docente d) {
        orientador = d;
    }

    public void setDocenteProponente(Docente d) {
        proponente = d;
    }

    public boolean temDocenteOrientador(){
        return orientador != null;
    }
    public boolean temDocenteProponente(){
        return proponente != null;
    }

    public void setDocenteOrientadorDocenteProponente() {
        orientador = proponente;
    }

    public String getEmailOrientador() {
        if(orientador != null){
            return orientador.getEmail();
        }
        return "";
    }

    public void removeOrientador() {
        orientador = null;
    }

    public Docente getOrientador() {
        return orientador;
    }
    public Docente getProponente() {
        return proponente;
    }

    public Proposta getClone() {
        return null;
    }

    public Object[] exportProposta() {
        return null;
    }

    public void setTitulo(String novo_titulo) {
        titulo = novo_titulo;
    }

    public void removeRamo(String ramo) {
        ramos.remove(ramo);
    }

    public String getEmailDocente(){
        return "";
    }

    public String getEntidade() {
        return "";
    }

    public void setRamos(List<String> ramos) {
        this.ramos = ramos;
    }
}
