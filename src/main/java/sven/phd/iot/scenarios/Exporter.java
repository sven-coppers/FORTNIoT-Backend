package sven.phd.iot.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.PredictionsRequest;
import sven.phd.iot.api.resources.DeviceResource;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.predictions.PredictionEngine;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Exporter {
    public static void export() {
        ObjectMapper objectMapper = new ObjectMapper();
        ContextManager cm = ContextManager.getInstance();
        PredictionEngine pe = cm.getPredictionEngine();
        String preset = cm.getScenarioManager().getActivePreset();
       // String caseName = (cm.getPredictionEngine().isPredicting() ? "a_" : "b_") + preset;
       // String caseName = (cm.getPredictionEngine().isPredicting() ? "a_" : "b_") + preset;
        String caseName = "training";

        if(preset.contains("tr")) {
            caseName = "training";
        } else if(preset.contains("uc1")) {
            caseName = "television";
        } else if(preset.contains("uc2")) {
            caseName = "temperature";
        } else if(preset.contains("uc3")) {
            caseName = "weather";
        } else if(preset.contains("uc4")) {
            caseName = "security";
        } else {
            caseName = "conflicts";
        }

        String outputFolderName = "C:\\Users\\scoppers\\Repositories\\iot_vis\\cache\\" + caseName + "\\";
      //  String outputFolderName = "C:\\Users\\scoppers\\Repositories\\iot_vis-vis\\cache\\";

        try {
            System.out.println("Exporting '" + caseName + "'to " + outputFolderName);
            File outputFolder = new File(outputFolderName);
            outputFolder.mkdirs();

           /* System.out.println(" - Settings...");
            PredictionsRequest settings = new PredictionsRequest(pe.isPredicting(), pe.getTickRate(), pe.getPredictionWindow(), preset, question);
            PrintWriter settingsWritter = new PrintWriter(outputFolderName + "settings.json", "UTF-8");
            settingsWritter.write(objectMapper.writeValueAsString(settings));
            settingsWritter.close(); */

            System.out.println(" - Rules...");
            PrintWriter ruleWriter = new PrintWriter(outputFolderName + "rules.json", "UTF-8");
            ruleWriter.write(objectMapper.writeValueAsString(cm.getRules()));
            ruleWriter.close();

            System.out.println(" - Rules history...");
            PrintWriter ruleHistoryWriter = new PrintWriter(outputFolderName + "rules_history.json", "UTF-8");
            ruleHistoryWriter.write(objectMapper.writeValueAsString(cm.getPastRuleExecutions()));
            ruleHistoryWriter.close();

            System.out.println(" - Devices...");
            PrintWriter deviceWriter = new PrintWriter(outputFolderName + "devices.json", "UTF-8");
            deviceWriter.write(objectMapper.writeValueAsString(cm.getHassioDeviceManager().getDevices()));
            deviceWriter.close();

            System.out.println(" - Devices history ...");
            PrintWriter deviceHistoryWriter = new PrintWriter(outputFolderName + "states_history.json", "UTF-8");
            deviceHistoryWriter.write(objectMapper.writeValueAsString(cm.getStateHistory()));
            deviceHistoryWriter.close();

            System.out.println(" - Devices future ...");
            PrintWriter deviceFutureWriter = new PrintWriter(outputFolderName + "future.json", "UTF-8");
            deviceFutureWriter.write(objectMapper.writeValueAsString(cm.getFuture()));
            deviceFutureWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportAlternativeFuture(Future alternativeFuture) {
        ObjectMapper objectMapper = new ObjectMapper();
        ContextManager cm = ContextManager.getInstance();
        PredictionEngine pe = cm.getPredictionEngine();
        String preset = cm.getScenarioManager().getActivePreset();
        // String caseName = (cm.getPredictionEngine().isPredicting() ? "a_" : "b_") + preset;
        // String caseName = (cm.getPredictionEngine().isPredicting() ? "a_" : "b_") + preset;
        String caseName = "training";

        if(preset.contains("tr")) {
            caseName = "training";
        } else if(preset.contains("uc1")) {
            caseName = "television";
        } else if(preset.contains("uc2")) {
            caseName = "temperature";
        } else if(preset.contains("uc3")) {
            caseName = "weather";
        } else if(preset.contains("uc4")) {
            caseName = "security";
        } else {
            caseName = "conflicts";
        }

        String outputFolderName = "C:\\Users\\scoppers\\Repositories\\iot_vis\\cache\\" + caseName + "\\";
        String filename = "future_";
        boolean first = true;

        for(RuleExecution ruleExecution : alternativeFuture.getFutureExecutions()) {
            for(ActionExecution actionExecution : ruleExecution.getActionExecutions()) {
                if(!first) filename += ";";

                filename += actionExecution.getActionExecutionID().replace("action_execution_id_", "") + (actionExecution.snoozed ? "t" : "f");
                first = false;
            }
        }

        try {
            System.out.println(" - Alternative future ...");
            PrintWriter deviceFutureWriter = new PrintWriter(outputFolderName + filename + ".json", "UTF-8");
            deviceFutureWriter.write(objectMapper.writeValueAsString(cm.getFuture()));
            deviceFutureWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
