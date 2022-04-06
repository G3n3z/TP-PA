package pt.isec.pa.apoio_poe.ui.text;

import java.io.*;
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
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("teste.csv"));
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
        Scanner sc = new Scanner(br);
        sc.useDelimiter(",");
        String line = " ";
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()){
            line = sc.nextLine();
            sb.append(line).append("\n");
        }
        System.out.println(sb.toString());

    }
}
