package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.TaskType;

import java.io.IOException;

public class SubtaskHandler extends CommonTaskHandler implements HttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();

        switch (method) {
            case "GET" -> handleGetEntity(h, TaskType.SUBTASK);
            case "POST" -> handlePostEntity(h, TaskType.SUBTASK);
            case "DELETE" -> handleDeleteEntity(h, TaskType.SUBTASK);
        }
    }
}
