import manager.Managers;
import manager.TaskManager;
import model.Task;
import model.Subtask;
import model.Epic;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Task 1", "Task 1");
        Task task2 = new Task("Task 2", "Task 2");
        Epic epic1  = new Epic("Epic 1", "Epic 1");
        Epic epic2  = new Epic("Epic 2", "Epic 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1", epic1.getId());

        taskManager.addSubtask(subtask1);

        /* HISTORY */
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        taskManager.getSubtask(subtask1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        // taskManager.getSubtask(subtask1.getId());


        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
