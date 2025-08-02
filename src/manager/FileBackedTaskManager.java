package manager;

import model.*;

import java.io.FileWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import java.io.File;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    File file;

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(historyManager, file);
        try {
            String fileContent = Files.readString(file.toPath());

            String[] lines = fileContent.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = manager.fromString(lines[i]);

                switch (task.getType()) {
                    case TASK:
                        manager.addTask(task);
                        break;
                    case SUBTASK:
                        Subtask subtask = (Subtask) task;
                        manager.addTask(subtask);
                        break;
                    case EPIC:
                        Epic epic = (Epic) task;
                        manager.addEpic(epic);
                        break;
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException();
        }

        return manager;
    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);

        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    public void save() {
        try {
            Writer fileWriter = new FileWriter(file.getName());
            String header = "id,type,name,status,description,epic\n";
            fileWriter.write(header);

            for (Task task : tasks.values()) {
                fileWriter.write(toString(task));
            }

            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(toString(subtask));
            }

            for (Epic epic : epics.values()) {
                fileWriter.write(toString(epic));
            }

            fileWriter.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String toString(Task task) {
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

    public Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType taskType = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        int epicId = -1;
        try {
            String epicName = split[5];
            Optional<Epic> optionalEpic = epics.values().stream().filter(epic -> epic.getName().equals(epicName)).findFirst();

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
}
