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
        Task task = userTasks.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
        }
        return task;
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
        epic.getListSubtask().remove(userSubtasks.get(subtask.getId()));
        userSubtasks.put(subtask.getId(), subtask);
        epic.getListSubtask().add(subtask);
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
        Subtask subtask = userSubtasks.get(id);
        if(subtask!=null){
            inMemoryHistoryManager.add(subtask);
        }
        return subtask;
    }

    //Удаление всех подзадач
    @Override
    public void deleteSubtasks() {
        userSubtasks.clear();
        for (Epic epic: userEpics.values()) {
            epic.getListSubtask().clear();
            statusEpic(epic);
        }
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
    }

    //Получение списка всех эпиков
    @Override
    public List<Epic> listEpic() {
        return new ArrayList<>(userEpics.values());
    }

    //Получение эпика по индексу
    @Override
    public Epic getEpic(int id) {
        Epic epic = userEpics.get(id);
        if(epic!=null){
            inMemoryHistoryManager.add(epic);
        }
        return epic;
    }

    //Удаление всех эпиков
    @Override
    public void deleteEpics() {
        userEpics.clear();
        userEpics.clear();
    }

    //Удаление эпика по индексу
    @Override
    public void removeEpic(int epicId) {
        Epic epic = userEpics.get(epicId);
        userEpics.remove(epicId);
        epic.getListSubtask().clear();
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