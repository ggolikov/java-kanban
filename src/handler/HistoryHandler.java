package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        Gson gson = setupGson();

        if (method.equals("GET")) {
            ArrayList<Task> history = taskManager.getHistory();

            sendText(h, gson.toJson(history));
        }
    }
}
