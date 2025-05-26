import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public TaskStatus getSubtasksStatus(HashMap<Integer, Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            return TaskStatus.NEW;
        }

        int newStatusCount = 0;
        int doneStatusCount = 0;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                newStatusCount++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                doneStatusCount++;
            }
        }

        if (newStatusCount == subtasks.size()) {
            return TaskStatus.NEW;
        } else if (doneStatusCount == subtasks.size()) {
            return TaskStatus.DONE;
        }

        return TaskStatus.IN_PROGRESS;
    }
};
