package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.Exceptions.BaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.CollectionBaseException;
import pt.isec.pa.apoio_poe.model.Exceptions.IncompleteCSVLine;
import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;
import pt.isec.pa.apoio_poe.model.Singleton.MessageCenter;
import pt.isec.pa.apoio_poe.model.data.Aluno;
import pt.isec.pa.apoio_poe.model.data.Candidatura;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.propostas.Estagio;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto;
import pt.isec.pa.apoio_poe.model.data.propostas.Projeto_Estagio;
import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;
import pt.isec.pa.apoio_poe.utils.CSVReader;
import pt.isec.pa.apoio_poe.utils.CSVWriter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

public class GestaoPropostas extends StateAdapter{

    public GestaoPropostas(ApoioContext context, boolean isClosed, Data data) {
        super(context, isClosed, data);
    }

    @Override
    public boolean recuarFase() {
        changeState(EnumState.CONFIG_OPTIONS);
        return true;
    }

    @Override
    public EnumState getState() {
        return EnumState.GESTAO_PROPOSTAS;
    }

    @Override
    public boolean importPropostas(String file) {
        if(!CSVReader.startScanner(file,",")){
            //MessageCenter.getInstance().putMessage("O ficheiro não existe"); TODO
            return false;
        }

        String tipo;
        int index = 1;

        while (CSVReader.hasNext()) {
            CollectionBaseException cb = null;
            try {

                tipo = CSVReader.readString();
                switch (tipo) {
                    case "T1" -> {
                        if (!leT1(index))
                            throw new InvalidCSVField("Na linha " + index + " -> Tipo de proposta invalido" );
                    }
                    case "T2" -> {
                        if (!leT2(index))
                            throw new InvalidCSVField("Na linha " + index + " -> Tipo de proposta invalido" );
                    }
                    case "T3" -> {
                        if (!leT3(index))
                            throw new InvalidCSVField("Na linha " + index + " -> Tipo de proposta invalido" );
                    }
                    default -> throw new InvalidCSVField("Linha: " + index + " -> tipo de proposta não válida");
                }
            } catch (BaseException e) {
                //MessageCenter.getInstance().putMessage("Erro de leitura na linha: " + index + " do ficheiro: " + file);
                if(cb == null){
                    cb = new CollectionBaseException();
                }
                cb.putException(e);

                if(!CSVReader.nextLine()) break;
                index++;
                continue;
            }

            index++;
            if(!CSVReader.nextLine()) break;
        }

        return index != 1;
    }

