package manager.interfaceClass;

import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    //Создание задачи
    void addTask(Task task);

    //Обновление задачи
    void updateTask(Task task);

    //Получение списка всех задач
    List<Task> listTask();

    //Получение задачи по индексу
    Task getTask(long id);

    //Удаление всех задач
    void deleteTasks();

    //Удаление задачи по индексу
    void removeTask(long id);

    //Создание подзадачи
    void addSubtask(Subtask subtask);

    //Обновление подзадачи
    void updateSubtask(Subtask subtask);

    //Получение списка всех подзадач
    List<Subtask> listSubtask();

    //Получение подзадачи по индексу
    Subtask getSubtask(long id);

    //Удаление всех подзадач
    void deleteSubtasks();

    //Удаление подзадачи по индексу
    void removeSubtask(long id);

    //Создание эпика
    void addEpic(Epic epic);

    //Обновление эпика
    void updateEpic(Epic epic);

    //Получение списка всех эпиков
    List<Epic> listEpic();

    //Получение эпика по индексу
    Epic getEpic(long id);

    //Удаление всех эпиков
    void deleteEpics();

    //Удаление эпика по индексу
    void removeEpic(long epicId);

    //Подзадачи эпика
    List<Subtask> getSubtaskByEpic(Epic epic);

    //Определение статуса эпика
    void statusEpic(Epic epic);

    List<Task> getHistory();

    //Сортировка задач по приоритету
    Map<LocalDateTime, Task> getPrioritizedTasks();

    //Определение пересечения времени задач
    void timeCrossing(Task task);
}