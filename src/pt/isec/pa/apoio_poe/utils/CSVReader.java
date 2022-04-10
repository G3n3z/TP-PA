package pt.isec.pa.apoio_poe.utils;

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

    public static void nextLine(){
        String line;
        try {
            line = br.readLine();

        }catch (IOException e){

            return;
        }
        if(line == null){
            return;
        }
        sc.close();
        sc = new Scanner(line).useLocale(Locale.US);
        sc.useDelimiter("[,\r\n]");

        //sc.nextLine();
    }
    public static long readLong(){
        return sc.nextLong();
    }
    public static boolean hasNextN(){return sc.hasNext("\n");}
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
