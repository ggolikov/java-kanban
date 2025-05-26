import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int taskCount = 0;

    public static int getTaskId() {
        return ++taskCount;
    }

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public TaskManager() {
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void clearTasks() {
        tasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void addSubtask(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        subtask.setEpic(epic);
        this.updateEpicAfterSubtasksChange(subtask.getEpic());
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        this.updateEpicAfterSubtasksChange(subtask.getEpic());
    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        subtasks.remove(id);

        this.updateEpicAfterSubtasksChange(subtask.getEpic());
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void clearEpics() {
        epics.clear();
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void updateEpicAfterSubtasksChange(Epic epic) {
        TaskStatus epicSubtasksStatus = epic.getSubtasksStatus(this.getEpicSubtasks(epic));
        Epic updatedEpic = new Epic(epic.getName(), epic.getDescription(), epicSubtasksStatus);
        updatedEpic.setId(epic.getId());

        this.updateEpic(updatedEpic);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void removeEpic(int id) {
        epics.remove(id);
    }

    public HashMap<Integer, Subtask> getEpicSubtasks(Epic epic) {
        HashMap<Integer, Subtask> epicSubtasks = new HashMap<>();
        for (Subtask subtask : subtasks.values()) {
            Epic subtaskEpic = subtask.getEpic();

            if (subtaskEpic == epic) {
                epicSubtasks.put(subtask.getId(), subtask);
            }
        }
        return epicSubtasks;
    }
}
