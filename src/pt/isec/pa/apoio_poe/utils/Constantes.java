package pt.isec.pa.apoio_poe.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constantes {
    private final static ArrayList ramos ;
    private final static ArrayList cursos;

    static {
        ramos = new ArrayList<>(Arrays.asList("DA","SI", "RAS"));
        cursos = new ArrayList<>(Arrays.asList("LEI-PL", "LEI"));
    }

    public static ArrayList<String> getRamos(){
        return new ArrayList<String>(ramos);
    }
    public static ArrayList<String> getCursos(){
        return new ArrayList<String>(cursos);
    }
    private Constantes(){}


}
