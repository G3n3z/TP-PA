package pt.isec.pa.apoio_poe.model.data;

import java.util.List;

public class Proposta {
    private String id;
    private List<String> ramos;
    private String titulo;
    private long numAluno;

    public String getId() {
        return id;
    }

    public long getNumAluno() {
        return numAluno;
    }
}
