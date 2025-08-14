package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    protected LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, LocalDateTime startTime, Duration duration, TaskStatus status) {
        super(name, description, startTime, duration);
        this.type = TaskType.EPIC;
        this.status = status;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void removeSubtask(int id) {
        int index = subtasks.indexOf(id);
        this.subtasks.remove(index);
    }

    public void clearSubtasks() {
        subtasks.clear();
        this.startTime = LocalDateTime.now();
        this.endTime = this.startTime;
    }
}
