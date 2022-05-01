package pt.isec.pa.apoio_poe.utils;

import pt.isec.pa.apoio_poe.model.Exceptions.InvalidCSVField;

import java.io.*;
import java.util.*;

public class CSVReader {

    private CSVReader(){}

    private static Scanner sc;
    private static BufferedReader br;


    /**
     *
     * @param file Nome do ficheiro para abrir para leitura
     * @param delimiter delimitador a ser usado no scanner
     * @return retorna true or false caso corra bem ou nao
     */
    public static boolean startScanner(String file, String delimiter){

        try {
            br = new BufferedReader(new FileReader(file));
        }catch (FileNotFoundException e) {
            return false;
        }
        String line;
        try {
            line = br.readLine();
        }catch (IOException e){
            return false;
        }
        sc = new Scanner(line).useLocale(Locale.US);
        sc.useDelimiter("[,\r\n]");
        return true;
    }

    /**
     *
     * @return retorna a leitura de booleano a partir do ficheiro
     */
    public static Boolean readBoolean(){

        return sc.nextBoolean();
    }

    /**
     *
     * @return retorna booleano lido do ficheiro aberto
     * @throws InvalidCSVField lança a exceção de campo invalido
     */
    public static Boolean readBoolean2() throws InvalidCSVField {

        try{
            return sc.nextBoolean();
        }catch (InputMismatchException e){
            throw new InvalidCSVField("Erro de leitura no Booleano");
        }
    }
    public static String readString(){
        return sc.next();
    }

    /**
     *
     * @return retorna os ramos separados por um limitador
     */
    public static List<String> readRamos(){
        String [] tokens = sc.next().split("\\|");
        List<String> ramos = new ArrayList<>();
        ramos.addAll(Arrays.asList(tokens));
        return ramos;
    }

    /**
     *
     * @return retorna um inteiro lido do ficheiro aberto
     */
    public static int readInt(){
        return sc.nextInt();
    }

    /**
     *
     * @return retorna um double lido do ficheiro aberto
     */
    public static Double readDouble(){
        return sc.nextDouble();
    }

    /**
     *
     * @return retorna um double lido do ficheiro
     * @throws InvalidCSVField lança a exceção de campo invalido
     */
    public static Double readDouble2() throws InvalidCSVField {
        try{
            return sc.nextDouble();
        }catch (InputMismatchException e){
            throw new InvalidCSVField("Erro na leitura do double ");
        }
    }

    /**
     *
     * @return retorna um booleano a dizer se conseguiu mudar de linha ou nao. Caso chega ao fim do ficheiro retorna falso
     */
    public static boolean nextLine(){
        String line;
        try {
            line = br.readLine();

        }catch (IOException e){

            return false;
        }
        if(line == null){
            return false;
        }
        sc.close();
        sc = new Scanner(line).useLocale(Locale.US);
        sc.useDelimiter("[,\r\n]");

        return true;
    }

    /**
     *
     * @return retorna um long lido do ficheiro aberto
     */
    public static long readLong(){
        return sc.nextLong();
    }

    /**
     *
     * @return retorna um long lido do ficheiro
     * @throws InvalidCSVField lança a exceção de campo invalido
     */
    public static long readLong2() throws InvalidCSVField {
        try {
            return sc.nextLong();
        }catch (InputMismatchException e){
            throw new InvalidCSVField("Erro de leitura no long");
        }
    }

    /**
     *
     * @return retorna se existe proximo elemento
     */
    public static boolean hasNext(){return sc.hasNext();}

    /**
     * Funcao que fecha o ficheiro aberto
     */
    public static void closeReaders(){
       try {
           br.close();
       }catch (IOException e){

       }finally {
           sc.close();
       }
    }


}
