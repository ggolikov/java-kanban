import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.Task;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        Task task = new Task("Task1", "Task1");
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
    }
}