package http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.TaskType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
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
}
