package pt.isec.pa.apoio_poe.model.errorCode;

public enum ErrorCode {


    /***
     * Correu bem
      */
    E0,
    /**
     * Leitura de campo incorreto
     */
    E1,

    /**
     * Nome de ficheiro incorreto
     */
    E2,

    /**
     * Numero de aluno inexistente
     */
    E3,

    /**
     * Email de docente inexistente
     */
    E4,

    /**
     * Curso insexistente
     */
    E5,

    /**
     * Classificação não comprendida entre 0.0 e 1.0
     */
    E6,

    /**
     * Ramo inexistente
     */
    E7,

    /**
     * Tentativa de introdução de uma entidade em proposta não estágio
     */
    E8,

    /**
     * Proposta inexistente
     */
    E9,

    /**
     * Proposta ja contém ramo inserido
     */
    E10,

    /**
     * Numero de aluno já registado
     */
    E11,

    /**
     * Email já registado
     */
    E12,

    /**
     * Linha incompleta
     */
    E13,

    /**
     * Aluno não pode aceder a estágio
     */
    E14,

    /**
     * Proposta já atribuída
     */
    E15,

    /**
     * Proposta sem Docente Orientador
     */
    E16,

    /**
     * Candidatura inexistente
     */
    E17,

    /**
     * Proposta inexistente em candidatura
     */
    E18,

    /**
     * Candidatura já existe
     */
    E19,

    /**
     * Tentativa de registar candidatura em aluno já com proposta
     */
    E20,

    /**
     * Inserção de candidatura a proposta com aluno já associado
     */
    E21,

    /**
     * Fase anterior Aberta
     */
    E22,

    /**
     * Atribuicao de propostas nao fechada //TODO: modificar texto
     */
    E23,

    /**
     *  numero total de propostas é igual ou superior ao número total de alunos e se, //TODO: modificar texto
     *  para cada ramo, o número total de propostas é igual ou superior ao número de alunos
     */
    E24,

    /***
     * A operaçao não é valida no estado atual
     */
    E25, 


    /**
     * Condicoes de fecho nao alcançadas //TODO
     */
    E26,

    /**
     * Proposta nao contem o ramo inserido
     */
    E27, 

    /**
     * Aluno nao tem candidatura
     */
    E28,

    /**
     * Proposta tem aluno previamente atibuido
     */
    E29,


    /**
     * Porposta ja contem docente Orientador
     */
    E30,


    /**
     * Aluno com ramo incompativel com proposta
     */
    E31,


    /**
     * Proposta já existente
     */
    E32,

    /**
     * Nao pode remover o ramo do aluno associado
     */
    E33,

    /**
     *
     * Aluno já contem proposta
     */
    E34

}
