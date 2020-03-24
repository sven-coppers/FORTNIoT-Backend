package sven.phd.iot.study;

import java.io.*;
import java.util.HashMap;

public class LogWriter {
    private static String outputFolderName = "C:\\Users\\scoppers\\Repositories\\iot-vis\\logs\\";

    public static void write(HashMap<String, String> values) {
        PrintWriter writer;

        try {
            File file = new File(outputFolderName + "time_logs.csv");

            if(!file.exists()) {
                writer = new PrintWriter(new FileOutputStream(file, false));
                exportHeader(writer, values);
            } else {
                writer = new PrintWriter(new FileOutputStream(file, true));
            }

            exportParticipant(writer, values);

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void exportHeader(PrintWriter writer, HashMap<String, String> values) {
        for(String key : values.keySet()) {
            writer.print(key + ";");
        }

        writer.println("empty");
    }

    private static void exportParticipant(PrintWriter writer, HashMap<String, String> values) {
        for(String key : values.keySet()) {
            writer.print(values.get(key) + ";");
        }

        writer.println("empty");
    }
}