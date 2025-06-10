package manager;

import java.util.ArrayList;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_HISTORY_SIZE = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() >= MAX_HISTORY_SIZE) {
                history.removeFirst();
            }
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
