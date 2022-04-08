package pt.isec.pa.apoio_poe.model.data;

import java.util.HashSet;
import java.util.Set;

public class Data {
    Set<Aluno> alunos;
    Set<Docente> docentes;
    Set<Proposta> propostas;

    public Data() {
        this.alunos = new HashSet<>();
        this.propostas = new HashSet<>();
    }


    public boolean addAluno(Aluno aluno) {
        return alunos.add(aluno);
    }

    public String getAlunos() {
        StringBuilder sb = new StringBuilder();
        alunos.forEach(sb::append);
        return sb.toString();
    }
}
