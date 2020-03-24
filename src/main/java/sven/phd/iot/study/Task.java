package sven.phd.iot.study;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    @JsonProperty("task_id") public String taskID;
    @JsonProperty("completed") public boolean completed;
    @JsonProperty("ongoing") public boolean ongoing;
    @JsonProperty("for_participant") public boolean forParticipant;
    @JsonProperty("instruction") public String instruction;
    @JsonProperty("url") public String url;
    @JsonProperty("preset") public String preset;

    public Task(String taskID, boolean forParticipant, String instruction, String url, String preset) {
        this.taskID = taskID;
        this.completed = false;
        this.forParticipant = forParticipant;
        this.instruction = instruction;
        this.url = url;
        this.preset = preset;
    }
}
