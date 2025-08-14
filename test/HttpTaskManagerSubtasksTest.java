import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
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

public class HttpTaskManagerSubtasksTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.gson;

    final String BASE_URL = "http://localhost:8080";

    public HttpTaskManagerSubtasksTest() throws IOException {
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

    public HttpResponse<String> addOrUpdateEpic(HttpClient client, String json) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addOrUpdateSubtask(HttpClient client, String json) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteSubtask(HttpClient client, int subtaskId) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        int epicId = 1;
        Epic epic = TaskFabric.createEpic();
        addOrUpdateEpic(client, gson.toJson(epic));
        Subtask subtask = TaskFabric.createSubtask(epicId);
        HttpResponse<String> response = addOrUpdateSubtask(client, gson.toJson(subtask));

        assertEquals(200, response.statusCode());

        Epic epicsFromManager = manager.getEpic(epicId);
        List<Subtask> epicSubtasks = manager.getEpicSubtasks(epicsFromManager);

        assertNotNull(epicSubtasks, "Подтаски не возвращаются");
        assertEquals(1, epicSubtasks.size(), "Некорректное количество подтасок");
        assertEquals("Test 1", epicSubtasks.getFirst().getName(), "Некорректное имя подтаски");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        int epicId = 1;
        Epic epic = TaskFabric.createEpic();
        addOrUpdateEpic(client, gson.toJson(epic));
        Subtask subtask = TaskFabric.createSubtask(epicId);
        addOrUpdateSubtask(client, gson.toJson(subtask));

        Subtask subtask2 = TaskFabric.createSubtask(epicId);
        subtask2.setId(2);
        subtask2.setName("Test 1 updated");

        HttpResponse<String> response = addOrUpdateSubtask(client, gson.toJson(subtask2));

        assertEquals(200, response.statusCode());

        Epic epicsFromManager = manager.getEpic(epicId);
        List<Subtask> epicSubtasks = manager.getEpicSubtasks(epicsFromManager);

        assertNotNull(epicSubtasks, "Подтаски не возвращаются");
        assertEquals(1, epicSubtasks.size(), "Некорректное количество подтасок");
        assertEquals("Test 1 updated", epicSubtasks.getFirst().getName(), "Некорректное имя подтаски");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        int epicId = 1;
        Epic epic = TaskFabric.createEpic();
        addOrUpdateEpic(client, gson.toJson(epic));
        Subtask subtask = TaskFabric.createSubtask(epicId);
        addOrUpdateSubtask(client, gson.toJson(subtask));

        HttpResponse<String> response = deleteSubtask(client, 2);

        assertEquals(200, response.statusCode());

        Epic epicsFromManager = manager.getEpic(epicId);
        List<Subtask> epicSubtasks = manager.getEpicSubtasks(epicsFromManager);

        assertNotNull(epicSubtasks, "Подтаски не возвращаются");
        assertEquals(0, epicSubtasks.size(), "Некорректное количество подтасок");
    }
} 