package pt.isec.pa.apoio_poe.model.data;

import java.util.HashSet;
import java.util.Set;

public class dados {
    Set<Pessoa> pessoas;
    Set<Proposta> propostas;

    public dados() {
        this.pessoas = new HashSet<>();
        this.propostas = new HashSet<>();
    }


    private Set<Aluno> getAlunos(){
        Set<Aluno> alunos = new HashSet<>();
        pessoas.forEach( a -> {
            if(a instanceof Aluno){
                alunos.add((Aluno)a);
            }
        });
        return alunos;
    }




}
