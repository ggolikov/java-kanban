package handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import Adapter.DurationTypeAdapter;
import Adapter.LocalDateTimeAdapter;
import manager.ManagerSaveException;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpHandler {
    protected TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, TaskType taskType, int id) throws IOException {
        String text = taskType + " with id " + id + " Not Found";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(404, 0);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendInternalError(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(503, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasOverlaps() {

    }

    protected Gson setupGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
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

    protected String[] getRequestPathParts(HttpExchange h) {
        String path = h.getRequestURI().getPath();
        return path.split("/");
    }

    protected void handleGetEntity(HttpExchange h, TaskType taskType) throws IOException {
        String[] pathParts = getRequestPathParts(h);

        Gson gson = setupGson();

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
                e.printStackTrace();
            }
        } else {
            try {
                switch (taskType) {
                    case TASK -> sendText(h, gson.toJson(taskManager.getTasks()));
                    case SUBTASK -> sendText(h, gson.toJson(taskManager.getSubtasks()));
                    case EPIC -> sendText(h, gson.toJson(taskManager.getEpics()));
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    protected void handlePostEntity(HttpExchange h, TaskType taskType) throws IOException {
        Gson gson = setupGson();

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

    protected void addOrUpdateEntity(Task taskDeserialized, TaskType taskType, String command) throws IOException {
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

        Gson gson = setupGson();

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
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (ManagerSaveException e) {
                e.printStackTrace();
            }
        } else {
            sendText(h, gson.toJson(taskManager.getTasks()));
        }
    }
}
