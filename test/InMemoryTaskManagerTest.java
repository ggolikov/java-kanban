import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import manager.Managers;
import manager.TaskManager;
import model.Task;
import model.Subtask;
import model.Epic;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("Task 1", "Task 1", now, duration);
        Task task2 = new Task("Task 2", "Task 2", now, duration);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        task2.setId(task1.getId());

        assertEquals(task1, task2);
    }

    @Test
    void subTasksWithSameIdShouldBeEqual() {
        Epic epic = new Epic("Epic 1", "Epic 1");
        epic.setId(1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2", epic.getId());

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask2.setId(subtask1.getId());

        assertEquals(subtask1, subtask2);
    }

    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        Epic epic2 = new Epic("Epic 2", "Epic 2");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        epic2.setId(epic1.getId());

        assertEquals(epic1, epic2);
    }

    @Test
    void firstAddedTaskShouldBeBoundById() {
        Task task1 = new Task("Task 1", "Task 1");
        taskManager.addTask(task1);

        assertEquals(task1, taskManager.getTask(1));
    }

    @Test
    void firstAddedEpicShouldBeBoundById() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        taskManager.addEpic(epic1);

        assertEquals(epic1, taskManager.getEpic(1));
    }
}