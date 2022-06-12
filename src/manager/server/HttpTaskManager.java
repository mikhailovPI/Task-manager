package manager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.saveToFile.FileBackedTasksManager;
import manager.saveToFile.TypeTask;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {

    private final Gson gson = HttpTaskServer.getGson();
    private final KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");

    public HttpTaskManager() {
        load();
    }

    @Override
    public void save() {
        if (!listTask().isEmpty()) {
            kvTaskClient.put("task", gson.toJson(listTask()));
        }
        if (!listEpic().isEmpty()) {
            kvTaskClient.put("epic", gson.toJson(listEpic()));
        }
        if (!listSubtask().isEmpty()) {
            kvTaskClient.put("subtask", gson.toJson(listSubtask()));
        }
        if (!getHistory().isEmpty()) {
            kvTaskClient.put("history", gson.toJson(getHistory()));
        }
    }

    @Override
    public void load() {
        try {  // Task load
            ArrayList<Task> tasks = gson.fromJson(kvTaskClient.load("task"),
                    new TypeToken<ArrayList<Task>>() {
                    }.getType());
            for (Task task : tasks) {
                addTask(task);
            }
        }  catch (NullPointerException e) {
            System.out.println("Список сохраненных задач пуст");
        }

        try {  //Epic load
            ArrayList<Epic> epics = gson.fromJson(kvTaskClient.load("epic"),
                    new TypeToken<ArrayList<Epic>>(){}.getType());

            for (Epic epic : epics) {
                addEpic(epic);
            }
        } catch (NullPointerException e) {
            System.out.println("Список сохраненных эпиков пуст");
        }

        try {  //Subtask load
            ArrayList<Subtask> subtask = gson.fromJson(kvTaskClient.load("subtask"),
                    new TypeToken<ArrayList<Subtask>>(){}.getType());
            for (Subtask subtask1 : subtask) {
                addSubtask(subtask1);
            }
        } catch (NullPointerException e) {
            System.out.println("Список сохраненных подзадач пуст");
        }
        try {  //History load
            ArrayList<Task> history = gson.fromJson(kvTaskClient.load("history"),
                    new TypeToken<ArrayList<Task>>(){}.getType());
            for (Task task : history) {
                if (task.getTypeTask() == TypeTask.TASK) {
                    getTask(task.getId());
                } else if (task.getTypeTask() == TypeTask.EPIC) {
                    getEpic(task.getId());
                } else getEpic(task.getId());
            }
        } catch (NullPointerException e) {
            System.out.println("Список сохраненной истории пуст");
        }
    }
}