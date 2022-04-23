package pt.isec.pa.apoio_poe.utils;

import java.io.*;

import java.util.List;


public class CSVWriter {

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


    public static boolean writeLine(String delimiter, Boolean breakLine, Boolean usePreviousDelimiter, Object... args){
        if(pw == null)
            return false;
        int index = 0;
        String line = "";
        for (Object o : args){
            index++;
            line = line.concat(CSVWriter.parseObject(o,delimiter));
            if(delimiter != null && index < args.length)
                line = line.concat(delimiter);
        }

        if (usePreviousDelimiter){
            line = delimiter + line;
        }
        if(breakLine) {
            pw.println(line);
        }
        else {
            pw.print(line);
            return true;
        }
        return true;
    }

    private static String parseObject(Object o, String delimiter) {

        if (o instanceof Boolean b) {
            return (b ? "true" : "false");
        } else if (o instanceof String s) {
            return s;
        } else if (o instanceof Double d) {
            return String.valueOf(d);
        } else if (o instanceof Integer i) {
            return String.valueOf(i);
        } else if (o instanceof Float f) {
            return String.valueOf(f);
        } else if (o instanceof Long l) {
            return String.valueOf(l);
        } else if (o instanceof List<?> array) {
            String line = "";
            for (var a : array) {
                if (a instanceof String s) {
                    if (delimiter != null) {
                        line = line.concat(s + delimiter);
                    }
                }
            }
            return line;
        }
        return "";
    }
    public static boolean closeFile(){
        if(pw == null)
            return false;
        pw.close();
        return true;
    }
}
