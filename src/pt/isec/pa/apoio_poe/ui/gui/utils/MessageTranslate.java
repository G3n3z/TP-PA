package pt.isec.pa.apoio_poe.ui.gui.utils;

import pt.isec.pa.apoio_poe.model.errorCode.ErrorCode;

public class MessageTranslate {
    private static MessageTranslate instance;

    public static String translateErrorCode(ErrorCode e){
        return switch(e){
            case E1 -> "Leitura de campo incorreto";
            case E2 -> "Nome de ficheiro incorreto";
            case E3 -> "Numero de aluno inexistente";
            case E4 -> "Email de docente inexistente";
            case E5 ->  "Curso insexistente";
            case E6 -> "Classificação não comprendida entre 0.0 e 1.0";
            case E7 -> "Ramo inexistente";
            case E8 -> "Tentativa de introdução de uma entidade em proposta não estágio";
            case E9 ->  "Proposta inexistente";
            case E10 ->  "Proposta ja contém ramo inserido";
            case E11 ->  "Numero de aluno já registado";
            case E12 ->  "Email já registado";
            case E13 ->  "Linha incompleta";
            case E14 ->  "Aluno não pode aceder a estágio";
            case E15 ->  "Proposta já atribuída a um aluno";
            case E16 ->  "Proposta sem Docente Orientador";
            case E17 ->  "Candidatura inexistente";
            case E18 ->  "Proposta inexistente em candidatura";
            case E19 ->  "Candidatura já existe";
            case E20 ->  "Tentativa de registar candidatura em aluno já com proposta";
            case E21 ->  "Inserção de candidatura a proposta com aluno já associado";
            case E22 ->  "Fase anterior Aberta";
            case E23 ->  "Atribuicao de propostas nao se encontra fechada";
            case E24 ->  "numero total de propostas é igual ou superior ao número total" +
                    " de alunos e se, para cada ramo, o número total de propostas " +
                    "é igual ou superior ao número de alunos";
            case E25 ->  "A operação não é válida no estado atual";
            case E26 ->  "Condições de fecho não alcançadas";
            case E27 ->  "Proposta não contem ramo inserido";
            case E28 ->  "Aluno não tem candidatura";
            case E29 ->  "Proposta contem aluno previamente attribuído";
            case E30 ->  "Proposta já contem docente orientador";
            case E31 -> "Aluno com ramo incompativel com a Proposta";
            case E32 -> "Proposta já existente";
            case E33 -> "Não pode remover o ramo do aluno associado";
            default ->  "";
        };
    }
}
