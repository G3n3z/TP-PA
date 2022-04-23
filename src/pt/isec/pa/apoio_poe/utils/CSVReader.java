package pt.isec.pa.apoio_poe.utils;

import pt.isec.pa.apoio_poe.model.Exceptions.InvalidField;

import java.io.*;
import java.util.*;

public class CSVReader {

    private CSVReader(){}

    private static Scanner sc;
    private static BufferedReader br;



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

    public static Boolean readBoolean(){

        return sc.nextBoolean();
    }
    public static Boolean readBoolean2() throws InvalidField {

        try{
            return sc.nextBoolean();
        }catch (InputMismatchException e){
            throw new InvalidField("Erro de leitura no Booleano");
        }
    }
    public static String readString(){
        return sc.next();
    }

    public static List<String> readRamos(){
        String [] tokens = sc.next().split("\\|");
        List<String> ramos = new ArrayList<>();
        ramos.addAll(Arrays.asList(tokens));
        return ramos;
    }

    public static int readInt(){
        return sc.nextInt();
    }
    public static Double readDouble(){
        return sc.nextDouble();
    }
    public static Double readDouble2() throws InvalidField {
        try{
            return sc.nextDouble();
        }catch (InputMismatchException e){
            throw new InvalidField("Erro na leitura do double ");
        }
    }

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
    public static long readLong(){

        return sc.nextLong();

    }
    public static long readLong2() throws InvalidField {
        try {
            return sc.nextLong();
        }catch (InputMismatchException e){
            throw new InvalidField("Erro de leitura no long");
        }
    }
    public static boolean hasNext(){return sc.hasNext();}
    public static void closeReaders(){
       try {
           br.close();
       }catch (IOException e){

       }finally {
           sc.close();
       }
    }


    //read parameters
    //passar um array de elementos genericos e fazer um ciclo com switch

}
