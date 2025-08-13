package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.TaskType;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();

        switch (method) {
            case "GET" -> handleGetEntity(h, TaskType.TASK);
            case "POST" -> handlePostEntity(h, TaskType.TASK);
            case "DELETE" -> handleDeleteEntity(h, TaskType.TASK);
        }
    }
}
