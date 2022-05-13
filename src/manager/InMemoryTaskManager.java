package manager;

import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {

    protected long index = 0;

    protected HashMap<Long, Task> userTasks = new HashMap<>();
    protected HashMap<Long, Subtask> userSubtasks = new HashMap<>();
    protected HashMap<Long, Epic> userEpics = new HashMap<>();

    protected HistoryManager inMemoryHistoryManager = Managers.getHistoryDefault();

    //Создание задачи
    @Override
    public void addTask(Task task) {
        task.setId(++index);
        userTasks.put(task.getId(), task);
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
    public Task getTask(long id) {
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
    public void removeTask(long id) {
        inMemoryHistoryManager.remove(id);
        userTasks.remove(id);
    }

    //Создание подзадачи
    @Override
    public void addSubtask(Subtask subtask) {
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
    public Subtask getSubtask(long id) {
        Subtask subtask = userSubtasks.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.add(subtask);
        }
        return subtask;
    }

    //Удаление всех подзадач
    @Override
    public void deleteSubtasks() {
        userSubtasks.clear();
        for (Epic epic : userEpics.values()) {
            epic.getListSubtask().clear();
            statusEpic(epic);
        }
    }

    //Удаление подзадачи по индексу
    @Override
    public void removeSubtask(long id) {
        inMemoryHistoryManager.remove(id);
        userSubtasks.remove(id);
    }

    //Создание эпика
    @Override
    public void addEpic(Epic epic) {
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
    public Epic getEpic(long id) {
        Epic epic = userEpics.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
        }
        return epic;
    }

    //Удаление всех эпиков
    @Override
    public void deleteEpics() {
        userEpics.clear();
        userSubtasks.clear();
    }

    //Удаление эпика по индексу
    @Override
    public void removeEpic(long id) {
        inMemoryHistoryManager.remove(id);
        Epic epic = userEpics.get(id);
        userEpics.remove(id);
        for (Subtask subtask : epic.getListSubtask()) {
            userSubtasks.remove(subtask.getId());
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return Objects.equals(userTasks, that.userTasks) && Objects.equals(userSubtasks, that.userSubtasks)
                && Objects.equals(userEpics, that.userEpics)
                && Objects.equals(inMemoryHistoryManager, that.inMemoryHistoryManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userTasks, userSubtasks, userEpics, inMemoryHistoryManager);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "userTasks=" + userTasks +
                ", userSubtasks=" + userSubtasks +
                ", userEpics=" + userEpics +
                ", inMemoryHistoryManager=" + inMemoryHistoryManager +
                '}';
    }
}