package manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

import model.Node;
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

class FastLinkedList {
    private final LinkedList<Node<Task>> tasks = new LinkedList<>();
    private final HashMap<Integer, Node<Task>> tasksMap = new HashMap<>();

    public void linkLast(Task task) {
        // existing
        int id = task.getId();
        removeTask(id);
        Node<Task> node = new Node<>(task);

        tasksMap.put(task.getId(), node);
        tasks.addLast(node);
    }

    public void removeTask(int id) {
        if (tasksMap.containsKey(id)) {
            Node<Task> existingNode = tasksMap.get(id);
            tasksMap.remove(id);

            tasks.remove(existingNode);
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();

        for (Node<Task> node : tasks) {
            list.add(node.data);
        }

        return list;
    }
}
