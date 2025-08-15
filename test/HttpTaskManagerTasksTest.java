import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.gson;

    final String BASE_URL = "http://localhost:8080";

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    public HttpResponse<String> addOrUpdateTask(HttpClient client, String json) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteTask(HttpClient client, int taskId) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = TaskFabric.createTask();
        HttpResponse<String> response = addOrUpdateTask(client, gson.toJson(task));

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = TaskFabric.createTask();

        addOrUpdateTask(client, gson.toJson(task));
        Task task2 = TaskFabric.createTask();
        task2.setId(1);
        task2.setName("Test 1 updated");

        HttpResponse<String> response = addOrUpdateTask(client, gson.toJson(task2));

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1 updated", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }


    @Test
    public void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = TaskFabric.createTask();
        addOrUpdateTask(client, gson.toJson(task));

        HttpResponse<String> response = deleteTask(client, 1);

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
} 