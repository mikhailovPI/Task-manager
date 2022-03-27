package TrackerTask;

import Task.Task;
import Task.Subtask;
import Task.Epic;

import java.util.ArrayList;
import java.util.HashMap;


public class Manager {

    private int index = 0;

    private HashMap<Integer, Task> userTask = new HashMap<>();
    private HashMap<Integer, Subtask> userSubtask = new HashMap<>();
    private HashMap<Integer, Epic> userEpic = new HashMap<>();


    //Создание задачи
    public Task creatTask(Task task) {
        task.setId(++index);
        userTask.put(task.getId(), task);
        System.out.println("Задача номер " + task.getId() + ": " + userTask.get(task.getId()));
        System.out.println("Описание задачи: " + task.getDescription());
        System.out.println("Статус задачи: " + task.getStatus() + "\n");
        return task;
    }

    //Обновление задачи
    public void updateTask(Task task) {
        if (userTask.containsKey(task.getId())) {
            System.out.println("Задача с таким именем уже существует! Производим замену...");
            userTask.put(task.getId(), task);
            System.out.println("Задача номер " + task.getId() + ": " + userTask.get(task.getId()));
            System.out.println("Описание задачи: " + task.getDescription());
            System.out.println("Статус задачи: " + task.getStatus() + "\n");
        } else {
            System.out.println("Задачи с индентификатором " + task.getId() + " не существует." + "\n");
        }
    }

    //Получение списка всех задач
    public ArrayList<Task> listTask() {
        System.out.println("Перед нами стоят следующие задачи:");
        return new ArrayList<>(userTask.values());
    }

    //Получение задачи по индексу
    public Task receiveTask(int id) {
        if (userTask.containsKey(id) == true) {
            System.out.println("Задача с идентификатором " + id + ": " + userTask.get(id) + "." + "\n");
        } else {
            System.out.println("Задачи с идентификатором " + id + " не существует!" + "\n");
        }
        return userTask.get(id);
    }

    //Удаление всех задач
    public void deleteTask() {
        userTask.clear();
        System.out.println("Все задачи удалены!" + "\n");
    }

    //Удаление задачи по индексу
    public void removeTask(int id) {
        if (userTask.containsKey(id) == true) {
            userTask.remove(id);
            System.out.println("Задача с идентификатором " + id + " удалена!" + "\n");
        } else {
            System.out.println("Задачи с идентификатором " + id + " не существует!" + "\n");
        }
    }

    //Создание подзадачи
    public Subtask creatSubtask(Subtask subtask) {
        subtask.setId(++index);
        userSubtask.put(subtask.getId(), subtask);
        System.out.println("Подзадача номер " + subtask.getId() + ": " + userSubtask.get(subtask.getId()));
        System.out.println("Описание подзадачи: " + subtask.getDescription());
        System.out.println("Статус подзадачи: " + subtask.getStatus() + "\n");
        return subtask;
    }

    //Обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        if (userSubtask.containsKey(subtask.getId())) {
            System.out.println("Подзадача с таким именем уже существует! Производим замену...");
            userSubtask.put(subtask.getId(), subtask);
            System.out.println("Подзадача номер " + subtask.getId() + ": " + userSubtask.get(subtask.getId()));
            System.out.println("Описание подзадачи: " + subtask.getDescription());
            System.out.println("Статус подзадачи: " + subtask.getStatus() + "\n");
        } else {
            System.out.println("Подзадачи с индентификатором " + subtask.getId() + " не существует." + "\n");
        }
    }

    //Получение списка всех подзадач
    public ArrayList<Subtask> listSubtask() {
        System.out.println("Перед нами стоят следующие подзадачи:");
        return new ArrayList<>(userSubtask.values());
    }

    //Получение подзадачи по индексу
    public Subtask receiveSubtask(int id) {
        if (userSubtask.containsKey(id) == true) {
            System.out.println("Подзадача с идентификатором " + id + ": " + userSubtask.get(id) + "." + "\n");
        } else {
            System.out.println("Подзадачи с идентификатором " + id + " не существует!" + "\n");
        }
        return userSubtask.get(id);
    }

    //Удаление всех подзадач
    public void deleteSubtask() {
        userSubtask.clear();
        System.out.println("Все подзадачи удалены!" + "\n");
    }

    //Удаление подзадачи по индексу
    public void removeSubtask(int id) {
        if (userTask.containsKey(id) == true) {
            userSubtask.remove(id);
            System.out.println("Подзадача с идентификатором " + id + " удалена!" + "\n");
        } else {
            System.out.println("Подзадача с идентификатором " + id + " не существует!" + "\n");
        }
    }

    //Создание эпика
    public Epic creatEpic(Epic epic) {
        epic.setId(++index);
        userEpic.put(epic.getId(), epic);
        System.out.println("Эпик номер " + epic.getId() + ": " + userEpic.get(epic.getId()));
        System.out.println("Описание эпика: " + epic.getDescription());
        System.out.println("Статус эпика: " + epic.getStatus() + "\n");
        return epic;
    }

    //Обновление эпика
    public void updateEpic(Epic epic) {
        if (userEpic.containsKey(epic.getId())) {
            System.out.println("Эпик с таким именем уже существует! Производим замену...");
            userEpic.put(epic.getId(), epic);
            System.out.println("Эпик номер " + epic.getId() + ": " + userEpic.get(epic.getId()));
            System.out.println("Описание эпика: " + epic.getDescription());
            System.out.println("Статус эпика: " + epic.getStatus() + "\n");
        } else {
            System.out.println("Эпика с индентификатором " + epic.getId() + " не существует." + "\n");
        }
    }

    //Получение списка всех эпиков
    public ArrayList<Epic> listEpic() {
        System.out.println("Перед нами стоят следующие подзадачи:");
        return new ArrayList<>(userEpic.values());
    }

    //Получение эпика по индексу
    public Epic receiveEpic(int id) {
        if (userEpic.containsKey(id) == true) {
            System.out.println("Эпик с идентификатором " + id + ": " + userEpic.get(id) + "." + "\n");
        } else {
            System.out.println("Эпика с идентификатором " + id + " не существует!" + "\n");
        }
        return userEpic.get(id);
    }

    //Удаление всех эпиков
    public void deleteEpic() {
        userEpic.clear();
        System.out.println("Все эпики удалены!" + "\n");
    }

    //Удаление эпика по индексу
    public void removeEpic(int id) {
        if (userEpic.containsKey(id) == true) {
            userEpic.remove(id);
            System.out.println("Эпик с идентификатором " + id + " удалена!" + "\n");
        } else {
            System.out.println("Эпика с идентификатором " + id + " не существует!" + "\n");
        }
    }

    private void statusEpic (Epic epic) {

    }


}