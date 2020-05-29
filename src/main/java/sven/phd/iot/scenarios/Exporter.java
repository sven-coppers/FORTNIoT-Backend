package sven.phd.iot.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.PredictionsRequest;
import sven.phd.iot.predictions.PredictionEngine;
import sven.phd.iot.students.bram.resources.DeviceResource;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Exporter {
    public static void export(String question) {
        ObjectMapper objectMapper = new ObjectMapper();
        ContextManager cm = ContextManager.getInstance();
        PredictionEngine pe = cm.getPredictionEngine();
        String preset = cm.getScenarioManager().getPreset();
        String caseName = (cm.getPredictionEngine().isPredicting() ? "a_" : "b_") + preset;
        //String outputFolderName = "C:\\Users\\scoppers\\Repositories\\iot-vis\\cache\\" + caseName + "\\";
        String outputFolderName = "C:\\Users\\scoppers\\Repositories\\iot-vis\\cache\\";

        try {
            System.out.println("Exporting '" + caseName + "'to " + outputFolderName);
            File outputFolder = new File(outputFolderName);
            outputFolder.mkdirs();

            System.out.println(" - Settings...");
            PredictionsRequest settings = new PredictionsRequest(pe.isPredicting(), pe.getTickRate(), pe.getPredictionWindow(), preset, question);
            PrintWriter settingsWritter = new PrintWriter(outputFolderName + "settings.json", "UTF-8");
            settingsWritter.write(objectMapper.writeValueAsString(settings));
            settingsWritter.close();

            System.out.println(" - Rules...");
            PrintWriter ruleWriter = new PrintWriter(outputFolderName + "rules.json", "UTF-8");
            ruleWriter.write(objectMapper.writeValueAsString(cm.getRules()));
            ruleWriter.close();

            System.out.println(" - Rules history...");
            PrintWriter ruleHistoryWriter = new PrintWriter(outputFolderName + "rules_history.json", "UTF-8");
            ruleHistoryWriter.write(objectMapper.writeValueAsString(cm.getPastRuleExecutions()));
            ruleHistoryWriter.close();

            System.out.println(" - Rules future...");
            PrintWriter ruleFutureWriter = new PrintWriter(outputFolderName + "rules_future.json", "UTF-8");
            ruleFutureWriter.write(objectMapper.writeValueAsString(cm.getFutureRuleExecutions()));
            ruleFutureWriter.close();

            System.out.println(" - Devices...");
            PrintWriter deviceWriter = new PrintWriter(outputFolderName + "devices.json", "UTF-8");
            deviceWriter.write(DeviceResource.getAllDevices());
            deviceWriter.close();

            System.out.println(" - Devices history ...");
            PrintWriter deviceHistoryWriter = new PrintWriter(outputFolderName + "states_history.json", "UTF-8");
            deviceHistoryWriter.write(objectMapper.writeValueAsString(cm.getStateHistory()));
            deviceHistoryWriter.close();

            System.out.println(" - Devices future ...");
            PrintWriter deviceFutureWriter = new PrintWriter(outputFolderName + "states_future.json", "UTF-8");
            deviceFutureWriter.write(objectMapper.writeValueAsString(cm.getStateFuture()));
            deviceFutureWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
