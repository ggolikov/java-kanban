import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer httpServer;

    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = Managers.getDefault();

        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new TaskHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
            httpServer.createContext("/epics", new EpicHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
            start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start() {
        if (httpServer != null) {
            httpServer.start();
            System.out.println("HTTP Server started");
        }
    }
}
