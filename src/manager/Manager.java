package manager;

import task.Task;
import task.Subtask;
import task.Epic;

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
        return task;
    }

    //Обновление задачи
    public void updateTask(Task task) {
        if (!userTask.containsKey(task.getId())) {
            return;
        }
        userTask.put(task.getId(), task);
    }

    //Получение списка всех задач
    public ArrayList<Task> listTask() {
        return new ArrayList<>(userTask.values());
    }

    //Получение задачи по индексу
    public Task receiveTask(int id) {
        return userTask.get(id);
    }

    //Удаление всех задач
    public void deleteTask() {
        userTask.clear();
    }

    //Удаление задачи по индексу
    public void removeTask(int id) {
        userTask.remove(id);
    }

    //Создание подзадачи
    public Subtask creatSubtask(Subtask subtask) {
        subtask.setId(++index);
        userSubtask.put(subtask.getId(), subtask);
        return subtask;
    }

    //Обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        if (!userSubtask.containsKey(subtask.getId())) {
            return;
        }
        userSubtask.put(subtask.getId(), subtask);
    }

    //Получение списка всех подзадач
    public ArrayList<Subtask> listSubtask() {
        return new ArrayList<>(userSubtask.values());
    }

    //Получение подзадачи по индексу
    public Subtask receiveSubtask(int id) {
        return userSubtask.get(id);
    }

    //Удаление всех подзадач
    public void deleteSubtask() {
        userSubtask.clear();
    }

    //Удаление подзадачи по индексу
    public void removeSubtask(int id) {
        userSubtask.remove(id);
    }

    //Создание эпика
    public void creatEpic(Epic epic) {
        epic.setId(++index);
        userEpic.put(epic.getId(), epic);
        statusEpic(epic);
    }

    //Обновление эпика
    public void updateEpic(Epic epic) {
        if (userEpic.containsKey(epic.getId())) {
            return;
        }
        userEpic.put(epic.getId(), epic);
        statusEpic(epic);
    }

    //Получение списка всех эпиков
    public ArrayList<Epic> listEpic() {
        return new ArrayList<>(userEpic.values());
    }

    //Получение эпика по индексу
    public Epic receiveEpic(int id) {
        return userEpic.get(id);
    }

    //Удаление всех эпиков
    public void deleteEpic() {
        userEpic.clear();
        userSubtask.clear();
    }

    //Удаление эпика по индексу
    public void removeEpic(int epicId) {
        userEpic.remove(epicId);
        userSubtask.values().clear();
    }

    //Подзадачи эпика
    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        return new ArrayList<Subtask>(epic.getListSubtask());
    }

    //Определение статуса эпика
    private void statusEpic(Epic epic) {
        ArrayList<Subtask> subId = getSubtaskByEpic(epic);
        ArrayList<Subtask> list = new ArrayList<>();
        for (Subtask sub : subId) {
            list.add(receiveSubtask(sub.getId()));
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
            epic.setStatus("NEW");
        } else if (counterDone == list.size()) {
            epic.setStatus("DONE");
        } else if (list.isEmpty()) {
            epic.setStatus("NEW");
            return;
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}