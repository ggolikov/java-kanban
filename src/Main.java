import http.HttpTaskServer;
import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);

        taskServer.start();
    }
}
