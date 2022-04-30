package pt.isec.pa.apoio_poe.model.Exceptions;

public enum ExceptionsCode {
    /**
     * Campo incorreto
     */
    E0,

    /**
     * Nome de ficheiro incorreto
     */
    E2,



    /**
     * Numero de aluno inexistente
     */
    I1,

    /**
     * Email de docente inexistente
     */
    I2,

    /**
     * Curso insexistente
     */
    I3,

    /**
     * Classificação não comprendida entre 0.0 e 1.0
     */
    I4,

    /**
     * Ramo inexistente
     */
    I5,

    /**
     * Tentativa de introdução de uma entidade em proposta não estágio
     */
    I6,

    /**
     * Proposta inexistente
     */
    I7,

    /**
     * Proposta ja contém ramo inserido
     */
    I8,

    /**
     * Numero de aluno já registado
     */
    I9,

    /**
     * Email já registado
     */
    I10,

    /**
     * Linha incompleta
     */
    I11,

    /**
     * Candidatura inexistente
     */
    O1,

    /**
     * Proposta inexistente em candidatura
     */
    O2,

    /**
     * Candidatura já existe
     */
    O3,

    /**
     * Tentativa de registar candidatura em aluno já com proposta
     */
    O4,

    /**
     * Inserção de candidatura a proposta com aluno já associado
     */
    O5,

    /**
     * Fase anterior Aberta
     */
    S1,

    /**
     * Atribuicao de propostas nao fechada //TODO: modificar texto
     */
    S2,

    /**
     *  numero total de propostas é igual ou superior ao número total de alunos e se, //TODO: modificar texto
     *  para cada ramo, o número total de propostas é igual ou superior ao número de alunos
     */
    S3

}
