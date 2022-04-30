package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.ConflitoAtribuicaoAutomaticaException;
import pt.isec.pa.apoio_poe.model.LogSingleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Comparator.AlunoComparator;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtribuicaoPropostas extends StateAdapter{


    public AtribuicaoPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean avancarFase(){
        changeState(EnumState.ATRIBUICAO_ORIENTADORES);
        return true;
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public boolean conflitoAtribuicaoCandidatura() {
        changeState(EnumState.CONFLITO_ATRIBUICAO_CANDIDATURA);
        return true;
    }

    @Override
    public void gestaoManualAtribuicoes() { changeState(EnumState.ATRIBUICAO_MANUAL_PROPOSTAS);}

    @Override
    public void obtencaoListaProposta() {
        changeState(EnumState.OBTENCAO_LISTA_PROPOSTA_ATRIBUICAO);
    }

    @Override
    public void obtencaoListaAlunos() {
        changeState(EnumState.OBTENCAO_LISTA_ALUNOS_PROPOSTAS);
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAO_PROPOSTAS;
    }

    /**
     *
     * @return Booleano para dizer se correu bem o fecho ou nao
     */

    @Override
    public boolean close() {
        if((data.getAlunos().stream().filter(Aluno::temCandidatura)).allMatch(Aluno::temPropostaConfirmada)){
            setClose(true);
            MessageCenter.getInstance().putMessage("Fase fechada corretamente\n");
            return true;
        }
        MessageCenter.getInstance().putMessage("Condições de fecho de fase não alcançadas.\n");
        MessageCenter.getInstance().putMessage(qualAlunoComCandidaturaSemPropostaAssocaida());
        return false;
    }

    /**
     * Atribuicao automatia das propostas_estágios aos alunos
     */

    @Override
    public void atribuicaoAutomaticaEstagio_PropostaEProjetoComAluno() {
        Aluno aluno;
        for (Proposta p : data.getProposta()){
            if(p.getNumAluno() == null || (p instanceof Estagio)){
                continue;
            }
            aluno = data.getAluno(p.getNumAluno());
            if(aluno != null){
                aluno.setProposta(p);
                aluno.setOrdem(1);
            }
        }
    }

    /**
     *
     * @param file Nome do ficheiro
     * @return Boolean se correu bem a importação
     */

    @Override
    public boolean exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return false;
        }

        for(Aluno a : data.getAlunos()) {
            CSVWriter.writeLine(",", false, false, a.getExportAluno());
            if(a.temCandidatura())
                CSVWriter.writeLine(",", false, true,a.getCandidatura().getExportCandidatura());
            if(a.temPropostaConfirmada()) {
                CSVWriter.writeLine(",", false, true, a.getProposta().exportProposta());
                CSVWriter.writeLine(",", false, true, a.getOrdem());
            }
            CSVWriter.writeLine(",", true,false);
        }
        //data.exportAlunosCandidaturaProposta();
        CSVWriter.closeFile();

        return true;
    }

    /**
     * Atricao automatica dos alunos que tem candidatura
     */

    @Override
    public void atribuicaoAutomaticaSemAtribuicoesDefinidas() throws ConflitoAtribuicaoAutomaticaException {

        List<Aluno> al = new ArrayList<>();
        List<Aluno> alunosComMesmaMedia;
        for(Aluno a : data.getAlunos()){
            if(!a.temPropostaNaoConfirmada() && !a.temPropostaConfirmada() && a.temCandidatura()){
                al.add(a);
            }
        }
        al.sort(new AlunoComparator()); //Ordenacao dos alunos por media: MODO decrescente
        //Pega-se na media de um aluno e obtem se todos os alunos com a mesma media
        for(Aluno a : al){
            if(a.temPropostaConfirmada()) continue;
            alunosComMesmaMedia = obtemAlunosComMedia(a.getClassificacao(), al);
            atribuiPropostaAAlunosComMesmaMedia(alunosComMesmaMedia);
            alunosComMesmaMedia.clear();
        }

        al.forEach(System.out::println); //TODO
    }


    /**
     *
     * @param classificacao     Classificação com a qual queremos obter todos os alunos
     * @param al                Lista de alunos a returnar com a mesma media. Podem ser 0 ou mais
     * @return                  Retorna lista com os alunos com a mesma media
     */

    public List<Aluno> obtemAlunosComMedia(double classificacao, List<Aluno> al) {
        List<Aluno>alunosComMesmaMedia = new ArrayList<>();
        for(Aluno a : al){
            if(a.getClassificacao() == classificacao){
                alunosComMesmaMedia.add(a);
            }
        }
        return alunosComMesmaMedia;
    }



    private void atribuiPropostaAAlunosComMesmaMedia(List<Aluno> alunosComMesmaMedia) throws ConflitoAtribuicaoAutomaticaException {
        List<Proposta> proposta = new ArrayList<>();
        ConflitoAtribuicaoAutomaticaException e = null;
        int i; boolean sair = true;
        Map<Proposta, ArrayList<Aluno>> proposta_aluno = data.getProposta_aluno();
        ciclo: for (Aluno a : alunosComMesmaMedia){
            if(a.temPropostaConfirmada()) continue;
            proposta.clear();
            proposta = data.getPropostasAPartirDeId(proposta, a.getCandidatura().getIdProposta());
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
                //set.getKey().setNumAluno(set.getValue().get(0).getNumeroAluno());
                proposta.add(set.getKey()); //Adiciona à lista para remover posteriormente
            }
            else{
                i++;
                if(i == 1){
                    e = new ConflitoAtribuicaoAutomaticaException();
                }
            }
        }
        proposta.forEach(proposta_aluno::remove);
        if(e != null){
            throw e;
        }

    }

    /**
     *
     * @return Retorna uma STRING com os alunos que tem candidatura sem proposta associada
     */

    private String qualAlunoComCandidaturaSemPropostaAssocaida() {
        StringBuilder sb = new StringBuilder();
        (data.getAlunos().stream().filter(Aluno::temCandidatura)).filter(aluno -> !aluno.temPropostaConfirmada()).forEach(a -> {
            sb.append("Aluno ").append( a.getNumeroAluno()).append(" - ").append(a.getNome())
                    .append(" - Tem candidatura: ").append(a.getCandidatura()).
                    append("\nMas não tem proposta associada \n");
        });
        return sb.toString();
    }

}
