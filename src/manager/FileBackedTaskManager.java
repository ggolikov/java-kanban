package manager;

import model.*;

import java.io.FileWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import java.io.File;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    File file;

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(historyManager, file);
        try {
            String fileContent = Files.readString(file.toPath());

            String[] lines = fileContent.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = CsvParser.fromString(lines[i], manager);

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
            throw new ManagerSaveException("Save exception");
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

    public void save() throws ManagerSaveException {
        try {
            Writer fileWriter = new FileWriter(file.getName());
            String header = "id,type,name,status,description,epic\n";
            fileWriter.write(header);

            for (Task task : tasks.values()) {
                fileWriter.write(CsvParser.toString(task, epics));
            }

            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(CsvParser.toString(subtask, epics));
            }

            for (Epic epic : epics.values()) {
                fileWriter.write(CsvParser.toString(epic, epics));
            }

            fileWriter.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Save exception");
        }
    }
}
