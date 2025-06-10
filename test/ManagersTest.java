import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import manager.Managers;
import manager.TaskManager;

class ManagersTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }
    @Test
    void createdTaskManagerShouldHaveNoTasks() {
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void createdTaskManagerShouldHaveEmptyHistory() {
        assertEquals(0, taskManager.getHistory().size());
    }
}