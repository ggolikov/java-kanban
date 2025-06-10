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
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2", epic2.getId());
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3", epic2.getId());

        Task taskToUpdate = new Task(task1.getName(), "Task 1 Updated", TaskStatus.IN_PROGRESS);
        taskToUpdate.setId(task1.getId());
        taskManager.updateTask(taskToUpdate);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Subtask updatedSubtask1 = new Subtask("Subtask UPD", "Subtask UPD", epic1.getId(), TaskStatus.IN_PROGRESS);
        updatedSubtask1.setId(subtask1.getId());
        updatedSubtask1.setEpicId(epic1.getId());
        taskManager.updateSubtask(updatedSubtask1);
        Subtask updatedSubtask2 = new Subtask("Subtask UPD2", "Subtask UPD2", epic1.getId(), TaskStatus.DONE);
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setEpicId(epic1.getId());
        taskManager.updateSubtask(updatedSubtask2);
        Subtask updatedSubtask3 = new Subtask("Subtask UPD2", "Subtask UPD2", epic2.getId(), TaskStatus.IN_PROGRESS);
        updatedSubtask3.setId(subtask3.getId());
        updatedSubtask3.setEpicId(epic2.getId());
        taskManager.updateSubtask(updatedSubtask3);
        /* HISTORY */
        Task t1 = taskManager.getTask(task1.getId());
        Task t2 = taskManager.getTask(task2.getId());
        Subtask t3 = taskManager.getSubtask(subtask1.getId());
        Subtask t4 = taskManager.getSubtask(subtask2.getId());


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
