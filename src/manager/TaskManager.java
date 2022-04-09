package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    //Создание задачи
    Task creatTask(Task task);

    //Обновление задачи
    void updateTask(Task task);

    //Получение списка всех задач
    List<Task> listTask();

    //Получение задачи по индексу
    Task getTask(int id);

    //Удаление всех задач
    void deleteTask();

    //Удаление задачи по индексу
    void removeTask(int id);

    //Создание подзадачи
    Subtask creatSubtask(Subtask subtask);

    //Обновление подзадачи
    void updateSubtask(Subtask subtask);

    //Получение списка всех подзадач
    List<Subtask> listSubtask();

    //Получение подзадачи по индексу
    Subtask getSubtask(int id);

    //Удаление всех подзадач
    void deleteSubtask();

    //Удаление подзадачи по индексу
    void removeSubtask(int id);

    //Создание эпика
    void creatEpic(Epic epic);

    //Обновление эпика
    void updateEpic(Epic epic);

    //Получение списка всех эпиков
    List<Epic> listEpic();

    //Получение эпика по индексу
    Epic getEpic(int id);

    //Удаление всех эпиков
    void deleteEpic();

    //Удаление эпика по индексу
    void removeEpic(int epicId);

    //Подзадачи эпика
    List<Subtask> getSubtaskByEpic(Epic epic);

    //Определение статуса эпика
    void statusEpic(Epic epic);
}