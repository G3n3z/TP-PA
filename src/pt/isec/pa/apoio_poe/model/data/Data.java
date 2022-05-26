package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.model.fsm.EnumState;
import pt.isec.pa.apoio_poe.utils.Constantes;

import java.io.Serializable;
import java.util.*;


public class Data implements Serializable {
    static final long serialVersionUID = 2L;
    private final String ficheiroBin = "load.dat";
    private EnumState lastState;
    Set<Aluno> alunos;
    Set<Docente> docentes;
    Set<Proposta> propostas;
    Set<Candidatura> candidaturas;
    Map<Proposta, ArrayList<Aluno>> proposta_aluno;
    Map<EnumState, Boolean> closed;
    public Data() {
        this.alunos = new HashSet<>();
        this.propostas = new HashSet<>();
        this.docentes = new HashSet<>();
        candidaturas = new HashSet<>();
        proposta_aluno = new HashMap<>();
        closed = new HashMap<>();
    }
    public boolean getBooleanState(EnumState enumstate){
        Boolean state = closed.get(enumstate);
        if(state == null){
            closed.put(enumstate, false);
            return false;
        }
        else {
            return state;
        }
    }

    public EnumState getLastState() {
        return lastState;
    }

    public void setLastState(EnumState lastState) {
        this.lastState = lastState;
    }

    public void closeState(EnumState state){
        closed.put(state,true);
    }



    public String getFicheiroBin() {
        return ficheiroBin;
    }

    public Aluno getAluno(long naluno){
        for(Aluno a : alunos){
            if(a.getNumeroAluno() == naluno)
                return a;
        }
        return null;
    }

    public Aluno getAlunoClone(long naluno){
        for(Aluno a : alunos){
            if(a.getNumeroAluno() == naluno)
                return a.getClone();
        }
        return null;
    }

    public List<Docente> getDocentes() {
        return new ArrayList<>(docentes);
    }
    public List<Aluno> getAlunos(){
        List<Aluno> al = new ArrayList<>();
        al.addAll(alunos);
        return al;
    }
    public List<Docente> getDocente(){
        List<Docente> dc = new ArrayList<>();
        for (Docente d : docentes){
            dc.add(d);
        }
        return dc;
    }
    public List<Proposta> getProposta(){
        List<Proposta> pr = new ArrayList<>();
        for (Proposta p : propostas){
            pr.add(p);
        }
        return pr;
    }
    public Docente getDocente(String email){
        for(Docente d : docentes){
            if(d.getEmail().equalsIgnoreCase(email))
                return d;
        }
        return null;
    }
    public List<Candidatura> getCandidaturas(){
        List<Candidatura> cand = new ArrayList<>();
        for(Candidatura c : candidaturas){
            cand.add(c.getClone());
        }
        return cand;
    }

    public List<Proposta> getPropostasAPartirDeId(List<Proposta> p, List<String> idProposta){
        for (String id : idProposta){
            for (Proposta proposta : propostas){
                if(proposta.getId().equals(id)){
                    p.add(proposta);
                }
            }
        }
        return p;
    }

