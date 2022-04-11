package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;




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

    public Set<Proposta> getPropostas() {
        return propostas;
    }

    public Set<Candidatura> getCandidaturas() {
        return candidaturas;
    }

    public Aluno getAluno(long naluno){
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
    public HashSet<Proposta> getPropostaAPartirDeId(HashSet<Proposta> p, List<String> idProposta){
        for (Proposta proposta : propostas){
            for (String id : idProposta){
                if(proposta.getId().equals(id)){
                    p.add(proposta);
                }
            }
        }
        return p;
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

    public String getPropostasToString() {
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
    public List<String> getRamosDeProposta(String id){
        for(Proposta p : propostas){
            if(p.getId().equals(id)){
                return p.getRamos();
            }
        }
        return null;
    }



    //Verificar se existe alguma proposta com um certo id e numAluno
    public boolean propostaTemAluno(long numAluno, String idProposta){
        return propostas.stream().anyMatch(p -> p.getNumAluno() != null && p.getNumAluno() == numAluno && p.getId().equals(idProposta));
    }

    public String getCandidaturasToString() {
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

    public HashSet<Proposta> getAutoPropostas() {
        HashSet<Proposta> p = new HashSet<>();
        for (Proposta proposta : propostas){
            if(proposta instanceof Projeto_Estagio)
                p.add(proposta);
        }
        return p;
    }

    public HashSet<Proposta> getProjetos() {
        HashSet<Proposta> p = new HashSet<>();
        for (Proposta proposta : propostas){
            if(proposta instanceof Projeto)
                p.add(proposta);
        }
        return p;
    }
    public String getPropostasWithFiltersToString(int ...filters){
        StringBuilder sb = new StringBuilder();
        getPropostasWithFilters(filters).forEach( p -> sb.append(p).append("\n"));
        return sb.toString();
    }

    public  Set<Proposta> getPropostasWithFilters(int ...filters){
        Set<Proposta> propostas = new HashSet<>();
        for (int i : filters){
            switch (i){
                case 1 -> propostas.addAll(getAutoPropostas());
                case 2 -> propostas.addAll(getProjetos());
                case 3 -> propostas.addAll(getPropostasComCandidatura());
                case 4 -> propostas.addAll(getPropostasSemCandidatura());
            }
        }
        return propostas;
    }

    private HashSet<Proposta> getPropostasSemCandidatura() {
        HashSet<Proposta> propostasReturn = new HashSet<>();
        for (Proposta p : propostas){
            if(!(p instanceof Projeto_Estagio)) {
                for (Candidatura c : candidaturas) {
                    if (!c.containsPropostaById(p.getId())) {
                        propostasReturn.add(p);
                    }
                }
            }
        }
        return propostasReturn;
    }

    private HashSet<Proposta> getPropostasComCandidatura() {

        HashSet<Proposta> propostasReturn = new HashSet<>();
        for (Proposta p : propostas){
            for (Candidatura c : candidaturas){
                if(c.containsPropostaById(p.getId())) {
                    propostasReturn.add(p);
                }
            }
        }
        return propostasReturn;
    }
}
