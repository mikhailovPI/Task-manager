package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private final Gson gson = HttpTaskServer.getGson();
    private HttpClient client;
    private HttpTaskServer server;

    Task taskOne = new Task("Task 1", "Описание Task 1", 1, StatusTask.NEW, 40,
            LocalDateTime.of(2022, Month.MAY, 27, 11, 0));
    Task taskTwo = new Task("Task 2", "Описание Task 2", 2, StatusTask.NEW, 40,
            LocalDateTime.of(2022, Month.MAY, 6, 12, 0));
    Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 3, StatusTask.NEW, 100,
            LocalDateTime.of(2022, Month.MAY, 2, 11, 0));
    Epic epicTwo = new Epic("Epic 2", "Описание Epic 2", 4, StatusTask.NEW, 100,
            LocalDateTime.of(2022, Month.MAY, 3, 11, 0));

    Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", 5,
            StatusTask.DONE, 40, LocalDateTime.of(2022, Month.MAY, 2, 15, 0), epicOne.getId());
    Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 6,
            StatusTask.NEW, 40, LocalDateTime.of(2022, Month.MAY, 3, 15, 0), epicOne.getId());

    @BeforeEach
    public void startServer() throws IOException {
        client = HttpClient.newHttpClient();
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void createNewEpicHttpTest() throws InterruptedException, IOException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(epicOne.getNameTask()));
    }

    @Test
    public void getEpicHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id=" + epicOne.getId());
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        System.out.println(responseGetTask.body());
        assertTrue(responseGetTask.body().contains(epicOne.getNameTask()));
    }

    @Test
    public void getWrongBodyEpicHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id=" + epicOne.getId());
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        assertFalse(responseGetTask.body().contains(epicTwo.getNameTask()));
    }

    @Test
    public void getAllEpicsHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(epicTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetEpic.statusCode(), "Статус-код не совпадает");
        assertTrue(responseGetEpic.body().contains(epicOne.getNameTask()), "Имя задачи не совпадает");
        assertTrue(responseGetEpic.body().contains(epicTwo.getNameTask()), "Имя задачи не совпадает");
    }

    @Test
    public void deleteEpicByIdHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(epicTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/epic/?id=" + epicOne.getId());

        HttpRequest requestDeleteEpic = HttpRequest.newBuilder().uri(urlDelete).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteEpic, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestGetEpic2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic2 = client.send(requestGetEpic2, HttpResponse.BodyHandlers.ofString());
        assertTrue(responseGetEpic2.body().contains(epicTwo.getNameTask()), "Имя задачи не совпадает");
    }

    @Test
    public void deleteAllEpicsHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(epicTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());
        assertTrue(responseGetEpic.body().contains(epicOne.getNameTask()), "Имя задачи не совпадает");
        assertTrue(responseGetEpic.body().contains(epicTwo.getNameTask()), "Имя задачи не совпадает");

        HttpRequest requestDeleteTask = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestGetEpic2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic2 = client.send(requestGetEpic2, HttpResponse.BodyHandlers.ofString());

        assertFalse(responseGetEpic2.body().contains(epicOne.getNameTask()), "Задача должна быть пустой");
        assertFalse(responseGetEpic2.body().contains(epicTwo.getNameTask()), "Задача должна быть пустой");
    }

    @Test
    public void createNewSubtaskHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(subtaskOne.getNameTask()));
    }

    @Test
    public void getSubTaskHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubTask = gson.toJson(subtaskOne);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubTask = URI.create("http://localhost:8080/tasks/subtask/?id="+subtaskOne.getId());
        HttpRequest requestGetSubTask = HttpRequest.newBuilder().uri(urlGetSubTask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetSubTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTask.statusCode());
        assertTrue(responseGetTask.body().contains(subtaskOne.getNameTask()));
    }

    @Test
    public void getWrongBodySubtaskHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubtask = URI.create("http://localhost:8080/tasks/subtask/?id="+subtaskOne.getId());
        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(urlGetSubtask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTask.statusCode());
        assertFalse(responseGetTask.body().contains(subtaskTwo.getNameTask()));
    }

    @Test
    public void getAllSubtasksHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetSubtasks = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetSubtasks.statusCode(), "Статус-код не совпадает");

        assertTrue(responseGetSubtasks.body().contains(subtaskOne.getNameTask()), "Имя задачи не совпадает");
        assertTrue(responseGetSubtasks.body().contains(subtaskTwo.getNameTask()), "Имя задачи не совпадает");
    }

    @Test
    public void deleteSubtaskByIdHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/subtask/?id="+subtaskOne.getId());
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(urlDelete).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetSubtasks2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());
        assertTrue(responseGetSubtasks2.body().contains(subtaskTwo.getNameTask()), "Имя задачи не совпадает");
    }

    //отдельно работает
    @Test
    public void deleteAllSubtasksHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetSubtasks2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());

        assertFalse(responseGetSubtasks2.body().contains(subtaskOne.getNameTask()), "Имя задачи совпадает");
        assertFalse(responseGetSubtasks2.body().contains(subtaskTwo.getNameTask()), "Имя задачи совпадает");
    }

    //не работает вообще
    @Test
    public void getEpicSubtasksHttpTest() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id="+epicOne.getId());
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        HttpResponse<String> responseGetEpic = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubtasks = URI.create("http://localhost:8080/tasks/subtask/epic/?id="+epicOne.getId());
        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(urlGetSubtasks).GET().build();
        HttpResponse<String> responseGetSubtasks = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetSubtasks.statusCode());

        Epic epicJson = gson.fromJson(responseGetEpic.body(), Epic.class);

        assertTrue(responseGetSubtasks.body().contains(epicJson.getListSubtask().get(0).getNameTask()),
                "Имена не совпадают");
        assertTrue(responseGetSubtasks.body().contains(epicJson.getListSubtask().get(1).getNameTask()),
                "Имена не совпадают");
    }

    @Test
    public void createNewTaskHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(taskOne.getNameTask()));
    }

    @Test
    public void getTaskHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/?id=" + taskOne.getId());
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        assertTrue(responseGetTask.body().contains(taskOne.getNameTask()));
    }

    @Test
    public void getWrongBodyTaskHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskTwo);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/?id=" + taskOne.getId());
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        assertFalse(responseGetTask.body().contains(taskOne.getNameTask()));
    }


    @Test
    public void getAllTasksHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne); // 'task1' here
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(taskTwo); // 'task2' here
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetTasks = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTasks.statusCode(), "Статус-код не совпадает");
        assertTrue(responseGetTasks.body().contains(taskOne.getNameTask()), "Имя задачи не совпадает");
        assertTrue(responseGetTasks.body().contains(taskTwo.getNameTask()), "Имя задачи не совпадает");
    }

    @Test
    public void deleteTaskByIdHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(taskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/task/?id="+taskOne.getId());
        HttpRequest requestDeleteTask = HttpRequest.newBuilder().uri(urlDelete).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestGetTask2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetTasks2 = client.send(requestGetTask2, HttpResponse.BodyHandlers.ofString());
        assertFalse(responseGetTasks2.body().contains(taskOne.getNameTask()), "Имя задачи не совпадает");
        assertTrue(responseGetTasks2.body().contains(taskTwo.getNameTask()), "Имя задачи не совпадает");
    }


    @Test
    public void deleteAllTasksHttpTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne); //
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(taskTwo);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDeleteTask = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        URI urlGetTasks2 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest requestGetTask2 = HttpRequest.newBuilder().uri(urlGetTasks2).GET().build();
        HttpResponse<String> responseGetTasks2 = client.send(requestGetTask2, HttpResponse.BodyHandlers.ofString());
        assertFalse(responseGetTasks2.body().contains(taskOne.getNameTask()), "Имя задачи не совпадает");
        assertFalse(responseGetTasks2.body().contains(taskTwo.getNameTask()), "Имя задачи не совпадает");
    }

    @Test
    public void getPrioritizedTasksHttpTest() throws IOException, InterruptedException {
        //epic
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        //task
        URI urlTask = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request3 = HttpRequest.newBuilder().uri(urlTask).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());
        //getPrioritizedTask
        URI UrlPrioritized = URI.create("http://localhost:8080/tasks");
        HttpRequest requestPrioritized = HttpRequest.newBuilder().uri(UrlPrioritized).GET().build();
        HttpResponse<String> response = client.send(requestPrioritized, HttpResponse.BodyHandlers.ofString());

        server.stop();
        assertEquals(200, response.statusCode(), "Неверный статус код");
        assertTrue(response.body().contains(taskOne.getNameTask()));
        assertFalse(response.body().contains(epicOne.getNameTask()));
    }

    //не работает вообще
    @Test
    public void getHistoryHttpTest() throws IOException, InterruptedException {
        //epic
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        //subtask1
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        subtaskOne.setEpicId((int) epicOne.getId());
        String jsonSubTask = gson.toJson(subtaskOne);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //task
        URI url3 = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        final HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/?id="+taskOne.getId());
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubTask = URI.create("http://localhost:8080/tasks/subtask/?id="+subtaskOne.getId());
        HttpRequest requestGetSubTask = HttpRequest.newBuilder().uri(urlGetSubTask).GET().build();
        client.send(requestGetSubTask, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id="+epicOne.getId());
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        URI urlHistory = URI.create("http://localhost:8080/tasks/history");
        HttpRequest requestHistory = HttpRequest.newBuilder().uri(urlHistory).GET().build();
        HttpResponse<String> response = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> history = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>(){}.getType());
        assertEquals(200, response.statusCode(), "Неверный статус код");
        assertTrue(response.body().contains(epicOne.getNameTask()));
        assertTrue(response.body().contains(subtaskOne.getNameTask()));
    }

    @Test
    public void wrongMethodEpic() throws InterruptedException, IOException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void wrongMethodTask() throws InterruptedException, IOException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void wrongMethodSubtask() throws InterruptedException, IOException {
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).PUT(bodySubtask).build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, responseSubtask.statusCode());
    }

    @Test
    public void wrongMethodSubtaskByEpic() throws InterruptedException, IOException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlHistory = URI.create("http://localhost:8080/tasks/history");
        HttpRequest requestHistory = HttpRequest.newBuilder().uri(urlHistory).DELETE().build();
        HttpResponse<String> response = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Неверный статус код");
    }

    @Test
    public void wrongMethodTasks() throws InterruptedException, IOException {
        //epic
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        //subtask1
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //subtask2
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        //task
        URI urlTask = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request3 = HttpRequest.newBuilder().uri(urlTask).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());
        //getPrioritizedTask
        URI UrlPrioritized = URI.create("http://localhost:8080/tasks");
        HttpRequest requestPrioritized = HttpRequest.newBuilder().uri(UrlPrioritized).DELETE().build();
        HttpResponse<String> response = client.send(requestPrioritized, HttpResponse.BodyHandlers.ofString());
        server.stop();

        assertEquals(404, response.statusCode(), "Неверный статус код");
    }
}