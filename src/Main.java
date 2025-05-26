public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task 1", "Task 1");
        Task task2 = new Task("Task 2", "Task 2");
        Epic epic1  = new Epic("Epic 1", "Epic 1");
        Epic epic2  = new Epic("Epic 2", "Epic 2");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1");
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2");
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Task taskToUpdate = new Task(task1.getName(), "Task 1 Updated", TaskStatus.IN_PROGRESS);
        taskToUpdate.setId(task1.getId());
        taskManager.updateTask(taskToUpdate);
        taskManager.removeTask(task2.getId());
        taskManager.addSubtask(subtask1, epic1);
        taskManager.addSubtask(subtask2, epic1);
        taskManager.addSubtask(subtask3, epic2);
        Subtask updatedSubtask1 = new Subtask("Subtask UPD", "Subtask UPD", TaskStatus.IN_PROGRESS);
        updatedSubtask1.setId(subtask1.getId());
        updatedSubtask1.setEpic(epic1);
        taskManager.updateSubtask(updatedSubtask1);
        Subtask updatedSubtask2 = new Subtask("Subtask UPD2", "Subtask UPD2", TaskStatus.DONE);
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setEpic(epic1);
        taskManager.updateSubtask(updatedSubtask2);
        taskManager.removeSubtask(subtask1.getId());
        taskManager.removeSubtask(subtask2.getId());
        Subtask updatedSubtask3 = new Subtask("Subtask UPD2", "Subtask UPD2", TaskStatus.IN_PROGRESS);
        updatedSubtask3.setId(subtask3.getId());
        updatedSubtask3.setEpic(epic2);
        taskManager.updateSubtask(updatedSubtask3);

        System.out.println("Tasks: " + taskManager.getTasks().size());
        System.out.println(taskManager.getTasks());
        System.out.println("Epics: " + taskManager.getEpics().size());
        System.out.println(taskManager.getEpics());
        System.out.println("Subtasks " + taskManager.getSubtasks().size());
        System.out.println(taskManager.getSubtasks());
        System.out.println("Task 1");
        System.out.println(taskManager.getTask(task1.getId()));
        System.out.println("Epic 1");
        System.out.println(taskManager.getEpic(epic1.getId()));
        System.out.println(taskManager.getEpicSubtasks(epic1));
        System.out.println("Epic 2");
        System.out.println(taskManager.getEpic(epic2.getId()));
        System.out.println(taskManager.getEpicSubtasks(epic2));
    }
}
