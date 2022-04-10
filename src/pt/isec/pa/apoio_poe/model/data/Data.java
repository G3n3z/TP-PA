package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.function.Predicate;
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
        candidaturas = new HashSet<>();
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



    public Boolean addCandidatura(Candidatura candidatura) {
         if(!candidaturas.add(candidatura)){
             return false;
         }
         for(Aluno a : alunos){
             if(a.getNumeroAluno() == candidatura.getNumAluno())
                 a.addCandidatura(candidatura);
         }
         return true;
    }

    public boolean existsFieldsOfCandidatura(Candidatura candidatura) {

        if(alunos.stream().noneMatch(aluno -> {       //Se nenhum aluno corresponder ao numero de aluno da candidatura e se o ramo do aluno nao abrangir o ramo da candidatura
            return aluno.getNumeroAluno() == candidatura.getNumAluno() && candidatura.getIdProposta().stream().anyMatch(c -> c.equals(aluno.getSiglaRamo()));
        })){
            return false;
        }
        //Se os ids da proposta existem----
        for(String id : candidatura.getIdProposta()){
            if(propostas.stream().noneMatch(proposta -> !(proposta instanceof Projeto_Estagio) && id.equals(proposta.getId()))){
                return false;
            }
        }
        return true;
    }

    public String getCandidaturas() {
        StringBuilder sb = new StringBuilder();

        candidaturas.forEach(sb::append);
        return sb.toString();
    }

    public String obtencaoAlunosComAutoProposta(){ //Obtenção de listas de alunos: Com autoproposta.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.proposta instanceof Projeto_Estagio).forEach(sb::append);
        return sb.toString();
    }

    public String obtencaoAlunosComCandidatura(){  //Obtenção de listas de alunos: om candidatura já registada.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.candidatura != null).forEach(sb::append);
        return sb.toString();
    }

    public String obtencaoAlunosSemCandidatura(){ //  Obtenção de listas de alunos: Sem candidatura registada.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.candidatura == null).forEach(sb::append);
        return sb.toString();
    }

    public String getPropostasWithFilter(int ...filters) {
        HashSet<Proposta> aux =  new HashSet<>(propostas);
        for (int i = 0 ; i < filters.length; i++){
            switch (filters[i]){
                case 1 -> {
                    aux.removeIf(p -> p instanceof Projeto_Estagio);
                }
                case 2 -> {}
                case 3 -> {}
                case 4 -> {}
            }
        }
        return "";
    }


    public void atribuicaoAutomaticaEstagio_Proposta() {

        for (Proposta p : propostas){
            if(!(p instanceof Projeto_Estagio)){
                continue;
            }
            for(Aluno aluno : alunos){
                if(aluno.getNumeroAluno() == p.getNumAluno()){
                    aluno.setProposta(p);
                }
            }
        }
    }
}
