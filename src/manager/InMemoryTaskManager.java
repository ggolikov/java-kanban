package manager;

import java.util.ArrayList;
import java.util.HashMap;

import model.Task;
import model.Subtask;
import model.Epic;
import model.TaskStatus;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskCount = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public int getTaskId() {
        return ++taskCount;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }

        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void addTask(Task task) {
        task.setId(getTaskId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            ArrayList<Integer> epicSubtasks = epic.getSubtasks();

            if (!epicSubtasks.isEmpty()) {
                for (Integer subtaskId : epicSubtasks) {
                    historyManager.remove(subtaskId);
                }
                epic.clearSubtasks();
            }

            updateEpicAfterSubtasksChange(epic);
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic subtaskEpic = epics.get(subtask.getEpicId());

        if (subtaskEpic != null) {
            subtask.setId(getTaskId());
            subtasks.put(subtask.getId(), subtask);
            subtaskEpic.getSubtasks().add(subtask.getId());
            updateEpicAfterSubtasksChange(subtaskEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Epic subtaskEpic = epics.get(subtask.getEpicId());

            if (subtaskEpic != null) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicAfterSubtasksChange(subtaskEpic);
            }
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        historyManager.remove(id);
        if (subtask != null) {
            Epic subtaskEpic = epics.get(subtask.getEpicId());

            if (subtaskEpic != null) {
                if (subtaskEpic.getSubtasks().contains(id)) {
                    subtaskEpic.removeSubtask(id);
                    historyManager.remove(id);
                }
                updateEpicAfterSubtasksChange(subtaskEpic);
            }
        }
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }

        epics.clear();

        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getEpicId());
        }

        subtasks.clear();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getTaskId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.get(id);

        for (int subtaskId : epic.getSubtasks()) {
            removeSubtask(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
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

    @Override
    public ArrayList<Task> getHistory() {
        return this.historyManager.getHistory();
    }

    public void updateEpicAfterSubtasksChange(Epic epic) {
        TaskStatus epicSubtasksStatus = getSubtasksStatus(getEpicSubtasks(epic));
        epic.setStatus(epicSubtasksStatus);
        Epic updatedEpic = new Epic(epic.getName(), epic.getDescription(), epicSubtasksStatus);
        updatedEpic.setId(epic.getId());
        updatedEpic.setSubtasks(epic.getSubtasks());

        updateEpic(updatedEpic);
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
