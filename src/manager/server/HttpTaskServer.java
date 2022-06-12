package manager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.interfaceClass.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static TaskManager manager;
    private static final Gson gson = getGson();
    private final HttpServer httpServer;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8080;
    private static final int LENGTH_URI = 3;

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start(); // запускаем сервер
    }

    public HttpTaskServer() throws IOException {
        manager =  Managers.getDefault();

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks/subtask/epic", new SubtasksByEpicHandler());

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdanter());
        return gsonBuilder.create();
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String path = httpExchange.getRequestURI().getPath(); // получение URI
            String requestMethod = httpExchange.getRequestMethod();
            int pathLength = path.split("/").length; //олучение длины массима path после split

            System.out.println("Обработка эндпоинта " + requestMethod + " /tasks");

            switch (requestMethod) {
                case "GET":
                    if (path.endsWith("tasks") && (pathLength == 2)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.getPrioritizedTasks()).getBytes(DEFAULT_CHARSET));
                            outputStream.write(("\n/получаем список Prioritized Tasks").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(("Данный метод не можем обработать.\n" +
                                "Используйте методы 'GET'").getBytes());
                        break;
                    }
            }
        }
    }

    class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String path = httpExchange.getRequestURI().getPath(); // получение URI
            String pathRequest = httpExchange.getRequestURI().getQuery(); // получение того, что после '?'
            String requestMethod = httpExchange.getRequestMethod();
            int pathLength = path.split("/").length; //олучение длины массима path после split

            System.out.println("Обработка эндпоинта " + requestMethod + " /tasks/task");
            switch (requestMethod) {
                case "GET":
                    if (path.endsWith("task") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.listTask()).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем список всех Task");
                            outputStream.write((gson.toJson(manager.listTask())).getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (pathRequest.startsWith("id=") && (pathLength == LENGTH_URI)) {
                        int id = Integer.parseInt(pathRequest.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.getTask(id)).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем Task по id");
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;
                case "POST":
                    try (inputStream) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        System.out.println("Тело запроса - " + body);
                        if (path.endsWith("task") && (pathLength == LENGTH_URI)) {

                            Task task = gson.fromJson(body, Task.class);
                            boolean idTask = manager.listTask().stream()
                                    .anyMatch((task1 -> task.getId() == task1.getId()));

                            httpExchange.sendResponseHeaders(201, 0);
                            OutputStream outputStream = httpExchange.getResponseBody();
                            if (idTask) {
                                manager.updateTask(task);
                                outputStream.write((gson.toJson(task).getBytes()));
                                System.out.println("Обновили Task");
                            } else {
                                manager.addTask(task);
                                outputStream.write((gson.toJson(task).getBytes()));
                                System.out.println("Создали новую Task");
                            }
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("task") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            manager.deleteTasks();
                            outputStream.write(("Все Task удалены").getBytes(DEFAULT_CHARSET));
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (pathRequest.startsWith("id=") && (pathLength == LENGTH_URI)) {
                        int id = Integer.parseInt(pathRequest.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            manager.removeTask(id);
                            outputStream.write(("Task с id = " + id + " удалена").getBytes(DEFAULT_CHARSET));
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(("Данный метод не можем обработать.\n" +
                                "Используйте методы 'GET', 'POST', 'DELETE'").getBytes());
                        break;
                    }
            }
        }
    }

    class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String path = httpExchange.getRequestURI().getPath(); // получение URI
            String pathRequest = httpExchange.getRequestURI().getQuery(); // получение того, что после '?'
            String requestMethod = httpExchange.getRequestMethod();
            int pathLength = path.split("/").length; //олучение длины массима path после split
            System.out.println("Обработка эндпоинта " + requestMethod + " /tasks/epic");

            switch (requestMethod) {
                case "GET":
                    if (path.endsWith("epic") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.listEpic()).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем список Epic");
                        }
                    } else if (pathRequest.startsWith("id=") && (pathLength == LENGTH_URI)) {
                        int id = Integer.parseInt(pathRequest.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.getEpic(id)).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем Epic по ид");
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(406, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;

                case "POST":
                    try (inputStream) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        System.out.println("Тело запроса - " + body);
                        if (path.endsWith("epic") && (pathLength == LENGTH_URI)) {
                            Epic epic = gson.fromJson(body, Epic.class);
                            boolean idTask = manager.listEpic().stream()
                                    .anyMatch((task1 -> epic.getId() == task1.getId()));
                            httpExchange.sendResponseHeaders(201, 0);
                            OutputStream outputStream = httpExchange.getResponseBody();
                            if (idTask) {
                                manager.updateEpic(epic);
                                outputStream.write((gson.toJson(epic).getBytes()));
                            } else {
                                manager.addEpic(epic);
                                outputStream.write((gson.toJson(epic)).getBytes());
                            }
                            outputStream.close();
                        }
                    }
                    break;

                case "DELETE":
                    if (path.endsWith("epic") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            manager.deleteEpics();
                            outputStream.write(("Все Epic удалены").getBytes(DEFAULT_CHARSET));
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (pathRequest.startsWith("id=") && (pathLength == LENGTH_URI)) {
                        int id = Integer.parseInt(pathRequest.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            manager.removeEpic(id);
                            outputStream.write(("Epic с id = " + id + " удален").getBytes(DEFAULT_CHARSET));
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(406, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;

                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(("Данный метод не можем обработать.\n" +
                                "Используйте методы 'GET', 'POST', 'DELETE'").getBytes());
                    }
            }
        }
    }

    class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String path = httpExchange.getRequestURI().getPath(); // получение URI
            String pathRequest = httpExchange.getRequestURI().getQuery(); // получение того, что после '?'
            String requestMethod = httpExchange.getRequestMethod();
            int pathLength = path.split("/").length; //олучение длины массима path после split

            System.out.println("Обработка эндпоинта " + requestMethod + " /tasks/subtask");

            switch (requestMethod) {
                case "POST":
                    try (inputStream) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        System.out.println("Тело запроса - " + body);
                        if (path.endsWith("subtask") && (pathLength == LENGTH_URI)) {
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            boolean idSubtask = manager.listEpic().stream()
                                    .anyMatch((subtask1 -> subtask.getId() == subtask1.getId()));

                            httpExchange.sendResponseHeaders(201, 0);
                            OutputStream outputStream = httpExchange.getResponseBody();
                            if (idSubtask) {
                                manager.updateSubtask(subtask);
                                outputStream.write((gson.toJson(subtask).getBytes()));
                            } else {
                                manager.addSubtask(subtask);
                                outputStream.write((gson.toJson(subtask).getBytes()));
                            }
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;

                case "GET":
                    if (path.endsWith("subtask") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.listSubtask()).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем список Subtask");
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (pathRequest.startsWith("id=") && (pathLength == LENGTH_URI)) {
                        int id = Integer.parseInt(pathRequest.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.getSubtask(id)).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем Subtask по ид");
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(406, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;

                case "DELETE":
                    if (path.endsWith("subtask") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            manager.deleteSubtasks();
                            outputStream.write(("Все Subtask удалены").getBytes(DEFAULT_CHARSET));
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (pathRequest.startsWith("id=") && (pathLength == LENGTH_URI)) {
                        int id = Integer.parseInt(pathRequest.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            manager.removeSubtask(id);
                            outputStream.write(("Subtask с id = " + id + " удалена").getBytes(DEFAULT_CHARSET));
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(406, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;

                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(("Данный метод не можем обработать.\n" +
                                "Используйте методы 'GET', 'POST', 'DELETE'")
                                .getBytes());
                    }
            }
        }
    }

    class SubtasksByEpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String path = httpExchange.getRequestURI().getPath(); // получение URI
            String pathRequest = httpExchange.getRequestURI().getQuery(); // получение того, что после '?'
            String requestMethod = httpExchange.getRequestMethod();
            int pathLength = path.split("/").length; //олучение длины массима path после split

            System.out.println("Обработка эндпоинта " + requestMethod + " /tasks/subtask/epic");

            switch (requestMethod) {
                case "GET":
                    if (pathRequest.startsWith("id=") && pathLength == 4) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            int id = Integer.parseInt(pathRequest.split("=")[1]);
                            Epic epic1 = null;
                            for (Epic epic : manager.listEpic()) {
                                if (epic.getId() == id) {
                                    epic1 = epic;
                                }
                            }
                            outputStream.write(gson.toJson(manager.getSubtaskByEpic(epic1)).getBytes(DEFAULT_CHARSET));
                            System.out.println("Получаем список Subtask для Epic");
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(406, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;

                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(("Данный метод не можем обработать.\n" +
                                "Используйте метод 'GET'").getBytes());
                    }
            }
        }
    }

    class HistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String path = httpExchange.getRequestURI().getPath(); // получение URI
            String requestMethod = httpExchange.getRequestMethod();
            int pathLength = path.split("/").length; //олучение длины массима path после split

            System.out.println("Обработка эндпоинта " + requestMethod + " /tasks/history");

            switch (requestMethod) {
                case "GET":
                    if (path.endsWith("history") && (pathLength == LENGTH_URI)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(manager.getHistory()).getBytes(DEFAULT_CHARSET));
                            outputStream.write(("\n/получаем список History").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.close();
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(("Данный метод не можем обработать.\n" +
                                "Используйте методы 'GET'").getBytes());
                        break;
                    }
            }
        }
    }
}