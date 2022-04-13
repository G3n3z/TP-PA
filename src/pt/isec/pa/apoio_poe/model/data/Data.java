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

    //verificar se a sigla do aluno corresponde a alguma das listadas na proposta
    public boolean verificaRamoAluno(long numAluno, List<String> ramos){
        return alunos.stream().anyMatch(p -> p.getNumeroAluno() == numAluno && ramos.contains(p.getSiglaRamo()));
    }

    //verificar se o aluno tem possibilidade de aceder a estagios alem de projetos //verificar a utilidade TODO
    public boolean verificaPossibilidade(long numAluno){
        return alunos.stream().anyMatch(p ->p.getNumeroAluno() == numAluno && p.isPossibilidade());
    }

    //verificar se o aluno ja contem um proposta atribuida
    public boolean verificaJaAtribuido(long numAluno){
        return alunos.stream().anyMatch(p ->p.getNumeroAluno() == numAluno && (p.temPropostaNaoConfirmada() || p.temPropostaConfirmada()));
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

    public String obtencaoAlunosComAutoPropostaAtribuida(){ //Obtenção de listas de alunos: Com autoproposta atribuida.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.getProposta() instanceof Projeto_Estagio).forEach(sb::append);
        return sb.toString();
    }

    public String obtencaoAlunosComCandidatura(){  //Obtenção de listas de alunos: om candidatura já registada.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.getCandidatura() != null).forEach(sb::append);
        return sb.toString();
    }

    public String obtencaoAlunosSemCandidatura(){ //  Obtenção de listas de alunos: Sem candidatura registada.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.getCandidatura() == null).forEach(sb::append);
        return sb.toString();
    }

    public String obtencaoAlunosSemProposta() { //Obter listas de alunos sem propostas registadas ou confirmadas.
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(a -> a.getProposta() == null && a.getPropostaNaoConfirmada() == null).forEach(sb::append);
        return sb.toString();
    }

    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {
        Aluno aluno;
        for (Proposta p : propostas){
            if(p.getNumAluno() == null || (p instanceof Estagio)){
                continue;
            }
            aluno = getAluno(p.getNumAluno());
            if(aluno != null){
                aluno.setProposta(p);
                aluno.setOrdem(1);
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

    public String getPropostasWithFiltersToStringAtribuicao(int ...filters){
        StringBuilder sb = new StringBuilder();
        getPropostasWithFiltersAtribuicao(filters).forEach( p -> sb.append(p).append("\n"));
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

    public  Set<Proposta> getPropostasWithFiltersAtribuicao(int ...filters){
        Set<Proposta> propostas = new HashSet<>();
        for (int i : filters){
            switch (i){
                case 1 -> propostas.addAll(getAutoPropostas());
                case 2 -> propostas.addAll(getProjetos());
                case 3 -> propostas.addAll(getPropostasSemAluno());
                case 4 -> propostas.addAll(getPropostasAtribuidas());
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

    public boolean existePropostaSemAluno(String proposta){ //Verifica se nas propostas
        HashSet<Proposta> p = getPropostasSemAluno();
        return p.contains(Proposta.getDummy(proposta));
    }

    private HashSet<Proposta> getPropostasSemAluno() { //Devolve "lista" de propostas não atribuídas a alunos
        HashSet<Proposta> propostaReturn = new HashSet<>();
        for (Proposta p : propostas){
            if(p.getNumAluno() == null && !p.isAtribuida()){
                propostaReturn.add(p);
            }
        }
        return propostaReturn;
    }

    private HashSet<Proposta> getPropostasAtribuidas() { //Devolve "lista" de propostas atribuídas a alunos
        HashSet<Proposta> propostaReturn = new HashSet<>();
        for (Proposta p : propostas){
            if(p.getNumAluno() != null && p.isAtribuida()){
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
            if(a.temPropostaConfirmada()) continue;
            alunosComMesmaMedia = obtemAlunosComMedia(a.getClassificacao(), al);
            atribuiPropostaAAlunosComMesmaMedia(alunosComMesmaMedia);
            //al.removeAll(alunosComMesmaMedia);
            alunosComMesmaMedia.clear();
            //Limpar os alunos ja atribuidos
        }

        al.forEach(System.out::println);
    }

    private void atribuiPropostaAAlunosComMesmaMedia(List<Aluno> alunosComMesmaMedia) {
        List<Proposta> proposta = new ArrayList<>();
        ConflitoAtribuicaoAutomaticaException e = null;
        int i; boolean sair = true;
        ciclo: for (Aluno a : alunosComMesmaMedia){
            if(a.temPropostaConfirmada()) continue;
            proposta.clear();
            proposta = getPropostasAPartirDeId(proposta, a.getCandidatura().getIdProposta());
            for(Proposta p : proposta){
                if(!p.isAtribuida() ){//Se a proposta está atribuida
                    if(p instanceof Estagio && !a.isPossibilidade()){
                        continue;
                    }
                    if(proposta_aluno.containsKey(p)){ //Se já contem a proposta
                        if(proposta_aluno.get(p).size() == 1){
                            sair = !sair;
                            if(sair){
                                proposta_aluno.remove(p);
                                break ciclo;
                            }
                        }
                        proposta_aluno.get(p).add(a);
                        break;
                    }
                    else{
                        proposta_aluno.put(p, new ArrayList<>());
                        proposta_aluno.get(p).add(a);
                        break;
                    }
                }
            }
        }
        i = 0; proposta.clear();
        for(Map.Entry<Proposta, ArrayList<Aluno>> set : proposta_aluno.entrySet()){
            if(set.getValue().size() == 1){
                set.getValue().get(0).setProposta(set.getKey());
                set.getKey().setAtribuida(true);
                set.getKey().setNumAluno(set.getValue().get(0).getNumeroAluno());
                proposta.add(set.getKey()); //Adiciona à lista para remover posteriormente
            }
            else{
                i++;
                if(i == 1){
                    e = new ConflitoAtribuicaoAutomaticaException();
                }
            }
        }
        proposta.forEach( p -> proposta_aluno.remove(p));
        if(e != null){
            throw e;
        }

    }


    private List<Aluno> obtemAlunosComMedia(double classificacao, List<Aluno> al) {
        List<Aluno>alunosComMesmaMedia = new ArrayList<>();
        for(Aluno a : al){
            if(a.getClassificacao() == classificacao){
                alunosComMesmaMedia.add(a);
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


    public String getTodosAlunosComPropostaAtribuida() {
        StringBuilder sb = new StringBuilder();
        alunos.stream().filter(Aluno::temPropostaConfirmada).forEach(aluno -> {
            sb.append("Aluno: ").append(aluno.getNumeroAluno()).append(" ").append(aluno.getNome())
                    .append(" tem a proposta ").append(aluno.getProposta().getId()).append(" Ordem: ")
                    .append(aluno.getOrdem()).append("\n");
        });

        return sb.toString();
    }

    public boolean todosOsAlunosComCandidaturaTemPropostaAssocaida(){
        return (alunos.stream().filter(Aluno::temCandidatura)).allMatch(Aluno::temPropostaConfirmada);
    }


    public String qualAlunoComCandidaturaSemPropostaAssocaida() {
        StringBuilder sb = new StringBuilder();
        (alunos.stream().filter(Aluno::temCandidatura)).filter(aluno -> !aluno.temPropostaConfirmada()).forEach(a -> {
            sb.append("Aluno ").append( a.getNumeroAluno()).append(" - ").append(a.getNome())
                    .append("Tem candidatura: ").append(a.getCandidatura()).
                    append("\nMas não tem proposta associada \n");
        });
        return sb.toString();
    }

    public boolean atribuicaoManual(long nAluno, String idProposta) {
        Aluno a = getAluno(nAluno);
        Proposta p = getPropostasAPartirDeId(new ArrayList<>(), Collections.singletonList(idProposta)).get(0);
        if(p == null)
            return false;
        a.setProposta(p);
        return true;
    }

    public boolean remocaoManual(long nAluno, String idProposta) {
        Aluno a = getAluno(nAluno);
        if(a == null)
            return false;
        a.removeProposta();
        return true;
    }

    public void associacaoAutomaticaDeDocentesAPropostas() {
        for(Proposta proposta : propostas){

            if(!proposta.temDocenteOrientador() && proposta.temDocenteProponente()){
                proposta.setDocenteOrientadorDocenteProponente();
            }

        }
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
        return !(p.getTipo().equals("T1") && a.isPossibilidade());
    }

    public boolean verificaCondicaoFechoF1() {
        int pDA = (int) propostas.stream().filter(proposta -> proposta.getRamos().contains("DA")).count();
        int pRAS = (int) propostas.stream().filter(proposta -> proposta.getRamos().contains("RAS")).count();
        int pSI = (int) propostas.stream().filter(proposta -> proposta.getRamos().contains("SI")).count();

        int totDA = 0, totRAS = 0, totSI = 0;
        for(Aluno a : alunos){
            if(a.getSiglaRamo().equals("DA"))
                totDA++;
            if(a.getSiglaRamo().equals("RAS"))
                totRAS++;
            if(a.getSiglaRamo().equals("SI"))
                totSI++;
        }
        return (propostas.size() >= alunos.size()) && (pDA >= totDA) && (pRAS >= totRAS) && (pSI >= totSI);
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

    public String getAlunosComPropostaEOrientador() {
        StringBuilder sb = new StringBuilder();
        for (Aluno a : alunos){
            if (a.temPropostaConfirmada()){
                if(a.getProposta().temDocenteOrientador()){
                    sb.append("O aluno ").append(a.getNumeroAluno()).append(" - ").append(a.getNome()).append(" tem a proposta ").append(a.getProposta().getId())
                            .append(" Orientada pelo docente: ").append(a.getProposta().getEmailOrientador());
                }
            }
        }
        return sb.toString();
    }

    public String getAlunosComPropostaESemOrientador() {
        StringBuilder sb = new StringBuilder();
        for (Aluno a : alunos){
            if (a.temPropostaConfirmada()){
                if(!a.getProposta().temDocenteOrientador()){
                    sb.append("O aluno ").append(a.getNumeroAluno()).append(" - ").append(a.getNome()).append(" tem a proposta ").append(a.getProposta().getId())
                            .append(" Sem Orientador");
                }
            }
        }
        return sb.toString();
    }

    public String getEstatisticasPorDocente() {
        StringBuilder sb = new StringBuilder();
        Map<Docente, ArrayList<Proposta>> docente_proposta = new HashMap<>();
        docentes.forEach(d -> docente_proposta.put(d, new ArrayList<>()));
        for (Proposta p : propostas){
            if(p.temDocenteOrientador()){
                if(docente_proposta.containsKey(p.getOrientador())){
                    docente_proposta.get(p.getOrientador()).add(p);
                }else {
                    docente_proposta.put(p.getOrientador(),new ArrayList<>());
                    docente_proposta.get(p.getOrientador()).add(p);
                }

            }
        }
        int min = 0, max = 0, count = 0, index = 0;
        for(Map.Entry<Docente, ArrayList<Proposta>> set: docente_proposta.entrySet()){
            if(index == 0){
                max = min = set.getValue().size();
                index++;
            }
            if(set.getValue().size() < min){
                min = set.getValue().size();
            }
            if(set.getValue().size() > max){
                max = set.getValue().size();
            }
            count += set.getValue().size();

        }
        double media = (double) count / docentes.size();
        sb.append("O numero em media : ").append(media).append(" o minimo: ").append(min).append(" o maximo: ").append(max).append("\n");
        docente_proposta.forEach((k,v) -> {
            sb.append("O docente : ").append(k.getEmail()).append(" - ").append(k.getNome()).append(" é orientador de ").append(v.size())
                    .append(" propostas").append("\n");
        });
        return  sb.toString();
    }

}
