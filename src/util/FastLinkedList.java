package util;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class FastLinkedList {
    private final HashMap<Integer, Node<Task>> tasksMap = new HashMap<>();
    private Node<Task> head = null;
    private Node<Task> last = null;

    public void linkLast(Task task) {
        int id = task.getId();
        removeTask(id);
        Node<Task> node = new Node<>(task);

        if (tasksMap.isEmpty()) {
            head = node;
        }

        if (last != null) {
            node.prev = last;
            last.next = node;
        }

        last = node;

        tasksMap.put(task.getId(), node);
    }

    public void removeTask(int id) {
        if (tasksMap.containsKey(id)) {
            Node<Task> existingNode = tasksMap.get(id);

            Node<Task> prev = existingNode.prev;
            Node<Task> next = existingNode.next;

            if (prev != null) {
                if (next != null) {
                    prev.next = next;
                } else {
                    last = prev;
                    prev.next = null;
                }
            } else {
                head = next;
                next.prev = null;
            }

            tasksMap.remove(id);
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();

        for (Node<Task> node : tasksMap.values()) {
            list.add(node.data);
        }

        return list;
    }
}