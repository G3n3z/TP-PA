package pt.isec.pa.apoio_poe.model.Exceptions;

import pt.isec.pa.apoio_poe.model.data.Aluno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ConflitoAtribuicaoAutomaticaException extends RuntimeException{

    List<Long> nAlunos;
    List<String> candidaturasDisponiveis;
    List<String> candidaturaEmConflito;
    List<Aluno> alunos;
    public ConflitoAtribuicaoAutomaticaException() {
        nAlunos = new ArrayList<>();
        candidaturaEmConflito = new ArrayList<>();
        candidaturasDisponiveis =  new ArrayList<>();
    }

    public void addAluno(Long nAluno){
        this.nAlunos.add(nAluno);
    }
    public void addConflito(String ...candidaturasEmConflito){
        this.candidaturaEmConflito.addAll(Arrays.asList(candidaturasEmConflito));
    }

    public void addDisponiveis(String ...candidaturasDisponiveis){
        this.candidaturaEmConflito.addAll(Arrays.asList(candidaturasDisponiveis));
    }
}
