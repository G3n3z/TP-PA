package pt.isec.pa.apoio_poe.model.data;

import java.util.ArrayList;
import java.util.List;

public class Proposta {
    private String id;
    private String tipo;
    private List<String> ramos;
    private String titulo;
    private long numAluno; //mudar para derivadas que usam?? //TODO

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

    public long getNumAluno() {
        return numAluno;
    }
}