    public boolean leT1(int index) throws InvalidCSVField {
        String id, titulo, entidade;
        List<String> ramos;
        long numAluno;
        try {
            id = CSVReader.readString();
            ramos = CSVReader.readRamos();
            titulo = CSVReader.readString();
            entidade = CSVReader.readString();
        } catch (NoSuchElementException e) {
            return false;
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (NoSuchElementException e){
            if(checkRamos(index, ramos)) {
                Proposta estagio = new Estagio(id, "T1", ramos, titulo, entidade);
                if (!data.addProposta(estagio)) {
                    //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                    InvalidCSVField ex = new InvalidCSVField("Na linha" + index + " -> " + "Proposta nao inserida porque o id ja existente");
                    ex.putLine(index);
                    throw ex;
                }
            }
            return true;
        }
        if(checkT1(index, ramos, numAluno)) {
            Proposta estagio = new Estagio(id, "T1", ramos, titulo, entidade, numAluno);
            if (data.addProposta(estagio)) {
                data.atribuipropostaNaoConfirmada(estagio, numAluno);
            }else {
                //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                InvalidCSVField ex = new InvalidCSVField("Na linha" + index + " -> " + "Proposta nao inserida porque o id ja existente");
                ex.putLine(index);
                throw ex;
            }
        }
        return true;
    }

    public boolean leT2(int index) throws InvalidCSVField, IncompleteCSVLine {
        String id, titulo, docente;
        List<String> ramos;
        long numAluno;
        try {
            id = CSVReader.readString();
            ramos = CSVReader.readRamos();
            titulo = CSVReader.readString();
            docente = CSVReader.readString();
        } catch (NoSuchElementException e) {
            throw new IncompleteCSVLine("Na linha " + index + " -> Linha Incompleta");
        }
        try {
            numAluno = CSVReader.readLong();
        } catch (NoSuchElementException e){
            if(checkT2(index,ramos,docente)) {
                Projeto projeto = new Projeto(id, "T2", ramos, titulo, docente);
                if (!data.addProposta(projeto)) {
                    //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                    throw new InvalidCSVField("Linha : " + index + "-> Proposta nao inserida porque o id ja existente", index);
                }else{
                    data.atribuiDocente(projeto);
                }
            }
            return true;
        }
        if(checkT2Aluno(index, ramos, docente, numAluno)) {
            Proposta projeto = new Projeto(id, "T2", ramos, titulo, docente, numAluno);
            if (data.addProposta(projeto)) {
                data.atribuipropostaNaoConfirmada(projeto, numAluno);
                data.atribuiDocente((Projeto) projeto);
            } else
                //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                throw new InvalidCSVField("Linha : " + index + "-> Proposta nao inserida porque o id ja existente", index);

        }
        return true;
    }

    public boolean leT3(int index) throws InvalidCSVField {
        String id, titulo;
        long numAluno;
        try {
            id = CSVReader.readString();
            titulo = CSVReader.readString();
            numAluno = CSVReader.readLong();
        } catch (InputMismatchException e) {
            return false;
        }
        if(checkT3(index, numAluno)) {
            Proposta autoproposta = new Projeto_Estagio(id, "T3", titulo, numAluno);
            if (data.addProposta(autoproposta)) {
                data.atribuipropostaNaoConfirmada(autoproposta, numAluno);
            } else {
                //MessageCenter.getInstance().putMessage("Linha : " + index + "-> Proposta nao inserida porque o id ja existente");
                throw new InvalidCSVField("Linha : " + index + "-> Proposta nao inserida porque o id ja existente", index);
            }
        }
        return true;
    }

    private boolean checkRamos(int index, List<String> ramos) {
        InvalidCSVField e;
        for(String ramo : ramos) {
            if (!data.existeRamos(ramo)) {
                //MessageCenter.getInstance().putMessage("Linha " + index + " -> está a tentar inserir uma proposta com um ramo inexistente");
                //throw new InvalidCSVField("Linha " + index + " -> está a tentar inserir uma proposta com um ramo inexistente", index);
                return false;
            }
        }
        return true;
    }

    private boolean checkT1(int index, List<String> ramos, long numAluno) throws InvalidCSVField {
        boolean ok = true;

        StringBuilder sb = new StringBuilder();

        if(!checkRamos(index, ramos)){
            sb.append("Está a tentar inserir uma proposta com um ramo inexistente. ");
        }

        if(!data.verificaNumAluno(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
            sb.append("O aluno não existe. ");
            ok = false;
        }
        if(!data.verificaRamoAluno(numAluno, ramos)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno de ramo diferente");
            sb.append("atribuição de proposta a aluno de ramo diferente. ");
            ok = false;
        }
        if(!data.verificaPossibilidade(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno não elegível");
            sb.append("atribuição de proposta a aluno não elegível. ");
            ok = false;
        }
        if(data.verificaJaAtribuido(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            sb.append("atribuição de proposta a aluno com proposta já atribuída. ");
            ok = false;
        }

        if(!ok){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }
        return true;
    }

    private boolean checkT2(int index, List<String> ramos, String docente) throws InvalidCSVField {
        StringBuilder sb = new StringBuilder();

        if(!checkRamos(index, ramos)){
            sb.append("Está a tentar inserir uma proposta com um ramo inexistente. ");
        }
        if(!data.existeDocenteComEmail(docente)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> está a tentar inserir um docente não registado");
            sb.append(" está a tentar inserir um docente não registado");
        }
        if(sb.length() > 0){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }
        return true;
    }

    private boolean checkT2Aluno(int index, List<String> ramos, String docente, long numAluno) throws  InvalidCSVField {
        StringBuilder sb = new StringBuilder();

        if(!checkRamos(index, ramos)){
            sb.append("Está a tentar inserir uma proposta com um ramo inexistente. ");
        }
        if(!data.existeDocenteComEmail(docente)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> está a tentar inserir um docente não registado");
            sb.append(" está a tentar inserir um docente não registado");
        }
        if(!data.verificaNumAluno(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
            sb.append(" aluno atribuído inexistente");
        }
        if(!data.verificaRamoAluno(numAluno, ramos)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno de ramo diferente");

            sb.append(" atribuição de proposta a aluno de ramo diferente");
        }
        if(data.verificaJaAtribuido(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            sb.append(" atribuição de proposta a aluno com proposta já atribuída");
        }
        if(sb.length() > 0){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }
        return true;
    }

    private boolean checkT3(int index, long numAluno) throws InvalidCSVField {
        StringBuilder sb = new StringBuilder();
        if(!data.verificaNumAluno(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> aluno atribuído inexistente");
            sb.append(" Aluno atribuído inexistente");
        }
        if(data.verificaJaAtribuido(numAluno)){
            //MessageCenter.getInstance().putMessage("Linha " + index + " -> atribuição de proposta a aluno com proposta já atribuída");
            sb.append("Atribuição de proposta a aluno com proposta já atribuída");
        }
        if(sb.length() > 0){
            throw new InvalidCSVField("Na linha " + index + " -> " + sb, index);
        }

        return true;
    }

    @Override
    public ErrorCode exportarCSV(String file) {
        if(!CSVWriter.startWriter(file)){
            return ErrorCode.E2;
        }
        for(Proposta p : data.getProposta())
            CSVWriter.writeLine(",", true, false,p.exportProposta());
        CSVWriter.closeFile();
        return ErrorCode.E0;
    }

    @Override
    public ErrorCode removeProposta(String id) {
        if(!data.verificaProposta(id)){
            //MessageCenter.getInstance().putMessage("Nao existe o id inserido");
            return ErrorCode.E9;
        }

        data.removeProposta(id);
        return ErrorCode.E0;
    }

    @Override
    public void editarPropostas() {
        changeState(EnumState.EDITAR_PROPOSTAS);
    }

    @Override
    public String getPropostasToString() {
        return data.getPropostasToString();
    }

    @Override
    public boolean removeAll() {
        for (Proposta p : data.getProposta()){
            data.removeProposta(p.getId());
        }
        return true;
    }
}
