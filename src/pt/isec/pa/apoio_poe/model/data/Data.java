package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Data {
    Set<Aluno> alunos;
    Set<Docente> docentes;
    Set<Proposta> propostas;
    Set<Candidatura> candidaturas;

    public Data() {
        this.alunos = new HashSet<>();
        this.propostas = new HashSet<>();
        this.docentes = new HashSet<>();
    }


    public boolean addAluno(Aluno aluno) {

        if(docentes.stream().anyMatch(d -> d.getEmail().equals(aluno.getEmail()))){
            return false;
        }

        return alunos.add(aluno);

    }

    public String getAlunos() {
        StringBuilder sb = new StringBuilder();
        alunos.forEach(sb::append);
        return sb.toString();
    }

    public boolean addDocente(Docente docente) {
        if(alunos.stream().anyMatch(a -> a.getEmail().equals(docente.getEmail()))){
            return false;
        }

        return docentes.add(docente);
    }

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



    public void addCandidatura(Candidatura candidatura) {
        if(!candidaturas.add(candidatura)){
            return;
        }

        //Adicionar ao aluno TODO
    }

    public void existsFieldsOfCandidatura(Candidatura candidatura) {
        //Verificar dados das candidaturas TODO

        if( !alunos.stream().anyMatch(aluno -> {
            boolean b = aluno.getNumeroAluno() == candidatura.numAluno;
            return b;
        })){
            return;
        }

    }
}
