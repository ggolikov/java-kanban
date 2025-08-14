package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.TaskType;

import java.io.IOException;
import java.util.ArrayList;

public class EpicHandler extends CommonTaskHandler implements HttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String[] pathParts = getRequestPathParts(h);
        
        switch (method) {
            case "GET" -> {
                if (pathParts.length == 4) {
                    String path = pathParts[3];

                    if (path.equals("subtasks")) {
                        try {
                            int id = Integer.parseInt(pathParts[2]);

                            Epic epic = taskManager.getEpic(id);

                            if (epic != null) {
                                ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(epic);
                                sendText(h, gson.toJson(subtasks));
                            } else {
                                sendNotFound(h, TaskType.EPIC, id);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    handleGetEntity(h, TaskType.EPIC);
                }
            }
            case "POST" -> handlePostEntity(h, TaskType.EPIC);
            case "DELETE" -> handleDeleteEntity(h, TaskType.EPIC);
        }
    }
}