    public boolean existeDocenteComEmail(String email){
        return docentes.stream().anyMatch(d -> d.getEmail().equalsIgnoreCase(email));
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

    public String getAlunosToString() {
        StringBuilder sb = new StringBuilder();
        alunos.forEach(sb::append);
        return sb.toString();
    }

    public boolean addDocente(Docente docente) {
        return docentes.add(docente);
    }

    public String getDocentesToString() {
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

    public boolean verificaNumAluno(long numAluno) {
        for(Aluno a : alunos) {
            if (a.getNumeroAluno() == numAluno)
                return true;
        }
        return false;
    }

    public void atribuipropostaNaoConfirmada(Proposta proposta, long numAluno) {
        for(Aluno a : alunos) {
            if (a.getNumeroAluno() == numAluno) {
                a.setPropostaNaoConfirmada(proposta);
                break;
            }
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

    //verificar se a sigla do aluno corresponde a alguma das listadas na proposta
    public boolean verificaRamoAluno(long numAluno, List<String> ramos){
        return alunos.stream().anyMatch(p -> p.getNumeroAluno() == numAluno && ramos.contains(p.getSiglaRamo()));
    }

    //verificar se o aluno tem possibilidade de aceder a estagios alem de projetos //verificar a utilidade
    public boolean verificaPossibilidade(long numAluno){
        return alunos.stream().anyMatch(p ->p.getNumeroAluno() == numAluno && p.isPossibilidade());
    }

    //verificar se o aluno ja contem um proposta atribuida
    public boolean verificaJaAtribuido(long numAluno){
        return alunos.stream().anyMatch(p ->p.getNumeroAluno() == numAluno && (p.temPropostaNaoConfirmada() || p.temPropostaConfirmada()));
    }

    public String getCandidaturasToString() {
        StringBuilder sb = new StringBuilder();

        candidaturas.forEach( candidatura -> sb.append(candidatura).append('\n'));
        return sb.toString();
    }

    public boolean changeNameAluno(long naluno,String novo_nome) {
        Aluno a = getAluno(naluno);
        if(a == null){
            return false;
        }
        a.setNome(novo_nome);
        return true;
    }

    public void removeAluno(Aluno a) {
        if(a == null){
            return;
        }
        if(a.temCandidatura()){
            candidaturas.remove(a.getCandidatura());
        }
        if (a.temPropostaNaoConfirmada()){
            if(a.getPropostaNaoConfirmada() instanceof Projeto_Estagio){
                propostas.remove(a.getPropostaNaoConfirmada());
            }
            else {
                a.getPropostaNaoConfirmada().setAtribuida(false);
                a.getPropostaNaoConfirmada().setNumAluno(null);
            }
        }

        if(a.temPropostaConfirmada()){
            if(a.getProposta() instanceof Projeto_Estagio){
                propostas.remove(a.getProposta());
            }
            else {
                a.getProposta().setAtribuida(false);
                a.getProposta().setNumAluno(null);
            }
        }
        alunos.remove(a);

    }

    public boolean changeCursoAluno(String novo_curso, long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            return false;
        }

        a.setSiglaCurso(novo_curso);
        return true;
    }

    public boolean changeRamoAluno(String novo_ramo, long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            return false;
        }
        a.setSiglaRamo(novo_ramo);
        return true;
    }

    public boolean changeClassAluno(double nova_classificacao, long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            return false;
        }
        a.setClassificacao(nova_classificacao);
        return true;
    }

    public boolean changePossibilidadeAluno(long nAluno) {
        Aluno a = getAluno(nAluno);
        if(a == null){
            return false;
        }
        a.setPossibilidade();
        return true;
    }

    public boolean removeDocente(Docente d) {

        if(d == null){
            return false;
        }
        for (Proposta p : propostas){
            if(p instanceof Projeto projeto && projeto.getEmailDocente() != null){
                if(projeto.getEmailDocente().equalsIgnoreCase(d.getEmail())){
                    projeto.setEmailDocente(null);
                }
            }
            if (p.getOrientador() != null && p.getOrientador().getEmail().equals(d.getEmail())){
                p.setDocenteOrientador(null);
            }
            if(p.getProponente() != null && p.getProponente().getEmail().equals(d.getEmail())){
                p.setDocenteProponente(null);
            }
        }
        return docentes.remove(d);
    }

    public boolean changeNameDocente(String novo_nome, String email) {
        Docente d = getDocente(email);
        if(d == null){
            return false;
        }
        d.setNome(novo_nome);
        return true;
    }

    public void removeProposta(String id){
        if(!propostas.contains(Proposta.getDummy(id)))
            return;

        for (Candidatura c : candidaturas){
            if(c.containsPropostaById(id)){
                c.getIdProposta().remove(id);
            }
        }

        for (Aluno a : alunos){
            if(a.temPropostaNaoConfirmada() && a.getPropostaNaoConfirmada().getId().equals(id)){
                a.removeNaoConfirmada();
            }
            if(a.temPropostaConfirmada() && a.getProposta().getId().equals(id)){
                a.removeProposta();
            }
            if(a.temCandidatura() && a.getCandidatura().getIdProposta().size() == 0){
                this.removeCandidatura(a.getNumeroAluno());
            }
        }

        propostas.removeIf(p -> p.getId().equals(id));
    }

    public boolean removeCandidatura(long naluno){
        Aluno a = getAluno(naluno);
        Candidatura c;
        if(a == null){
            return false;
        }
        c = a.getCandidatura();
        a.limpaCandidatura();
        return candidaturas.remove(c);
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

    public HashSet<Proposta> getPropostasSemCandidatura() {
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

    public HashSet<Proposta> getPropostasComCandidatura() {

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
        return alunos.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
    }

    public boolean existePropostaSemAluno(String proposta){ //Verifica se nas propostas
        HashSet<Proposta> p = getPropostasSemAluno();
        return p.contains(Proposta.getDummy(proposta));
    }

    public HashSet<Proposta> getPropostasSemAluno() { //Devolve "lista" de propostas não atribuídas a alunos
        HashSet<Proposta> propostaReturn = new HashSet<>();
        for (Proposta p : propostas){
            if(!p.isAtribuida()){
                propostaReturn.add(p);
            }
        }
        return propostaReturn;
    }

    public HashSet<Proposta> getPropostasAtribuidas() { //Devolve "lista" de propostas atribuídas a alunos
        HashSet<Proposta> propostaReturn = new HashSet<>();
        for (Proposta p : propostas){
            if(p.isAtribuida()){
                propostaReturn.add(p);
            }
        }
        return propostaReturn;
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

    public boolean atribuicaoManual(long nAluno, String idProposta) {
        Aluno a = getAluno(nAluno);
        if (a == null){
            return false;
        }
        Proposta p = getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(idProposta)).get(0);
        if(p == null || p.isAtribuida())
            return false;
        a.setProposta(p);
        return true;
    }

    public boolean remocaoManual(long nAluno, String idProposta) {
        Aluno a = getAluno(nAluno);
        if(a == null)
            return false;
        if(a.getProposta() instanceof Projeto_Estagio)
            return false;
        a.removeProposta();
        return true;
    }

    public void atribuiDocente(Projeto projeto) {
        for (Docente d : docentes){
            if(d.getEmail().equalsIgnoreCase(projeto.getEmailDocente())){
                projeto.setDocenteProponente(d);
                break;
            }
        }

    }

    public boolean verificaElegibilidade(long nAluno, String idProposta) {
        Aluno a = getAluno(nAluno);
        Proposta p = getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(idProposta)).get(0);
        if(p instanceof Estagio)
            return a.isPossibilidade();
        return true;
    }



    public boolean setOrientador(String emailDocente, String id) {
        for (Proposta p : propostas){
            if(p.getId().equalsIgnoreCase(id) ){
                if(!p.temDocenteOrientador()) {
                    p.setDocenteOrientador(getDocente(emailDocente));
                    return true;
                }else return false;

            }
        }
        return false;
    }

    public boolean removerOrientador(String emailDocente, String id) {
        for (Proposta p : propostas){
            if(p.getId().equalsIgnoreCase(id)){
                if(p.getEmailOrientador().equals(emailDocente)){
                    p.removeOrientador();
                    return true;
                }else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean verificaProposta(String id) {
        return propostas.stream().anyMatch(p -> p.getId().equals(id));
    }

}
