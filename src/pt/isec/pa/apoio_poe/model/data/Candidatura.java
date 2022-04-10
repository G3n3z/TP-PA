package pt.isec.pa.apoio_poe.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Candidatura {
    long numAluno;
    List<String> idProposta;

    public Candidatura(long numAluno, List<String> idProposta) {
        this.numAluno = numAluno;
        this.idProposta = idProposta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidatura cand = (Candidatura) o;
        return numAluno == cand.numAluno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numAluno);
    }
}
