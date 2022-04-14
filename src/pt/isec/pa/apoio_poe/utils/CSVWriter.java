package pt.isec.pa.apoio_poe.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static boolean writeLine(String delimiter, Boolean breakLine, Object... args){
        if(pw == null)
            return false;
        int index = 0;
        String line = "";
        for (Object o : args){
            if(o instanceof Boolean b){
                line = line.concat((b ? "true" : "false"));
            }
            else if (o instanceof String s){
                line = line.concat(s);
            }
            else if (o instanceof Double d){
                line = line.concat(String.valueOf(d));
            }
            else if (o instanceof Integer i){
                line = line.concat(String.valueOf(i));
            }
            else if (o instanceof Float f){
                line = line.concat(String.valueOf(f));
            }
            else if (o instanceof Long l){
                line = line.concat(String.valueOf(l));
            }
            else if(o instanceof List<?> array){
                for (var a : array){
                    if(a instanceof String s){
                        if(delimiter != null){
                            line = line.concat(s+delimiter);
                        }
                    }
                }
            }
            else{
                continue;
            }
            if(delimiter != null)
                line = line.concat(delimiter);
        }

        if(breakLine) {
            while(delimiter != null && delimiter.length() > 0 && line.charAt(line.length()-1) == ','){
                line = line.substring(0, line.length()-1);
            }
            pw.println(line);
        }
        else
            pw.print(line);

        return true;
    }


    public static boolean closeFile(){
        if(pw == null)
            return false;
        pw.close();
        return true;
    }
}
