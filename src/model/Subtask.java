package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description) {
        super(name, description);
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, int epicId, Duration duration) {
        this(name, description, epicId);
        this.duration = duration;
    }

    public Subtask(String name, String description, int epicId, LocalDateTime startTime, Duration duration, TaskStatus status) {
        super(name, description, status);
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getEpicId() {
        return this.epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
