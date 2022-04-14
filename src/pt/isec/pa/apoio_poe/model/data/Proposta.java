package pt.isec.pa.apoio_poe.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proposta {
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
    public Proposta(String id, String tipo, String titulo, long numAluno) {
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

    public Proposta(String id, String tipo, List<String> ramos, String titulo, long numAluno) {
        this.id = id;
        this.tipo = tipo;
        this.ramos = new ArrayList<>(ramos);
        this.titulo = titulo;
        this.numAluno = numAluno;
    }

    public static Proposta getDummy(String proposta) {
        return new Proposta(proposta);
    }

    public boolean isAtribuida() {
        return atribuida;
    }

    public void setAtribuida(boolean atribuida) {
        this.atribuida = atribuida;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", tipo='" + tipo + '\'' +
                ", ramos=" + ramos +
                ", titulo='" + titulo + '\'' +
                ", numAluno=" + numAluno;
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

    public List<String> getRamos() {
        if(ramos == null)
            return null;
        return new ArrayList<>(ramos);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proposta))
            return false;
        Proposta proposta = (Proposta) o;
        return getId().equals(proposta.getId());
    }

    public void setNumAluno(long numAluno){
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
}
