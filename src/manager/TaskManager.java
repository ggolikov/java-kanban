package manager;

import java.util.ArrayList;
import java.util.List;

import model.Task;
import model.Subtask;
import model.Epic;

public interface TaskManager {
    int getTaskId();

    ArrayList<Task> getTasks();

    void clearTasks();

    Task getTask(int id);

    void addTask(Task task);

    void updateTask(Task task);

    void removeTask(int id);

    ArrayList<Subtask> getSubtasks();

    void clearSubtasks();

    Subtask getSubtask(int id);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    ArrayList<Epic> getEpics();

    void clearEpics();

    Epic getEpic(int id);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpic(int id);

    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    void setEpicDuration(Epic epic);

    ArrayList<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean arwTasksOverlapping(Task task1, Task task2);

    boolean isTaskOverlapping(Task task);

}
