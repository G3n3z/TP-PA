package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtribuicaoOrientadores extends StateAdapter {
    public AtribuicaoOrientadores(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean avancarFase() {
        return false;
    }


    @Override
    public boolean recuarFase() {
        changeState(EnumState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.ATRIBUICAO_ORIENTADORES;
    }

    /**
     * Apenas fecha a fase se a anterior estiver fechada
     * @return ErrorCode com o resultado da operacao
     */
    @Override
    public ErrorCode close() {
        if(!data.getBooleanState(EnumState.ATRIBUICAO_PROPOSTAS)){
            return ErrorCode.E22;
        }
        setClose(true);
        changeState(EnumState.CONSULTA);
        data.closeState(getState());
        return ErrorCode.E0;
    }

    /**
     * Metodo que confirma que o proponente é o orientador
     */
    @Override
    public void associacaoAutomaticaDeDocentesAPropostas() {
        for(Proposta proposta : data.getProposta()){
            if(!proposta.temDocenteOrientador() && proposta.temDocenteProponente()){
                proposta.setDocenteOrientadorDocenteProponente();
            }
        }
    }

    /**
     * Transicao de estado para a gestao de orientadores
     * @return boolean com o resultado
     */
    @Override
    public boolean gerirOrientadores() {
        changeState(EnumState.GESTAO_ORIENTADORES);
        return true;
    }

    /**
     *
     * @param file path para o ficheiro no qual vai exportar
     * @return ErrorCode com o resultado da operação
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

                if (a.getProposta().temDocenteOrientador()) {
                    CSVWriter.writeLine(",", false, true, a.getProposta().getOrientador().getExportDocente());
                }
            }

            CSVWriter.writeLine(",", true,false);
        }

        CSVWriter.closeFile();

        return ErrorCode.E0;
    }

    /**
     *
     * @return
     */
    @Override
    public String getEstatisticasPorDocente() {
        StringBuilder sb = new StringBuilder();
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

    @Override
    public String getAlunosComPropostaEOrientador() {
        StringBuilder sb = new StringBuilder();
        for (Aluno a : data.getAlunos()){
            if (a.temPropostaConfirmada()){
                if(a.getProposta().temDocenteOrientador()){
                    sb.append("O aluno ").append(a.getNumeroAluno()).append(" - ").append(a.getNome()).append(" tem a proposta ").append(a.getProposta().getId())
                            .append(" Orientada pelo docente: ").append(a.getProposta().getEmailOrientador()).append('\n');
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String getAlunosComPropostaESemOrientador() {
        StringBuilder sb = new StringBuilder();
        for (Aluno a : data.getAlunos()){
            if (a.temPropostaConfirmada()){
                if(!a.getProposta().temDocenteOrientador()){
                    sb.append("O aluno ").append(a.getNumeroAluno()).append(" - ").append(a.getNome()).append(" tem a proposta ").append(a.getProposta().getId())
                            .append(" Sem Orientador\n");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public List<Proposta> getPropostasComOrientador() {
        List<Proposta> propostas = data.getPropostasCopia();
        propostas.removeIf(proposta -> !proposta.temDocenteOrientador());
        return propostas;
    }

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
    @Override
    public Map<Docente, Integer> getDocentesPorOrientacoes() {
        Map<Docente, ArrayList<Proposta>> docente_proposta= getEstatiticasDocente();
        Map<Docente, Integer> docente_numOrientacoes = new HashMap<>();
        for(Map.Entry<Docente,ArrayList<Proposta>> set : docente_proposta.entrySet()){
            docente_numOrientacoes.put(set.getKey().getClone(), set.getValue().size());
        }
        return docente_numOrientacoes;
    }
}
