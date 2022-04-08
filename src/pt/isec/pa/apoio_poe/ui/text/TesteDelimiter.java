package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.utils.CSVReader;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TesteDelimiter {
    public static void main1(String[] args) throws FileNotFoundException {
        Scanner sc;
        try {
            sc = new Scanner(new File("teste.csv"));
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
            return;
        }

        sc.useDelimiter(",");
        while (sc.hasNext()){
            System.out.println(sc.next());
        }
    }

    public static void main(String[] args) {
        if(!CSVReader.startScanner("teste.csv",",")){
            System.out.println("Nao conseguiu ler o ficheiro");
            return;
        }
        try {
            while (CSVReader.hasNext()) {
                try {
                System.out.println("Long: " + CSVReader.readString());
                System.out.println("Nome: " + CSVReader.readString());
                System.out.println("Email: " + CSVReader.readString());
                System.out.println("Curso: " + CSVReader.readString());
                System.out.println("Ramo: " + CSVReader.readString());
                System.out.println("Double: " + CSVReader.readDouble());
                System.out.println("Bool: " + CSVReader.readBoolean());
                }catch (InputMismatchException e){
                    CSVReader.nextLine();
                }
            }
        }catch (InputMismatchException e){
            System.out.println(e.toString());
        }


    }

    public static void main3(String[] args) {
        if(!CSVReader.startScanner("teste.csv",",")){
            System.out.println("Nao conseguiu ler o ficheiro");
            return;
        }




    }


}
