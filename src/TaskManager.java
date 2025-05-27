import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskCount = 0;

    private int getTaskId() {
        return ++taskCount;
    }

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(getTaskId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            ArrayList<Subtask> epicSubtasks = getEpicSubtasks(epic);

            if (!epicSubtasks.isEmpty()) {
                epic.clearSubtasks();
            }

            updateEpicAfterSubtasksChange(epic);
        }
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void addSubtask(Subtask subtask) {
        Epic subtaskEpic = epics.get(subtask.getEpicId());

        if (subtaskEpic != null) {
            subtask.setId(getTaskId());
            subtasks.put(subtask.getId(), subtask);
            subtaskEpic.getSubtasks().add(subtask.getId());
            updateEpicAfterSubtasksChange(subtaskEpic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic subtaskEpic = epics.get(subtask.getEpicId());

            if (subtaskEpic != null) {
                updateEpicAfterSubtasksChange(subtaskEpic);
            }
        }
    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic subtaskEpic = epics.get(subtask.getEpicId());

            if (subtaskEpic != null) {
                updateEpicAfterSubtasksChange(subtaskEpic);
            }
        }
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void addEpic(Epic epic) {
        epic.setId(getTaskId());
        epics.put(epic.getId(), epic);
    }

    private void updateEpicAfterSubtasksChange(Epic epic) {
        TaskStatus epicSubtasksStatus = getSubtasksStatus(getEpicSubtasks(epic));
        Epic updatedEpic = new Epic(epic.getName(), epic.getDescription(), epicSubtasksStatus);
        updatedEpic.setId(epic.getId());
        updatedEpic.setSubtasks(epic.getSubtasks());

        updateEpic(updatedEpic);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void removeEpic(int id) {
        Epic epic = epics.get(id);

        for (int subtaskId : epic.getSubtasks()) {
            removeSubtask(subtaskId);
        }
        epics.remove(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (int epicSubtaskId : epic.getSubtasks()) {
            Subtask subtask = subtasks.get(epicSubtaskId);
            if (subtask != null) {
                epicSubtasks.add(subtask);
            }
        }

        return epicSubtasks;
    }

    public TaskStatus getSubtasksStatus(ArrayList<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            return TaskStatus.NEW;
        }

        int newStatusCount = 0;
        int doneStatusCount = 0;

        for (Subtask subtask : subtasks) {
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
}
