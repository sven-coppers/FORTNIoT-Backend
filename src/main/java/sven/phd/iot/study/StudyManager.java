package sven.phd.iot.study;

import sven.phd.iot.ContextManager;
import sven.phd.iot.scenarios.Exporter;

import java.util.*;

public class StudyManager {
    private static String SURVEY_INFORMED_CONSENT = "https://docs.google.com/forms/d/e/1FAIpQLSdx4zu-15b8tG7kr7p1xO2Gs5dmpD5vqS_-OJ6r_sm8kUkUvA/viewform?entry.1409557049=";
    private static String SURVEY_DEMOGRAPHY = "https://docs.google.com/forms/d/e/1FAIpQLSd_PQYBxpwPbGIsi_Lo8RBlPUUiA_Dq-T6rDUM2tmr8-ciVuQ/viewform?usp=pp_url&entry.1409557049=";
    private static String SURVEY_WHAT = "https://docs.google.com/forms/d/e/1FAIpQLSdAb_UyUtclY6BN4VqOVfCGdw2yp70TOk_zttVyk4VYIZakWg/viewform?usp=pp_url&entry.1409557049=";
    private static String SURVEY_TRUST = "https://docs.google.com/forms/d/e/1FAIpQLSfs0sTrEZTTm3aFRHxdFvcz1JJOWJWRsZnjOyxFOkay9dEkjQ/viewform?usp=pp_url&entry.1409557049=";
    private static String SURVEY_USE_CASE_END = "https://docs.google.com/forms/d/e/1FAIpQLSdeYvKCC1OFaABr5HBzgkZ6I0hRiGMOpO1v97C2GmXZ9SHilQ/viewform?usp=pp_url&entry.1409557049=";
    private static String SURVEY_CLOSING = "https://docs.google.com/forms/d/e/1FAIpQLSfRnR_ScUBngixG9RdMUzrAuXluqFhewcNS25bHc39Hzc5QJQ/viewform?usp=pp_url&entry.1409557049=";
    private static String TRAINING_A = "https://drive.google.com/file/d/18An-8zEWz5qkSdq9UAFYQCO-N0_XdxQ5/view?usp=sharing";
    private static String TRAINING_B = "https://drive.google.com/file/d/188kjcFIKB61qDbLjH22ysdzv0zbqlafR/view?usp=sharing";
    private static String PROTOTYPE = "https://research.edm.uhasselt.be/~scoppers/iot/prototype.php";

    private List<Task> tasks;
    private HashMap<String, String> timings;
    private Date lastStart;

    public StudyManager() {
        this.tasks = new ArrayList<>();
        this.timings = new HashMap<>();
    }

    public void start(String participantID, String group) {
        this.tasks.clear();
        this.timings.clear();

        this.timings.put("group", group);
        this.timings.put("participant", participantID);

        this.tasks.add(new Task("vpn", false, "Start the VPN", null, null));
        this.tasks.add(new Task("sync", false, "Enable syncing the cache in WIN_SCP", null, null));
        this.tasks.add(new Task("engine", false, (group.equals("A")? "Start" : "Stop") + " the prediction engine", null, null));
        this.tasks.add(new Task("intro",false, "In this experiment, we ask questions about the behavior of fictive smart homes. You will use our prototype visualizations to come up with answers to these questions. ", null, null));
        this.tasks.add(new Task("consent",true, "Fill in the informed consent", SURVEY_INFORMED_CONSENT + participantID, null));
        this.tasks.add(new Task("demographics",true, "Fill in the demographics", getSurveyDemography(participantID, group), null));
        this.tasks.add(new Task("training",true, "Read the training document", group.equals("A")? TRAINING_A : TRAINING_B, null));
        this.tasks.add(new Task("prototype",true, "Share the prototype", PROTOTYPE, "tr_v1"));

        // Training
        this.tasks.add(new Task("tr_what", false, "What will happen?", getWhatSurvey(participantID, group, "tr"), null));
        for(int i = 1; i <= 4; i++) {
            this.tasks.add(new Task("tr_v" + i, false, "Trust question " + i, getSurveyTrust(participantID, group, "tr", "v" + i), "tr_v" + i));
        }

        // Full use cases
        List<Integer> useCases = new ArrayList<>();

        for(int i = 1; i <= 4; i++) {
            useCases.add(i);
        }

        Collections.shuffle(useCases);

        for(int i = 0; i < useCases.size(); i++) {
            String useCase = "uc" + useCases.get(i);

            List<Integer> trustQuestions = new ArrayList<>();
            trustQuestions.addAll(useCases);
            Collections.shuffle(trustQuestions);
            String preset = useCase + "_v1";

            this.tasks.add(new Task(useCase + "_what", false, "What will happen?", getWhatSurvey(participantID, group, useCase), preset));

            for(int j = 0; j < trustQuestions.size(); j++) {
                String question = "v" + trustQuestions.get(j);
                preset = useCase + "_" + question;

                this.tasks.add(new Task(useCase + "_" + question, false, "Trust question " + trustQuestions.get(j), getSurveyTrust(participantID, group, useCase, question), preset));
            }

            this.tasks.add(new Task(useCase + "_closing", true, "Use Case Closing", getUseCaseEnd(participantID, group, useCase), null));
        }

        this.tasks.add(new Task("closing", true, "Fill in the closing survey", getClosing(participantID, group), null));

        this.tasks.get(0).ongoing = true;
        this.lastStart = new Date();
    }

