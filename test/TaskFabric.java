import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskFabric {
    static Task createTask() {
        return new Task("Test 1", "Testing task 1", LocalDateTime.now(), Duration.ofMinutes(5), TaskStatus.NEW);
    }

    static Subtask createSubtask(int epicId) {
        return new Subtask("Test 1", "Testing task 1", epicId, LocalDateTime.now(), Duration.ofMinutes(5), TaskStatus.NEW);
    }

    static Epic createEpic() {
        Epic epic = new Epic("Test 1", "Testing epic 1", LocalDateTime.now(), Duration.ofMinutes(5), TaskStatus.NEW);
        epic.setEndTime(LocalDateTime.now());

        return epic;
    }
}
