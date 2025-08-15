import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerEpicsTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.gson;

    final String BASE_URL = "http://localhost:8080";

    public HttpTaskManagerEpicsTest() throws IOException {
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

    public HttpResponse<String> deleteEpic(HttpClient client, int epicId) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = TaskFabric.createEpic();
        HttpResponse<String> response = addOrUpdateEpic(client, gson.toJson(epic));

        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Test 1", epicsFromManager.getFirst().getName(), "Некорректное имя эпика");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = TaskFabric.createEpic();

        addOrUpdateEpic(client, gson.toJson(epic));
        Epic epic2 = TaskFabric.createEpic();

        epic2.setId(1);
        epic2.setName("Test 1 updated");
        HttpResponse<String> response = addOrUpdateEpic(client, gson.toJson(epic2));

        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Test 1 updated", epicsFromManager.getFirst().getName(), "Некорректное имя эпика");
    }
    
    @Test
    public void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = TaskFabric.createEpic();
        addOrUpdateEpic(client, gson.toJson(epic));

        HttpResponse<String> response = deleteEpic(client, 1);

        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(0, epicsFromManager.size(), "Некорректное количество эпиков");
    }
} 