package manager;

import model.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1 == null && task2 == null) {
            return 0;
        } else if (task1 == null) {
            return -1;
        } else if (task2 == null) {
            return 1;
        } else {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    }
}