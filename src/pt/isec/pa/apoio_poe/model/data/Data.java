package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.data.Comparator.AlunoComparator;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.utils.Constantes;

import java.util.*;


public class Data {
    Set<Aluno> alunos;
    Set<Docente> docentes;
    Set<Proposta> propostas;
    Set<Candidatura> candidaturas;
    Map<Proposta, ArrayList<Aluno>> proposta_aluno;
    public Data() {
        this.alunos = new HashSet<>();
        this.propostas = new HashSet<>();
        this.docentes = new HashSet<>();
        candidaturas = new HashSet<>();
        proposta_aluno = new HashMap<>();
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
    public boolean existeDocenteComEmail(String email){
        return docentes.stream().anyMatch(d -> d.getEmail().equals(email));
    }
    public boolean existeCursos(String siglaCurso){
        return Constantes.getCursos().stream().anyMatch(curso -> curso.equals(siglaCurso));
    }
    public boolean existeRamos(String ramo) {
        return Constantes.getRamos().stream().anyMatch(ramos -> ramos.equals(ramo));
    }

    public boolean addAluno(Aluno aluno) {
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

    public void atribuipropostaNaoConfirmada(Proposta proposta, long numAluno) {
        for(Aluno a : alunos) {
            if (a.getNumeroEstudante() == numAluno)
                a.setPropostaNaoConfirmada(proposta);
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
        alunos.stream().filter(a -> a.getPropostaNaoConfirmada() instanceof Projeto_Estagio).forEach(sb::append);
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


    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {
        Aluno aluno;
        for (Proposta p : propostas){
            if(p.getNumAluno() == null){
                continue;
            }
            aluno = getAluno(p.getNumAluno());
            if(aluno != null){
                aluno.setProposta(p);
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

    public boolean existeAlunoComEmail(String email) {
        return alunos.stream().anyMatch(a -> a.getEmail().equals(email));
    }

    public boolean existePropostaSemAluno(String proposta){
        HashSet<Proposta> p = getPropostasSemAluno();
        return p.contains(Proposta.getDummy(proposta));
    }

    private HashSet<Proposta> getPropostasSemAluno() {
        HashSet<Proposta> propostaReturn = new HashSet<>();
        for (Proposta p : propostas){
            if(p.getNumAluno() == null){
                propostaReturn.add(p);
            }
        }
        return propostaReturn;
    }


    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() {
        List<Aluno> al = new ArrayList<>();
        List<Aluno> alunosComMesmaMedia;
        for(Aluno a : alunos){
            if(!a.temPropostaNaoConfirmada() && !a.temPropostaConfirmada() && a.temCandidatura()){
                al.add(a);
            }
        }
        al.sort(new AlunoComparator());
        //Pega-se na media de um aluno e obtem se todos os alunos com a mesma media
        for(Aluno a : al){
            alunosComMesmaMedia = obtemAlunosComMedia(a.getClassificacao(), al);
            atribuiPropostaAAlunosComMesmaMedia(alunosComMesmaMedia);
            alunosComMesmaMedia.clear();
            //Limpar os alunos ja atribuidos
        }

        al.forEach(System.out::println);
    }

    private void atribuiPropostaAAlunosComMesmaMedia(List<Aluno> alunosComMesmaMedia) {
        HashSet<Proposta> proposta = new HashSet<>();
        ConflitoAtribuicaoAutomaticaException e = null;
        int i = 1; boolean sair = true;
        ciclo: for (Aluno a : alunosComMesmaMedia){
            proposta = getPropostaAPartirDeId(proposta, a.getCandidatura().getIdProposta());
            for(Proposta p : proposta){
                if(!p.isAtribuida() ){//Se a proposta está atribuida
                    if(p instanceof Estagio && !a.isPossibilidade()){
                        continue;
                    }
                    a.setOrdem(i);
                    if(proposta_aluno.containsKey(p)){ //Se já contem a proposta
                        if(proposta_aluno.get(p).size() == 1){
                            sair = !sair;
                            if(sair){
                                proposta_aluno.remove(p);
                                break ciclo;
                            }
                        }
                        proposta_aluno.get(p).add(a);
                    }
                    else{
                        proposta_aluno.put(p, new ArrayList<>());
                        proposta_aluno.get(p).add(a);
                        break;
                    }
                }
                i++;
            }
        }
        i = 0;
        for(Map.Entry<Proposta, ArrayList<Aluno>> set : proposta_aluno.entrySet()){
            if(set.getValue().size() == 1){
                set.getValue().get(0).setProposta(set.getKey());
                set.getKey().setAtribuida(true);
                proposta_aluno.remove(set.getKey());
            }
            else{
                i++;
                if(i == 1){
                    e = new ConflitoAtribuicaoAutomaticaException();
                }
            }
        }
        if(e != null){
            throw e;
        }

    }

    private List<Aluno> obtemAlunosComMedia(double classificacao, List<Aluno> al) {
        List<Aluno>alunosComMesmaMedia = new ArrayList<>();
        for(Aluno a : al){
            if(a.getClassificacao() == classificacao){
                alunosComMesmaMedia.add(a);
            }else {
                break;
            }
        }
        return alunosComMesmaMedia;
    }

    public String getConflitoToString() {
        StringBuilder sb = new StringBuilder();
        proposta_aluno.forEach((k,v) -> {
            sb.append("Proposta com id: ").append(k.getId()).append(" com conflito\n");
            sb.append("Lista de alunos com sobreposição\n");
            v.forEach(aluno -> {
                sb.append("Aluno: ").append(aluno.getNumeroAluno()).append(" email: ").append(aluno.getEmail()).append("\n");
            } );
        });
        return sb.toString();
    }

    public boolean existConflit() {
        return proposta_aluno.size() > 0;
    }

    public String consultaAlunosConflito() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alunos: \n");
        proposta_aluno.forEach((k,v) -> {
            v.forEach(sb::append);
        });

        return sb.toString();
    }

    public String consultaPropostaConflito() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proposta");
        proposta_aluno.forEach((k,v) ->{
            sb.append(k);
        });
        return sb.toString();
    }

    public Map<Proposta, ArrayList<Aluno>> getProposta_aluno() {
        return proposta_aluno;
    }
}
