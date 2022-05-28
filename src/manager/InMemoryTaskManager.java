package manager;

import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected long index = 0;

    protected HashMap<Long, Task> userTasks = new HashMap<>();
    protected HashMap<Long, Subtask> userSubtasks = new HashMap<>();
    protected HashMap<Long, Epic> userEpics = new HashMap<>();
    protected Map<LocalDateTime, Task> prioritizedTasks = new TreeMap<>();

    protected HistoryManager inMemoryHistoryManager = Managers.getHistoryDefault();

    //Создание задачи
    @Override
    public void addTask(Task task) {
        task.setId(++index);
        userTasks.put(task.getId(), task);
        task.getEndTime();
        getPrioritizedTasks();
        timeCrossing(task);
    }

    //Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (!userTasks.containsKey(task.getId())) {
            return;
        }
        userTasks.put(task.getId(), task);
        task.getEndTime();
        getPrioritizedTasks();
        timeCrossing(task);
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
        getPrioritizedTasks();
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
        subtask.getEndTime();
        getPrioritizedTasks();
        timeCrossing(subtask);
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
        subtask.getEndTime();
        getPrioritizedTasks();
        timeCrossing(subtask);
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
        getPrioritizedTasks();
    }

    //Создание эпика
    @Override
    public void addEpic(Epic epic) {
        epic.setId(++index);
        userEpics.put(epic.getId(), epic);
        statusEpic(epic);
        epic.getEndTime();
    }

    //Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        if (!userEpics.containsKey(epic.getId())) {
            return;
        }
        userEpics.put(epic.getId(), epic);
        epic.getEndTime();
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
        List<Subtask> list = new ArrayList<>(epic.getListSubtask());
        return list;
    }

    //Определение статуса эпика
    @Override
    public void statusEpic(Epic epic) {
        List<Subtask> listSubtask = new ArrayList<>(getSubtaskByEpic(epic));

        if (epic == null) {
            return;
        }
        boolean a = listSubtask.stream().allMatch(Subtask -> Subtask.getStatus().equals(StatusTask.DONE));
        boolean b = listSubtask.stream().allMatch(Subtask -> Subtask.getStatus().equals(StatusTask.NEW));
        if (b) {
            epic.setStatus(StatusTask.NEW);
        } else if (a) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    //Сортировка задач по приоритету
    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> sortListTask = new ArrayList<>();
        for (Task task : userTasks.values()) {
            prioritizedTasks.put(task.getStartTime(), task);
        }
        for (Subtask subtask : userSubtasks.values()) {
            prioritizedTasks.put(subtask.getStartTime(), subtask);
        }
        sortListTask.addAll(prioritizedTasks.values());
        return sortListTask;
    }

    //Определение пересечения времени задач
    @Override
    public void timeCrossing(Task task) {
        try {
            for (Task oldTask : getPrioritizedTasks()) {
                if(task.getEndTime().isAfter(oldTask.getStartTime()) &&
                        task.getEndTime().isBefore(oldTask.getEndTime())) {
                    throw new RuntimeException();
                } else if (task.getStartTime().isAfter(oldTask.getStartTime()) &&
                        task.getStartTime().isBefore(oldTask.getEndTime())) {
                    throw new RuntimeException();
                } else if (task.getStartTime().isAfter(oldTask.getStartTime()) &&
                        task.getEndTime().isBefore(oldTask.getEndTime())) {
                    throw new RuntimeException();
                } else if(oldTask.getStartTime().isAfter(task.getStartTime()) &&
                        oldTask.getStartTime().isBefore(task.getEndTime())) {
                    throw new RuntimeException();
                }
            }
        } catch (RuntimeException e) {
            throw new TaskTimeException(e.getMessage());
        }
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