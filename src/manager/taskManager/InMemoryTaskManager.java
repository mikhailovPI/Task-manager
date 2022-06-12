
package manager.taskManager;

import manager.Managers;
import manager.exception.TaskTimeException;
import manager.interfaceClass.HistoryManager;
import manager.interfaceClass.TaskManager;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.time.Duration;
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
        timeCrossing(task);
        task.setId(++index);
        userTasks.put(task.getId(), task);
        prioritizedTasks.put(task.getStartTime(), task);
        task.getEndTime();
    }

    //Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (!userTasks.containsKey(task.getId())) {
            return;
        }
        timeCrossing(task);
        userTasks.put(task.getId(), task);
        prioritizedTasks.put(task.getStartTime(), task);
        task.getEndTime();
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
        for (Task task : userTasks.values()) {
            inMemoryHistoryManager.remove(task.getId());
            prioritizedTasks.remove(task.getStartTime(), task);
        }
        userTasks.clear();
    }

    //Удаление задачи по индексу
    @Override
    public void removeTask(long id) {
        for (Task task : userTasks.values()) {
            if (id == task.getId()) {
                prioritizedTasks.remove(task.getStartTime());
                break;
            }
        }
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
        timeCrossing(subtask);
        subtask.setId(++index);
        userSubtasks.put(subtask.getId(), subtask);
        epic.getListSubtask().add(subtask);
        prioritizedTasks.put(subtask.getStartTime(), subtask);
        statusEpic(epic);
        subtask.getEndTime();
        calculationTimeEpic(epic);
    }

    //Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!userSubtasks.containsKey(subtask.getId())) {
            return;
        }
        timeCrossing(subtask);
        Epic epic = userEpics.get(subtask.getEpicId());
        epic.getListSubtask().remove(userSubtasks.get(subtask.getId()));
        userSubtasks.put(subtask.getId(), subtask);
        prioritizedTasks.put(subtask.getStartTime(), subtask);
        epic.getListSubtask().add(subtask);
        statusEpic(epic);
        subtask.getEndTime();
        calculationTimeEpic(epic);
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
        for (Subtask subtask : userSubtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask.getStartTime());
        }
        for (Epic epic : userEpics.values()) {
            epic.getListSubtask().clear();
            statusEpic(epic);
        }
        userSubtasks.clear();
    }

    //Удаление подзадачи по индексу
    @Override
    public void removeSubtask(long id) {
        for (Subtask subtask : userSubtasks.values()) {
            if (id == subtask.getId()) {
                Epic epic = userEpics.get(subtask.getEpicId());
                epic.getListSubtask().remove(subtask);
                calculationTimeEpic(epic);
                prioritizedTasks.remove(subtask.getStartTime());
                epic.getEndTime();
                break;
            }
        }
        userSubtasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    //Создание эпика
    @Override
    public void addEpic(Epic epic) {
        epic.setId(++index);
        userEpics.put(epic.getId(), epic);
        statusEpic(epic);
        calculationTimeEpic(epic);
        epic.getEndTime();
    }

    //Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        if (!userEpics.containsKey(epic.getId())) {
            return;
        }
        userEpics.put(epic.getId(), epic);
        calculationTimeEpic(epic);
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
        for (Epic epic : userEpics.values()) {
            inMemoryHistoryManager.remove(epic.getId());
        }
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
        calculationTimeEpic(epic);
        epic.getEndTime();
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
        if (epic == null) {
            return;
        }
        if (getSubtaskByEpic(epic).isEmpty()) {
            epic.setStatus(StatusTask.NEW);
        }
        boolean a = getSubtaskByEpic(epic).stream().allMatch(Subtask -> Subtask.getStatus().equals(StatusTask.DONE));
        boolean b = getSubtaskByEpic(epic).stream().allMatch(Subtask -> Subtask.getStatus().equals(StatusTask.NEW));
        if (b) {
            epic.setStatus(StatusTask.NEW);
        } else if (a) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    //Получение истории задач
    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    //Сортировка задач по приоритету
    @Override
    public Map<LocalDateTime, Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    //Определение пересечения времени задач
    @Override
    public void timeCrossing(Task task) {
        for (Task oldTask : getPrioritizedTasks().values()) {
            if (task.getEndTime().isAfter(oldTask.getStartTime())
                    && task.getStartTime().isBefore(oldTask.getEndTime())) {
                throw new TaskTimeException();
            }
        }
    }

    //Расчет продолжительности и времени старта эпика
    public void calculationTimeEpic(Epic epic) {
        LocalDateTime minStartTime = LocalDateTime.of(3000, 1, 1, 0, 0);
        LocalDateTime maxEndTime = LocalDateTime.of(1980, 1, 1, 0, 0);

        for (Subtask subtask : getSubtaskByEpic(epic)) {
            if (minStartTime.isAfter(subtask.getStartTime())) {
                minStartTime = subtask.getStartTime();
            } else if (maxEndTime.isBefore(subtask.getStartTime().plusMinutes(subtask.getDuration()))) {
                maxEndTime = subtask.getStartTime().plusMinutes(subtask.getDuration());
            }
            epic.setStartTime(minStartTime);
            epic.setDuration(Duration.between(minStartTime, maxEndTime).toMinutes());
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
