package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Candidatura implements Serializable {
    private long numAluno;
     private List<String> idProposta;

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

    public long getNumAluno() {
        return numAluno;
    }

    public List<String> getIdProposta() {
        return idProposta;
    }

    @Override
    public String toString() {
        return "Candidatura{" +
                "numAluno=" + numAluno +
                ", idProposta=" + idProposta +
                '}';
    }

    public Boolean containsPropostaById(String id){
        return idProposta.contains(id);
    }

    public void removeProposta(String id) {
        idProposta.remove(id);
    }

    public Candidatura getClone() {
        return new Candidatura(numAluno,idProposta);
    }

    private String getIdsToExport(){

        if(idProposta == null)
                return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < idProposta.size(); i++){
            sb.append(idProposta.get(i));
            if (i != idProposta.size() - 1)
                sb.append(",");
            }
        return sb.toString();

    }

    public Object[] getExportCandidatura() {
        return new Object[]{numAluno,getIdsToExport()};
    }
}
