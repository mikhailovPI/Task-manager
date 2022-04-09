package manager;

import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int index = 0;

    private HashMap<Integer, Task> userTasks = new HashMap<>();
    private HashMap<Integer, Subtask> userSubtasks = new HashMap<>();
    private HashMap<Integer, Epic> userEpics = new HashMap<>();

    private HistoryManager inMemoryHistoryManager = Managers.getHistoryDefault();

    //Создание задачи
    @Override
    public Task creatTask(Task task) {
        task.setId(++index);
        userTasks.put(task.getId(), task);
        return task;
    }

    //Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (!userTasks.containsKey(task.getId())) {
            return;
        }
        userTasks.put(task.getId(), task);
    }

    //Получение списка всех задач
    @Override
    public List<Task> listTask() {
        return new ArrayList<>(userTasks.values());
    }

    //Получение задачи по индексу
    @Override
    public Task getTask(int id) {
        if (userTasks.containsKey(id)) {
            inMemoryHistoryManager.add(userTasks.get(id));
        }
        return userTasks.get(id);
    }

    //Удаление всех задач
    @Override
    public void deleteTasks() {
        userTasks.clear();
    }

    //Удаление задачи по индексу
    @Override
    public void removeTask(int id) {
        userTasks.remove(id);
    }

    //Создание подзадачи
    @Override
    public void creatSubtask(Subtask subtask) {
        Epic epic = userEpics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtask.setId(++index);
        userSubtasks.put(subtask.getId(), subtask);
        epic.getListSubtask().add(subtask);
        statusEpic(epic);
    }

    //Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!userSubtasks.containsKey(subtask.getId())) {
            return;
        }
        Epic epic = userEpics.get(subtask.getEpicId());
        userSubtasks.put(subtask.getId(), subtask);
        statusEpic(epic);
    }

    //Получение списка всех подзадач
    @Override
    public List<Subtask> listSubtask() {
        return new ArrayList<>(userSubtasks.values());
    }

    //Получение подзадачи по индексу
    @Override
    public Subtask getSubtask(int id) {
        if (userSubtasks.containsKey(id)) {
            inMemoryHistoryManager.add(userSubtasks.get(id));
        }
        return userSubtasks.get(id);
    }

    //Удаление всех подзадач
    @Override
    public void deleteSubtasks(Subtask subtask) {
        Epic epic = userEpics.get(subtask.getEpicId());
        userSubtasks.clear();
        epic.getListSubtask().clear();
        statusEpic(epic);
    }

    //Удаление подзадачи по индексу
    @Override
    public void removeSubtask(int id) {
        userSubtasks.remove(id);
    }

    //Создание эпика
    @Override
    public void creatEpic(Epic epic) {
        epic.setId(++index);
        userEpics.put(epic.getId(), epic);
        statusEpic(epic);
    }

    //Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        if (userEpics.containsKey(epic.getId())) {
            return;
        }
        userEpics.put(epic.getId(), epic);
        statusEpic(epic);
    }

    //Получение списка всех эпиков
    @Override
    public List<Epic> listEpic() {
        return new ArrayList<>(userEpics.values());
    }

    //Получение эпика по индексу
    @Override
    public Epic getEpic(int id) {
        if (userEpics.containsKey(id)) {
            inMemoryHistoryManager.add(userEpics.get(id));
        }
        return userEpics.get(id);
    }

    //Удаление всех эпиков
    @Override
    public void deleteEpics(Epic epic) {
        userEpics.clear();
        epic.getListSubtask().clear();
        statusEpic(epic);
    }

    //Удаление эпика по индексу
    @Override
    public void removeEpic(int epicId) {
        userEpics.remove(epicId);

        userSubtasks.values().clear();
    }

    //Подзадачи эпика
    @Override
    public List<Subtask> getSubtaskByEpic(Epic epic) {
        return new ArrayList<Subtask>(epic.getListSubtask());
    }

    //Определение статуса эпика
    @Override
    public void statusEpic(Epic epic) {
        List<Subtask> subId = getSubtaskByEpic(epic);
        ArrayList<Subtask> list = new ArrayList<>();
        for (Subtask sub : subId) {
            list.add(getSubtask(sub.getId()));
        }
        int counterNew = 0;
        int counterDone = 0;
        for (Subtask subtask : list) {
            if (subtask.getStatus().equals("NEW")) {
                counterNew += 1;
            } else if (subtask.getStatus().equals("DONE")) {
                counterDone += 1;
            }
        }
        if (counterNew == list.size()) {
            epic.setStatus(StatusTask.NEW);
        } else if (counterDone == list.size()) {
            epic.setStatus(StatusTask.DONE);
        } else if (list.isEmpty()) {
            epic.setStatus(StatusTask.NEW);
            return;
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }
}