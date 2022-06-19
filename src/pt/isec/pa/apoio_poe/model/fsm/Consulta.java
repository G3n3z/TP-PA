package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.utils.CSVWriter;
import pt.isec.pa.apoio_poe.utils.Constantes;

import java.util.*;

public class Consulta extends StateAdapter{

    public Consulta(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }


    @Override
    public EnumState getState() {
        return EnumState.CONSULTA;
    }

    /**
     *
     * @param file Path para o ficheiro a exportar
     * @return ErrorCode com o resultado do metodo
     */
    @Override
    public ErrorCode exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return ErrorCode.E2;
        }
        for(Aluno a : data.getAlunos()) {
            CSVWriter.writeLine(",", false, false, a.getExportAluno());
            if(a.temCandidatura())
                CSVWriter.writeLine(",", false, true,a.getCandidatura().getExportCandidatura());
            if(a.temPropostaConfirmada()) {
                CSVWriter.writeLine(",", false, true, a.getProposta().exportProposta());
                CSVWriter.writeLine(",", false, true, a.getOrdem());
            }
            if(a.temPropostaConfirmada() && a.getProposta().temDocenteOrientador()){
                CSVWriter.writeLine(",",false,true,a.getProposta().getOrientador().getExportDocente());
            }
            CSVWriter.writeLine(",", true,false);
        }

        CSVWriter.closeFile();

        return ErrorCode.E0;

    }

    /**
     *
     * @return retorna String com todos os alunos com proposta atribuida
     */
    @Override
    public String getTodosAlunosComPropostaAtribuida() {
        StringBuilder sb = new StringBuilder();
        data.getAlunos().stream().filter(Aluno::temPropostaConfirmada).forEach(aluno -> {
            sb.append("Aluno: ").append(aluno.getNumeroAluno()).append(" ").append(aluno.getNome())
                    .append(" tem a proposta ").append(aluno.getProposta().getId()).append(" Ordem: ")
                    .append(aluno.getOrdem()).append("\n");
        });

        return sb.toString();
    }

    /**
     *
     * @return String com os alunos sem proposta mas com candidatura
     */
    @Override
    public String obtencaoAlunosSemPropostaComCandidatura() {
        StringBuilder sb = new StringBuilder();
        for (Aluno a : data.getAlunos()) {
            if (a.temPropostaConfirmada() && a.temPropostaNaoConfirmada())
                continue;
            if (!a.temCandidatura())
                continue;
            List<Proposta> propostaStream = (data.getPropostasAPartirDeId(new ArrayList<>(), a.getCandidatura().getIdProposta()));
            if (propostaStream.stream().anyMatch(p -> !p.isAtribuida()))
                sb.append("Aluno: ").append(a.getNumeroAluno()).append(" - ").append(a.getNumeroAluno()).append(" com proposta(s) disponivel da sua candidatura: ");
            for (Proposta p : propostaStream) {
                if (!p.isAtribuida())
                    sb.append(p.getId()).append(" ");
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     *
     * @return retorna String com todas as propostas disponiveis
     */
    @Override
    public String getPropostasDisponiveis() {
        StringBuilder sb = new StringBuilder();
        data.getProposta().stream().filter(p -> !p.isAtribuida() && !(p instanceof Projeto_Estagio)).forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }


    /**
     *
     * @return retorna String com todas as propostas atribuidas
     */
    @Override
    public String getPropostasAtribuidasToString() {
        StringBuilder sb = new StringBuilder();
        data.getProposta().stream().filter(Proposta::isAtribuida).forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }

    /**
     *
     * @return retorna String com as estatisticas por docente
     */
    @Override
    public String getEstatisticasPorDocente() {
        StringBuilder sb = new StringBuilder();
        Map<Docente, ArrayList<Proposta>> docente_proposta = getEstatiticasDocente();

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
        double media = (double) count /  data.getDocentes().size();
        sb.append("O numero em media : ").append(media).append(" o minimo: ").append(min).append(" o maximo: ").append(max).append("\n");
        docente_proposta.forEach((k,v) -> {
            sb.append("O docente : ").append(k.getEmail()).append(" - ").append(k.getNome()).append(" é orientador de ").append(v.size())
                    .append(" propostas").append("\n");
        });
        return  sb.toString();
    }

    /**
     *
     * @return retorna uma map com a copia de docentes e um ArrayList das propostas do qual é orientador
     */
    private Map<Docente, ArrayList<Proposta>> getEstatiticasDocente(){
        Map<Docente, ArrayList<Proposta>> docente_proposta = new HashMap<>();
        data.getDocentes().forEach(d -> docente_proposta.put(d, new ArrayList<>()));
        for (Proposta p : data.getProposta()){
            if(p.temDocenteOrientador()){
                if(docente_proposta.containsKey(p.getOrientador())){
                    docente_proposta.get(p.getOrientador()).add(p);
                }else {
                    docente_proposta.put(p.getOrientador(),new ArrayList<>());
                    docente_proposta.get(p.getOrientador()).add(p);
                }

            }
        }

        return docente_proposta;
    }

    /**
     *
     * @return retorna as propostas atribuidas por Ramos
     */
    @Override
    public Map<String, Integer> getPropostasPorRamos() {
        Map<String, Integer> result = new HashMap<>();

        Integer quant;

        for (Aluno aluno : data.getAlunos()) {
            if(aluno.temPropostaConfirmada()){
                if(result.containsKey(aluno.getSiglaRamo())){
                    quant = result.get(aluno.getSiglaRamo());
                    quant++;
                    result.put(aluno.getSiglaRamo(), quant);
                }
                else{
                    result.put(aluno.getSiglaRamo(), 1);
                }
            }
        }
        for (String ramo : Constantes.getRamos()) {
            if(!result.containsKey(ramo)){
                result.put(ramo, 0);
            }
        }
        return result;
    }

    /**
     *
     * @return os dados das atibuiçoes, quantidade de propostas atribuidas,  propostas não atribuidas, percentagem de propostas atibuidas
     * percentagem de propostas não atibuidas
     */
    @Override
    public List<Double> getDadosAtribuicoes() {
        List<Double> dados = new ArrayList<>();
        int total = data.getProposta().size();
        double countAtribuida = (double) (int) data.getProposta().stream().filter(Proposta::isAtribuida).count();
        double countNotAtribuida = total  - countAtribuida;
        double percAtribuida = (countAtribuida * 100) / (double)total;
        double percNotAtribuida = 100 - percAtribuida;
        dados.addAll(List.of(countAtribuida,countNotAtribuida,percAtribuida,percNotAtribuida));
        return dados;
    }

    /**
     *
     * @return retorna um Map com o top5 das entidades e numero de de estágios da mesma
     */
    @Override
    public Map<String, Integer> getTop5Empresas() {

        Map<String, Integer> auxTop5 = new HashMap<>();
        for (Proposta proposta : data.getProposta()) {
            if(proposta instanceof Estagio e){
                String s = e.getEntidade();
                if(!s.equals("")){
                    if(auxTop5.containsKey(s)){
                        Integer aux = auxTop5.get(s);
                        aux++;
                        auxTop5.put(s, aux);
                    }
                    else{
                        auxTop5.put(s, 1);
                    }
                }
            }
        }

        Map<String, Integer> top5 = new HashMap<>();
        int max;
        String name="";
        for (int i = 0; i < 5; i++) {
            max = 0;
            for(Map.Entry<String,Integer> set : auxTop5.entrySet()){
                if(set.getValue() > max){
                    max = set.getValue();
                    name = set.getKey();
                }
            }
            if(max > 0)
                top5.put(name, max);
            auxTop5.remove(name);
        }


        return top5;
    }

    /**
     *
     * @return retorna um Map com uma copia do docentes e a quantidade de estágios a serem orientados pelos mesmos
     */
    @Override
    public Map<Docente, Integer> getTop5Orientadores() {
        Map<Docente, Integer> auxTop5 = new HashMap<>();
        Integer quant = 0;
        for (Proposta proposta : data.getProposta()) {
            if(proposta.temDocenteOrientador()){
                if(auxTop5.containsKey(proposta.getOrientador())){
                    quant = auxTop5.get(proposta.getOrientador());
                    auxTop5.put(proposta.getOrientador(),++quant);
                }else{
                    auxTop5.put(proposta.getOrientador().getClone(), 1);
                }
            }
        }
        Map<Docente, Integer> top5 = new HashMap<>();
        int max;
        Docente docente = null;
        for (int i = 0; i < 5; i++) {
            max = 0;
            for(Map.Entry<Docente,Integer> set : auxTop5.entrySet()){
                if(set.getValue() > max){
                    max = set.getValue();
                    docente = set.getKey();
                }
            }
            if(max > 0)
                top5.put(docente, max);
            auxTop5.remove(docente);
        }


        return top5;


    }

    /**
     *
     * @return retorna um Map com os docentes e a quantidade das suas orientações
     */
    @Override
    public Map<Docente, Integer> getDocentesPorOrientacoes() {
        Map<Docente, ArrayList<Proposta>> docente_proposta= getEstatiticasDocente();
        Map<Docente, Integer> docente_numOrientacoes = new HashMap<>();
        for(Map.Entry<Docente,ArrayList<Proposta>> set : docente_proposta.entrySet()){
            docente_numOrientacoes.put(set.getKey().getClone(), set.getValue().size());
        }
        return docente_numOrientacoes;
    }

    /**
     *
     * @return retorna um List com uma copia dos alunos com proposta confirmada e orientador atribuido
     */
    @Override
    public List<Aluno> getAlunosComPropostaConfirmadaEOrientador() {
        List<Aluno> alunos = new ArrayList<>();
        for (Aluno aluno : data.getAlunos()) {
            if(aluno.temPropostaConfirmada()){
                if (aluno.getProposta().temDocenteOrientador()){
                    alunos.add(aluno.getClone());
                }
            }
        }
        return alunos;
    }

    /**
     *
     * @return retorna um List com a copia dos alunos com proposta confirmada e sem orientador
     */
    @Override
    public List<Aluno> getAlunosComPropostaConfirmadaESemOrientador() {
        List<Aluno> alunos = new ArrayList<>();
        for (Aluno aluno : data.getAlunos()) {
            if(aluno.temPropostaConfirmada()){
                if (!aluno.getProposta().temDocenteOrientador()){
                    alunos.add(aluno.getClone());
                }
            }
        }
        return alunos;
    }

    /**
     *
     * @return retorna um map com o email e o numero medio, maximo e minimo de orientações por docente
     */
    @Override
    public Map<String, Number> getDadosNumeroOrientacoes() {
        int min = 0, max = 0, count = 0, index = 0;
        for(Map.Entry<Docente, ArrayList<Proposta>> set: getEstatiticasDocente().entrySet()){
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
        Map<String, Number> dadosReturn = new HashMap<>();
        double media = (double) count /  data.getDocentes().size();
        dadosReturn.put("media", media);
        dadosReturn.put("max", max);
        dadosReturn.put("min", min);
        return dadosReturn;
    }
}
