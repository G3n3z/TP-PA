package pt.isec.pa.apoio_poe.utils;

import java.io.*;

public class CSVWriter {

    private static BufferedReader bw;
    private static PrintWriter pw;

    public static boolean startWriter(String file){

        try{
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        } catch (IOException e) {
            if(pw != null){
                pw.close();
            }
            return false;
        }
        return true;
    }

    public static boolean writeLine(String delimiter, Object... args){
        if(pw == null)
            return false;
        int index = 0;
        String line = "";
        for (Object o : args){
            if(o instanceof Boolean b){
                line = line.concat((b ? "true" : "false"));
            }

            line = line.concat(delimiter);
        }
        return true;
    }
}
