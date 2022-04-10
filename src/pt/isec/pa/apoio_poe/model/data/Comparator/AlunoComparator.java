package pt.isec.pa.apoio_poe.model.data.Comparator;

import pt.isec.pa.apoio_poe.model.data.Aluno;

import java.util.Comparator;

public class AlunoComparator implements Comparator<Aluno> {
    @Override
    public int compare(Aluno a1, Aluno a2) {
        if (a1.getClassificacao() - a2.getClassificacao()< 0){
            return -1;
        }else if (a1.getClassificacao() - a2.getClassificacao() == 0){
            return 0;
        }
        else
            return 1;
    }
}
