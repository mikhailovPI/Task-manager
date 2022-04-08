package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    //Создание задачи
    Task creatTask(Task task);

    //Обновление задачи
    void updateTask(Task task);

    //Получение списка всех задач
    ArrayList<Task> listTask();

    //Получение задачи по индексу
    Task receiveTask(int id);

    //Удаление всех задач
    void deleteTask();

    //Удаление задачи по индексу
    void removeTask(int id);

    //Создание подзадачи
    Subtask creatSubtask(Subtask subtask);

    //Обновление подзадачи
    void updateSubtask(Subtask subtask);

    //Получение списка всех подзадач
    ArrayList<Subtask> listSubtask();

    //Получение подзадачи по индексу
    Subtask receiveSubtask(int id);

    //Удаление всех подзадач
    void deleteSubtask();

    //Удаление подзадачи по индексу
    void removeSubtask(int id);

    //Создание эпика
    void creatEpic(Epic epic);

    //Обновление эпика
    void updateEpic(Epic epic);

    //Получение списка всех эпиков
    ArrayList<Epic> listEpic();

    //Получение эпика по индексу
    Epic receiveEpic(int id);

    //Удаление всех эпиков
    void deleteEpic();

    //Удаление эпика по индексу
    void removeEpic(int epicId);

    //Подзадачи эпика
    ArrayList<Subtask> getSubtaskByEpic(Epic epic);

    //Определение статуса эпика
    void statusEpic(Epic epic);
}
