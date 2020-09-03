package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.study.LogWriter;
import sven.phd.iot.study.StudyManager;
import sven.phd.iot.study.Task;

import java.util.*;

public class StudyManagerMathias extends StudyManager {
    private static String SURVEY_INFORMED_CONSENT = "https://docs.google.com/forms/d/e/1FAIpQLSeKl4SjCaOmyAJOVFVZpAuBvgj-SBv_A40HJe54fulgMst2_w/viewform?usp=pp_url&entry.1409557049=";
    private static String SURVEY_DEMOGRAPHY = "https://docs.google.com/forms/d/e/1FAIpQLSc0NNMx7GJorCJS8WiQM8TV7_NyKJx2hj7fvSY3P3NB2BfotA/viewform?usp=pp_url&entry.1409557049=";
    private static String SURVEY_UNDERSTANDING = "https://docs.google.com/forms/d/e/1FAIpQLSdm4mI4n33DvkcavxejRNC2YX4e6FXTV6M4koq57hTPvIv1hg/viewform?usp=pp_url&entry.560708687=";
    private static String SURVEY_OBSERVATION = "https://docs.google.com/forms/d/e/1FAIpQLSftVxSKEt6gmf-9bBI9mWYscN237JE9zyeoZLqilakxXq-zBg/viewform?usp=pp_url&entry.248897562=";
    private static String SURVEY_CLOSING = "https://docs.google.com/forms/d/e/1FAIpQLSdFBvuiIuxCIrVuSDWxB1dmbIceXLmUjgEHU6GpydLx4lWYUA/viewform?usp=pp_url&entry.1671254014=";
    private static String TRAINING = "https://drive.google.com/file/d/1fFOZ1R4sAIfaVNaOqROly2pCT83a1x3H/view?usp=sharing";
    private static String SCENARIO1 = "https://docs.google.com/document/d/1Ya_-dSyxy2nj0hNQb4IDod9d6zY36qMeRhps-Oqw7AY/edit?usp=sharing";
    private static String SCENARIO2 = "https://docs.google.com/document/d/1r7DAK6sZ8BjqmKnI0ZpPbDbXkWPAalZ_Iy1RxYc3skE/edit?usp=sharing";
    private static String SCENARIO3 = "https://docs.google.com/document/d/1posJk6uLQ5aVWaUGB3V33Nv7DW05VmvTLIAWLI1zfrI/edit?usp=sharing";
    private static String SCENARIO4 = "https://docs.google.com/document/d/1tnQOV4fZFau2i6VvrID00YeZifleQ9y4Yq7HzmGcNXA/edit?usp=sharing";
    private static String SCENARIO5 = "https://docs.google.com/document/d/1ihvF05C0s2hpprlYihLH0E7w3YESfNqyzlRBOHXYqWw/edit?usp=sharing";

    private List<Task> tasks;
    private HashMap<String, String> timings;
    private Date lastStart;

    public StudyManagerMathias() {
        this.tasks = new ArrayList<>();
        this.timings = new HashMap<>();
    }

    @Override
    public void start(String participantID, String group) {
        this.tasks.clear();
        this.timings.clear();

        //this.timings.put("group", group);
        this.timings.put("participant", participantID);

        this.tasks.add(new Task("engine", false, "Start the prediction engine", null, null));
        this.tasks.add(new Task("intro",false, "In this experiment, we ask to solve conflicts in fictive smart homes. You will use two different methods for solving the conflicts, one of which will be by using a prototype visualization we created.", null, null));
        this.tasks.add(new Task("consent",true, "Fill in the informed consent", SURVEY_INFORMED_CONSENT + participantID, null));
        this.tasks.add(new Task("demographics",true, "Fill in the demographics", SURVEY_DEMOGRAPHY + participantID, null));
        this.tasks.add(new Task("training",true, "Read the training document", TRAINING, null));
        //this.tasks.add(new Task("prototype",true, "Share the prototype", PROTOTYPE, "tr_v1"));

        // Full use cases
        List<Integer> useCases = new ArrayList<>();

        for(int i = 1; i <= 5; i++) {
            useCases.add(i);
        }

        Collections.shuffle(useCases);

        for(int i = 0; i < useCases.size(); i++) {
            String useCase = "muc" + useCases.get(i);

            List<Integer> methods = new ArrayList<>();
            methods.add(1);
            methods.add(2);
            Collections.shuffle(methods);
            String preset = useCase;

            // Give scenario
            this.tasks.add(new Task(useCase + "_scenario", false, "Present the new scenario", getScenario(useCase), preset));

            // Understand the scenario
            this.tasks.add(new Task(useCase + "_what", false, "Understand the scenario and what is going wrong", getUnderstandingSurvey(participantID, useCase), preset));

            // Start use case
            for (Integer integer : methods) {
                String method = "v" + integer;

                this.tasks.add(new Task(useCase + "_" + method, false, "Method " + integer, getObservationSurvey(participantID, useCase, method), preset));
            }
        }

        this.tasks.add(new Task("closing", true, "Fill in the closing survey", SURVEY_CLOSING + participantID, null));

        this.tasks.get(0).ongoing = true;
        this.lastStart = new Date();
    }

    private String getScenario(String useCase) {
        switch (useCase) {
            case "muc1":
                return SCENARIO1;
            case "muc2":
                return SCENARIO2;
            case "muc3":
                return SCENARIO3;
            case "muc4":
                return SCENARIO4;
            case "muc5":
                return SCENARIO5;
            default:
                break;
        }
        return "";
    }


    private String getUnderstandingSurvey(String participant, String useCase) {
        return  SURVEY_UNDERSTANDING + participant + "&entry.1811671223=" + useCase;
    }

    private String getObservationSurvey(String participant, String useCase, String method) {
        return SURVEY_OBSERVATION + participant + "&entry.1434186355=" + useCase + "&entry.178095170=" + method;
    }

    @Override
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
                        ContextManager.getInstance().getScenarioManager().setActivePreset(this.tasks.get(i + 1).preset);
                        ContextManager.getInstance().getPredictionEngine().updateFuturePredictions();
                        //Exporter.export(this.getQuestion(this.tasks.get(i + 1).taskID));
                    }
                } else { // There are no new tasks
                    LogWriter.write(timings);
                }

                break;
            }
        }
    }

    @Override
    public List<Task> getTasks() {
        return this.tasks;
    }

    @Override
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
