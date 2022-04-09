package pt.isec.pa.apoio_poe.utils;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class CSVReader {

    private CSVReader(){}

    private static Scanner sc;
    private static BufferedReader br;

    public static boolean startScanner(String file, String delimiter){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        }catch (FileNotFoundException e) {
            return false;
        }

        sc = new Scanner(br).useLocale(Locale.US);
        sc.useDelimiter("[,\n\r]+");

        return true;
    }

    public static Boolean readBoolean(){
        return sc.nextBoolean();
    }

    public static String readString(){
        return sc.next();
    }

    public static int readInt(){
        return sc.nextInt();
    }
    public static Double readDouble(){
        return sc.nextDouble();
    }

    public static void nextLine(){
        sc.nextLine();
    }
    public static long readLong(){
        return sc.nextLong();
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
