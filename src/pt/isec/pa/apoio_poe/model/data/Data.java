package pt.isec.pa.apoio_poe.model.data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Data {
    Set<Aluno> alunos;
    Set<Docente> docentes;
    Set<Proposta> propostas;

    public Data() {
        this.alunos = new HashSet<>();
        this.propostas = new HashSet<>();
        this.docentes = new HashSet<>();
    }


    public boolean addAluno(Aluno aluno) {

        Stream<Docente> aux = docentes.stream().filter(d -> d.getEmail().equals(aluno.getEmail()));
        if(aux.count()!= 0){
            return false;
        }
        return alunos.add(aluno);

    }

    public String getAlunos() {
        StringBuilder sb = new StringBuilder();
        alunos.forEach(sb::append);
        return sb.toString();
    }

    public boolean addDocente(Docente docente) { return docentes.add(docente); }

    public String getDocentes() {
        StringBuilder sb = new StringBuilder();
        docentes.forEach(sb::append);
        return sb.toString();
    }

    public boolean addProposta(Proposta proposta) { return propostas.add(proposta); }

    public boolean verificaDocente(String docente) {
        Docente dummy = Docente.getDummyDocente(docente);
        return docentes.contains(dummy);
    }

    public boolean verificaAluno(long numAluno) {
        for(Aluno a : alunos) {
            if (a.getNumeroEstudante() == numAluno)
                return true;
        }
        return false;
    }

    public void atribuiproposta(Proposta proposta, long numAluno) {
        for(Aluno a : alunos) {
            if (a.getNumeroEstudante() == numAluno)
                a.setProposta(proposta);
        }
    }

    public String getPropostas() {
        StringBuilder sb = new StringBuilder();
        propostas.forEach(sb::append);
        return sb.toString();
    }
}