    private String getSurveyDemography(String participant, String group) {
        return SURVEY_DEMOGRAPHY + participant + "&entry.1889097042=" + group;
    }

    private String getWhatSurvey(String participant, String group, String useCase) {
        return SURVEY_WHAT + participant + "&entry.574035086=" + group + "&entry.1542649745=" + useCase;
    }

    private String getSurveyTrust(String participant, String group, String useCase, String question) {
        return SURVEY_TRUST + participant + "&entry.574035086=" + group + "&entry.1542649745=" + useCase + "&entry.642607458=" + question;
    }

    private String getUseCaseEnd(String participant, String group, String useCase) {
        return SURVEY_USE_CASE_END + participant + "&entry.1992576792=" + group + "&entry.519341768=" + useCase;
    }

    private String getClosing(String participant, String group) {
        return SURVEY_CLOSING + participant + "&entry.720023718=" + group;
    }

    public void next() {
        for(int i = 0; i < this.tasks.size(); i++) {
            Task task = this.tasks.get(i);

            if(task.ongoing) {
                // Modify old task
                task.completed = true;
                task.ongoing = false;

                // LOG TIME
                long timePassed = new Date().getTime() - lastStart.getTime();
                timings.put(task.taskID, "" + timePassed);

                // Set new task
                if(i < this.tasks.size() - 1) {
                    this.tasks.get(i + 1).ongoing = true;
                    lastStart = new Date();

                    if(this.tasks.get(i + 1).preset != null) {
                        ContextManager.getInstance().getScenarioManager().setPreset(this.tasks.get(i + 1).preset);
                        ContextManager.getInstance().getPredictionEngine().updateFuturePredictions();
                        Exporter.export(this.getQuestion(this.tasks.get(i + 1).taskID));
                    }
                } else { // There are no new tasks
                    LogWriter.write(timings);
                }

                break;
            }
        }
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public String getQuestion(String taskID) {
        HashMap<String, String> questions = new HashMap<>();
        questions.put("tr_what", "What will happen? Why?");
        questions.put("tr_v1", "The lights turn off when the sun rises?");
        questions.put("tr_v2", "The lights turn on when smoke is detected");
        questions.put("tr_v3", "The lights turn on when the sun sets, but only when somebody is home");
        questions.put("tr_v4", "The lights turn off when there is no longer smoke detected");
        questions.put("uc1_what", "What will happen? Why?");
        questions.put("uc1_v1", "The TV will turn on when the news starts and somebody is home");
        questions.put("uc1_v2", "The led strip turns on when the superbowl starts");
        questions.put("uc1_v3", "All lights turn off when Star Wars starts ");
        questions.put("uc1_v4", "The led strip turns on when the superbowl starts");
        questions.put("uc2_what", "What will happen? Why?");
        questions.put("uc2_v1", "The floor heating will start heating in the morning");
        questions.put("uc2_v2", "The heating will turn to eco when the target temperature has been reached");
        questions.put("uc2_v3", "The airco will turn on when the target temperature is lower than the measured temperature");
        questions.put("uc2_v4", "The floor heating will turn on earlier to anticipate target temperature ");
        questions.put("uc3_what", "What will happen? Why?");
        questions.put("uc3_v1", "The rolling shutter will raise at sun rise, even when nobody is home");
        questions.put("uc3_v2", "The rolling shutter lowers when the wind speed reaches 55KM/H");
        questions.put("uc3_v3", "All lights will turn off when the sun rises");
        questions.put("uc3_v4", "The rolling shutter will raise when the wind speed drops below 55KM/H");
        questions.put("uc4_what", "What will happen? Why?");
        questions.put("uc4_v1", "The front door remains unlocked when somebody leaves, but there still is somebody home");
        questions.put("uc4_v2", "All doors unlock when smoke is detected");
        questions.put("uc4_v3", "All doors unlock when routine = sleeping ends");
        questions.put("uc4_v4", "All doors are locked as long as everyone is sleeping");

        return questions.get(taskID);
    }
}
