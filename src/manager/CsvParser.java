package manager;

import model.*;

import java.util.HashMap;
import java.util.Optional;

public class CsvParser {
    public static Task fromString(String value, TaskManager manager) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType taskType = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        int epicId = -1;
        try {
            String epicName = split[5];
            Optional<Epic> optionalEpic = manager.getEpics().stream().filter(epic -> epic.getName().equals(epicName)).findFirst();

            if (optionalEpic.isPresent()) {
                epicId = optionalEpic.get().getId();
            }

        } catch (ArrayIndexOutOfBoundsException exception) {
            epicId = 0;
        }

        switch (taskType) {
            case TaskType.TASK: {
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            }
            case TaskType.SUBTASK: {
                Subtask subtask = new Subtask(name, description, epicId, status);
                subtask.setId(id);
                return subtask;
            }
            case TaskType.EPIC: {
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            }
            default: {
                return null;
            }
        }
    }

    public static String toString(Task task, HashMap<Integer, Epic> epics) {
        TaskType type = task.getType();

        switch (type) {
            case TASK, EPIC:
                return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + ",\n";
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                Epic subtaskEpic = epics.get(subtask.getEpicId());
                String epicName = subtaskEpic != null ? subtaskEpic.getName() : "";

                return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," + task.getStatus() + "," + subtask.getDescription() + "," + epicName + "\n";
            default:
                return "";
        }
    }
}
