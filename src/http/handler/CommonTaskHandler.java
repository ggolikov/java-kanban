package http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.ManagerSaveException;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CommonTaskHandler extends BaseHttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;

    protected static String[] getRequestPathParts(HttpExchange h) {
        String path = h.getRequestURI().getPath();
        return path.split("/");
    }

    public CommonTaskHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;

        this.gson = HttpTaskServer.gson;
    }

    protected JsonElement parseRequestBody(HttpExchange h) throws IOException {
        InputStream inputStream = h.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            System.out.println("Task object is not a json object");
        }

        return jsonElement;
    }

    protected void handleGetEntity(HttpExchange h, TaskType taskType) throws IOException {
        String[] pathParts = getRequestPathParts(h);

        if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);

                Task entity = null;

                switch (taskType) {
                    case TASK -> entity = taskManager.getTask(id);
                    case SUBTASK -> entity = taskManager.getSubtask(id);
                    case EPIC -> entity = taskManager.getEpic(id);
                }

                if (entity != null) {
                    sendText(h, gson.toJson(entity));
                } else {
                    sendNotFound(h, taskType, id);
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                switch (taskType) {
                    case TASK -> sendText(h, gson.toJson(taskManager.getTasks()));
                    case SUBTASK -> sendText(h, gson.toJson(taskManager.getSubtasks()));
                    case EPIC -> sendText(h, gson.toJson(taskManager.getEpics()));
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected void handlePostEntity(HttpExchange h, TaskType taskType) throws IOException {
        JsonElement jsonElement = parseRequestBody(h);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement parsedId = jsonObject.get("id");

        Class<? extends Task> entityClass = null;
        ArrayList<? extends Task> entities = new ArrayList<>();

        switch (taskType) {
            case TASK -> {
                entityClass = Task.class;
                entities = taskManager.getTasks();
            }
            case SUBTASK -> {
                entityClass = Subtask.class;
                entities = taskManager.getSubtasks();
            }
            case EPIC -> {
                entityClass = Epic.class;
                entities = taskManager.getEpics();
            }
        }

        try {
            Task taskDeserialized = gson.fromJson(jsonElement, entityClass);
            if (parsedId != null) {
                int id = parsedId.getAsInt();
                boolean taskIsPresent = entities.stream().anyMatch(task -> task.getId() == id);

                if (taskIsPresent) {
                    addOrUpdateEntity(taskDeserialized, taskType, "update");

                    sendText(h, taskType + "with id " + id + " updated");
                } else {
                    addOrUpdateEntity(taskDeserialized, taskType, "add");

                    sendText(h, taskType + " added");
                }
            } else {
                addOrUpdateEntity(taskDeserialized, taskType, "add");

                sendText(h, taskType + " added");
            }
        } catch (Exception e) {
            sendInternalError(h, e.getMessage());
        }
    }

    protected void addOrUpdateEntity(Task taskDeserialized, TaskType taskType, String command) {
        switch (taskType) {
            case TASK -> {
                if (command.equals("add")) {
                    taskManager.addTask(taskDeserialized);
                } else {
                    taskManager.updateTask(taskDeserialized);
                }
            }
            case SUBTASK -> {
                if (command.equals("add")) {
                    taskManager.addSubtask((Subtask) taskDeserialized);
                } else {
                    taskManager.updateSubtask((Subtask) taskDeserialized);
                }
            }
            case EPIC -> {
                if (command.equals("add")) {
                    taskManager.addEpic((Epic) taskDeserialized);
                } else {
                    taskManager.updateEpic((Epic) taskDeserialized);
                }
            }
        }
    }

    protected void handleDeleteEntity(HttpExchange h, TaskType taskType) throws IOException {
        String[] pathParts = getRequestPathParts(h);

        if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);

                Task entity = null;

                switch (taskType) {
                    case TASK -> entity = taskManager.getTask(id);
                    case SUBTASK -> entity = taskManager.getSubtask(id);
                    case EPIC -> entity = taskManager.getEpic(id);
                }

                if (entity != null) {
                    switch (taskType) {
                        case TASK -> taskManager.removeTask(id);
                        case SUBTASK -> taskManager.removeSubtask(id);
                        case EPIC -> taskManager.removeEpic(id);
                    }
                    sendText(h, taskType + " with id " + id + " removed");
                } else {
                    sendNotFound(h, taskType, id);
                }
            } catch (NumberFormatException | ManagerSaveException e) {
                System.out.println(e.getMessage());
            }
        } else {
            sendText(h, gson.toJson(taskManager.getTasks()));
        }
    }
}
