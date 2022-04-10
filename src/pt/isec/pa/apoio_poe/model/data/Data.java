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

    private Aluno getAluno(long naluno){
        for(Aluno a : alunos){
            if(a.getNumeroAluno() == naluno)
                return a;
        }
        return null;
    }
    private Docente getDocente(String email){
        for(Docente d : docentes){
            if(d.getEmail().equalsIgnoreCase(email))
                return d;
        }
        return null;
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

    public boolean addProposta(Proposta proposta) {
        return propostas.add(proposta);
    }


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
            if(propostaTemAluno(candidatura.getNumAluno(), id)){
                return false;
            }
        }
        return true;
    }

    //Verificar se existe alguma proposta com um certo id e numAluno
    public boolean propostaTemAluno(long numAluno, String idProposta){
        return propostas.stream().anyMatch(p -> p.getNumAluno() != null && p.getNumAluno() == numAluno && p.getId().equals(idProposta));
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


    public void changeNameAluno(long naluno,String novo_nome) {
        Aluno a = getAluno(naluno);
        if(a == null){
            //Por mensagem no logo //TODO
            return;
        }
        a.setNome(novo_nome);
    }

    public boolean removeAluno(long numero_de_aluno) {
        return alunos.removeIf(a -> a.getNumeroAluno() == numero_de_aluno);
    }

    public boolean changeCursoAluno(String novo_curso, long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            //Por mensagem no logo //TODO
            return false;
        }
        a.setSiglaCurso(novo_curso);
        return true;
    }

    public boolean changeRamoAluno(String novo_ramo, long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            //Por mensagem no logo //TODO
            return false;
        }
        a.setSiglaCurso(novo_ramo);
        return true;
    }

    public boolean changeClassAluno(double nova_classificaçao, long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            //Por mensagem no logo //TODO
            return false;
        }
        a.setClassificacao(nova_classificaçao);
        return true;
    }

    public boolean removeDocente(String email) {
        return docentes.remove(Docente.getDummyDocente(email));
    }

    public boolean changeNameDocente(String novo_nome, String email) {
        Docente d = getDocente(email);
        if(d == null){
            return false;
        }
        d.setNome(novo_nome);
        return true;
    }
}
