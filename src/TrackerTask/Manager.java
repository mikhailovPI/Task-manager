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
        return task;
    }

    //Обновление задачи
    public void updateTask(Task task) {
        if (userTask.containsKey(task.getId())) {
            userTask.put(task.getId(), task);
        } else {
            System.out.println("Задачи с индентификатором " + task.getId() + " не существует." + "\n");
        }
    }

    //Получение списка всех задач
    public ArrayList<Task> listTask() {
        return new ArrayList<>(userTask.values());
    }

    //Получение задачи по индексу
    public Task receiveTask(int id) {
        if (!userTask.containsKey(id) == true) {
            System.out.println("Задачи с идентификатором " + id + " не существует!" + "\n");
        } return userTask.get(id);
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
        } else {
            System.out.println("Задачи с идентификатором " + id + " не существует!" + "\n");
        }
    }

    //Создание подзадачи
    public Subtask creatSubtask(Subtask subtask) {
        subtask.setId(++index);
        userSubtask.put(subtask.getId(), subtask);
        return subtask;
    }

    //Обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        if (userSubtask.containsKey(subtask.getId())) {
            System.out.println("Подзадача с таким именем уже существует! Производим замену...");
            userSubtask.put(subtask.getId(), subtask);
        } else {
            System.out.println("Подзадачи с индентификатором " + subtask.getId() + " не существует." + "\n");
        }
    }

    //Получение списка всех подзадач
    public ArrayList<Subtask> listSubtask() {
        return new ArrayList<>(userSubtask.values());
    }

    //Получение подзадачи по индексу
    public Subtask receiveSubtask(int id) {
        if (!userSubtask.containsKey(id) == true) {
            System.out.println("Подзадачи с идентификатором " + id + " не существует!" + "\n");
        } return userSubtask.get(id);
    }

    //Удаление всех подзадач
    public void deleteSubtask() {
        userSubtask.clear();
        System.out.println("Все подзадачи удалены!" + "\n");
    }

    //Удаление подзадачи по индексу
    public void removeSubtask(int id) {
        if (!userTask.containsKey(id)) {
            userSubtask.remove(id);
        } else {
            System.out.println("Подзадача с идентификатором " + id + " не существует!" + "\n");
        }
    }

    //Создание эпика
    public void creatEpic(Epic epic) {
        epic.setId(++index);
        userEpic.put(epic.getId(), epic);
        statusEpic(epic.getId());
    }

    //Обновление эпика
    public void updateEpic(Epic epic) {
        if (userEpic.containsKey(epic.getId())) {
            userEpic.put(epic.getId(), epic);
        } else {
            System.out.println("Эпика с индентификатором " + epic.getId() + " не существует." + "\n");
        }
    }

    //Получение списка всех эпиков
    public ArrayList<Epic> listEpic() {
        return new ArrayList<>(userEpic.values());
    }

    //Получение эпика по индексу
    public Epic receiveEpic(int id) {
        if (!userEpic.containsKey(id) == true) {
            System.out.println("Эпика с идентификатором " + id + " не существует!" + "\n");
        } return userEpic.get(id);
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
            userSubtask.values().clear();
        } else {
            System.out.println("Эпика с идентификатором " + id + " не существует!" + "\n");
        }
    }

    //Определение статуса эпика
    private void statusEpic(int idEpic) {
        int counterNew = 0;
        int counterDone = 0;
        ArrayList<Subtask> list = userEpic.get(idEpic).getListSubtask();
        if (list.size() == 0) {
            userEpic.get(idEpic).setStatus("NEW");
        } else {
            for (Subtask subtask : list) {
                if (subtask.getStatus().equals("NEW")) {
                    counterNew += 1;
                } else if (subtask.getStatus().equals("DONE")) {
                    counterDone += 1;
                }
            }
            if (counterNew == list.size()) {
                userEpic.get(idEpic).setStatus("NEW");
            } else if (counterDone == list.size()) {
                userEpic.get(idEpic).setStatus("DONE");
            } else {
                userEpic.get(idEpic).setStatus("IN_PROGRESS");
            }
        }
    }

    public void getSubtaskByEpic(int idEpic) {
        if (!userEpic.containsKey(idEpic)) {
            System.out.println("Эпика с индификатором " + idEpic + "не существует.");
        } else {
            userEpic.get(idEpic).setListSubtask(listSubtask());
            System.out.println(userEpic.get(idEpic).getListSubtask());
        }
    }
}