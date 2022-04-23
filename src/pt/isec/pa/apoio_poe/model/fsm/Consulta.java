package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Docente;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consulta extends StateAdapter{

    public Consulta(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }


    @Override
    public EnumState getState() {
        return EnumState.CONSULTA;
    }

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
            if(a.getProposta().temDocenteOrientador()){
                CSVWriter.writeLine(",",false,true,a.getProposta().getOrientador().getExportDocente());
            }
            CSVWriter.writeLine(",", true,false);
        }

        CSVWriter.closeFile();

        return true;

    }
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

    @Override
    public String getPropostasDisponiveis() {
        StringBuilder sb = new StringBuilder();
        data.getProposta().stream().filter(p -> !p.isAtribuida() && !(p instanceof Projeto_Estagio)).forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }

    @Override
    public String getPropostasAtribuidasToString() {
        StringBuilder sb = new StringBuilder();
        data.getProposta().stream().filter(Proposta::isAtribuida).forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }

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

}
