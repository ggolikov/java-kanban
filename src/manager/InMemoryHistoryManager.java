package manager;

import java.util.ArrayList;

import util.FastLinkedList;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final FastLinkedList history = new FastLinkedList();

    @Override
    public void add(Task task) {
        System.out.println(task);
        if (task != null) {
            history.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        history.removeTask(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }
}
