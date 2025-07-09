package util;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class FastLinkedList {
    private final LinkedList<Node<Task>> tasks = new LinkedList<>();
    private final HashMap<Integer, Node<Task>> tasksMap = new HashMap<>();

    public void linkLast(Task task) {
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