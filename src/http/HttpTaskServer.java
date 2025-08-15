package http;

import http.adapter.DurationTypeAdapter;
import http.adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.handler.*;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new TaskHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
            httpServer.createContext("/epics", new EpicHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        if (httpServer != null) {
            httpServer.start();
            System.out.println("HTTP Server started");
        }
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
            System.out.println("HTTP Server stopped");
        }
    }
}
